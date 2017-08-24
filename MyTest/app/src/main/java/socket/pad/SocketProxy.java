package socket.pad;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: wangjf
 * Date: 2016-3-10
 * Time: 2:22:36
 * To change this template use File | Settings | File Templates.
 */
public class SocketProxy {
    private static final String TAG = SocketProxy.class.getSimpleName();

    private static final int RECONNECT_TIME = 1000 * 5;
    public static final int INIT_CMD = 0x021003;

    /*  ServerSocket
    Socket
    */

    public static final int MSG_SYS_MSG_QUEUE_CREATED = 0x0020;
    public static final int MSG_SYS_MSG_QUEUE_REMOVED = 0x0021;

    private final int ESP_MAX_ROUTE_COUNT = 100;
    private final int ESP_MAX_PACKET_SIZE = 4096;

    private int g_LocalAppId;
    private int g_LocalDestAppId;
    private String serverIpAddress;
    private int iPort;

    private Object lock = new Object();

    private Hashtable g_espRouteMap = new Hashtable();

    private SocketProxyMsgHandler msgHandler = null;

    private MsgHandleThread mhThread;

    public int getLocalAppId() {
        return g_LocalAppId;
    }

    /**
     * Create a SocketProxy.
     *
     * @param iAppId           Local AppId.
     * @param iDestAppId       Destination AppId, if iDestAppId Equals to Local AppId, will start a Server to accept Remote App, otherwise as a client..
     * @param pServerIpAddress Ip Address of Remote Server.
     * @param iPort            port number of Remote Server, or listen port.
     */
    public SocketProxy(int iAppId, int iDestAppId, String pServerIpAddress, int iPort, SocketProxyMsgHandler msgHandler) {
        this.g_LocalAppId = iAppId;
        this.g_LocalDestAppId = iDestAppId;
        this.serverIpAddress = pServerIpAddress;
        this.iPort = iPort;

        this.msgHandler = msgHandler;
    }

    /**
     * Will Start a Server or a Client in new Thread.
     */
    public void startup() {
        mhThread = new MsgHandleThread(this);
        Runnable run = new SocketProc();
        Thread th = new Thread(run, "SocketProxy-AppId#" + g_LocalAppId);

        th.start();
    }

    /**
     * Send a Msg to dest module Only.
     *
     * @param mDestModuleId    Destination Module  Id(e.g agticket Process will be 1).
     * @param nMsgId           iCmd.
     * @param iParam0
     * @param iParam1          Not used.
     * @param iParam2          Not used.
     * @param iParam3          Not used.
     * @param iParam4          Not used.
     * @param iParam5          Not used.
     * @param iParamDataLength Not used.
     * @param pszParamData     Msg Data in Jason or URL QueryString format.
     * @return
     * @throws Exception
     */
    public int MsgSendOnlyEnq(int mDestModuleId, int nMsgId, int iParam0, int iParam1, int iParam2, int iParam3, int iParam4, int iParam5, int iParamDataLength, String pszParamData) throws Exception {
        Socket sock = espFindRoute(mDestModuleId);
        if (sock == null)
            return -1;

        CssShmMsgStruct stMsg = CssShmMsgStruct.makeMsgStruct(g_LocalAppId, mDestModuleId, nMsgId, iParam0, iParam1, iParam2, iParam3, iParam4, iParam5, iParamDataLength, pszParamData);

        stMsg.iNetFlag = 0; // ENQ
        stMsg.bNeedAck = false;
        stMsg.iSeqId = getSeqId();
        return espWriteMsgToSocket(sock, stMsg);
    }

    /**
     * Send ENQ and wait for ACK. default time out is 30s.
     *
     * @param mDestModuleId
     * @param nMsgId           iCmd
     * @param iParam0          not used.
     * @param iParam1          not used.
     * @param iParam2          not used.
     * @param iParam3          not used.
     * @param iParam4          not used.
     * @param iParam5          not used.
     * @param iParamDataLength not used.
     * @param pszParamData
     * @return ACK Msg, Check ACK Msg.stProcMsg.iParam0 to test mACK Code.
     * @throws Exception
     */
    public CssShmMsgStruct MsgSendEnq(int mDestModuleId, int nMsgId, int iParam0, int iParam1, int iParam2, int iParam3, int iParam4, int iParam5, int iParamDataLength, String pszParamData/*,
                 CssShmMsgStruct ppStruCssShmMsgAck*/) throws Exception {
        Socket sock = espFindRoute(mDestModuleId);
        if (sock == null)
            return null;

        CssShmMsgStruct stMsg = CssShmMsgStruct.makeMsgStruct(g_LocalAppId, mDestModuleId, nMsgId, iParam0, iParam1, iParam2, iParam3, iParam4, iParam5, iParamDataLength, pszParamData);

        stMsg.iNetFlag = 0; // ENQ
        stMsg.bNeedAck = true;
        stMsg.iSeqId = getSeqId();
        System.out.println("hahah2 ");
        int iRet = espWriteMsgToSocket(sock, stMsg);
        if (iRet != 0)
            return null;

        // Wait ACK.
        String key = "" + g_LocalAppId + "-" + stMsg.iSeqId;
        Object enqLock = new Object();
        synchronized (ackReqList) {
            ackReqList.put(key, enqLock);
        }


        try {
            synchronized (enqLock) {
                enqLock.wait(30 * 1000); //. 30s
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            synchronized (ackReqList) {
                Object value = ackReqList.remove(key);
                if (value == null || !(value instanceof CssShmMsgStruct))
                    return null; // NO Ack

                CssShmMsgStruct ackMsg = (CssShmMsgStruct) value;

                if (ackMsg.iAppId != stMsg.iDestAppId ||
                    ackMsg.iDestAppId != stMsg.iAppId ||
                    ackMsg.iNetFlag != 1 || // ACK
                    ackMsg.iSeqId != stMsg.iSeqId) {
                    return null;
                }
                return ackMsg;
            }
        }

    }

    private int g_SeqId = 0;

    private synchronized int getSeqId() {
        g_SeqId = (g_SeqId + 1) % 100000;
        if (g_SeqId == 0)
            g_SeqId = 1;
        return g_SeqId;

    }

    /**
     * Response an ACK for ENQ
     *
     * @param pStruCssShmMsg   The <b>ENQ</b> Msg.
     * @param mAckCode         ACK Code. 0 is OK. other values indicate ENQ not be proceed, or ENQ msg is Wrong.
     * @param iParam1
     * @param iParam2
     * @param iParam3
     * @param iParam4
     * @param iParam5
     * @param iParamDataLength
     * @param pszParamData
     * @return
     * @throws Exception
     */
    public int MsgSendAck(CssShmMsgStruct pStruCssShmMsg, int mAckCode, int iParam1, int iParam2, int iParam3, int iParam4, int iParam5, int iParamDataLength, String pszParamData) throws Exception {
        Socket sock = espFindRoute(pStruCssShmMsg.iAppId);
        if (sock == null)
            return -1;

        CssShmMsgStruct stMsg = CssShmMsgStruct.makeMsgStruct(g_LocalAppId, pStruCssShmMsg.iAppId, pStruCssShmMsg.stProcMsg.iCmd, mAckCode, iParam1, iParam2, iParam3, iParam4, iParam5, iParamDataLength, pszParamData);

        stMsg.iNetFlag = 1; // ACK
        stMsg.bNeedAck = false;
        stMsg.iSeqId = pStruCssShmMsg.iSeqId;

        return espWriteMsgToSocket(sock, stMsg);
    }

    /**
     * Not implemennt yet.
     *
     * @return 0
     */
    public int Close() {
        return 0;
    }

    /**
     * Use System.out as log.
     *
     * @param s
     */
    public void log(String s) {
        System.out.println(s);
        //        Log.d(TAG, s);
    }

    synchronized Socket espFindRoute(int iAppId) {
        Integer iKey = new Integer(iAppId);
        return (Socket) g_espRouteMap.get(iKey);
    }


    synchronized int espRegisterRoute(int iAppId, Socket sock) //, pthread_mutex_t pMutex)
    {
        // printf ("Enter Lock : %s, %d\n", __FILE__, __LINE__) ;
        Socket pRoute = espFindRoute(iAppId);

        if (pRoute != null) {
            espRemoveRoute(iAppId);
        }

        Integer iKey = new Integer(iAppId);
        g_espRouteMap.put(iKey, sock);
        // Write  MSG ;
        CssShmMsgStruct stMsg = new CssShmMsgStruct();

        stMsg.iAppId = iAppId;
        stMsg.iDestAppId = g_LocalAppId;
        stMsg.iNetFlag = 0; // ENQ
        stMsg.bNeedAck = false;
        stMsg.iSeqId = 0;
        stMsg.stProcMsg.iCmd = MSG_SYS_MSG_QUEUE_CREATED;

        CssShmWriteEnqToMsgQ(stMsg);

        // printf ("UnLock : %s, %d\n", __FILE__, __LINE__) ;
        return 0;
    }

    synchronized int espRemoveRoute(int iAppId) {
        // printf ("Enter Lock : %s, %d\n", __FILE__, __LINE__) ;
        Socket sock = espFindRoute(iAppId);

        if (sock == null)
            return 0;

        Integer iKey = new Integer(iAppId);
        g_espRouteMap.remove(iKey);
        if (!sock.isClosed()) {
            try {
                sock.close();
            } catch (Throwable t) {
            }
        }

        log("espRemoveRoute iAppId = [" + iAppId + "], SocketClosed.");
        // pRoute->iSocketMutex = NULL ;

        // Write  MSG ;
        CssShmMsgStruct stMsg = new CssShmMsgStruct();

        stMsg.iAppId = iAppId;
        stMsg.iDestAppId = g_LocalAppId;
        stMsg.iNetFlag = 0; // ENQ
        stMsg.bNeedAck = false;
        stMsg.iSeqId = 0;
        stMsg.stProcMsg.iCmd = MSG_SYS_MSG_QUEUE_REMOVED;

        CssShmWriteEnqToMsgQ(stMsg);
        return 0;
    }

    private int CssShmWriteEnqToMsgQ(CssShmMsgStruct stMsg) {
        mhThread.Enqueue(stMsg);
        return 0;
    }

    int callToMsgHandler(CssShmMsgStruct stMsg) {
        if (msgHandler == null)
            return -1;
        try {
            msgHandler.OnMsgEnq(this, stMsg);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0;
    }

    private Hashtable ackReqList = new Hashtable();

    private void CssShmWriteAckToMsgQ(int iDestAppId, int iSeqId, CssShmMsgStruct pstMsg) {
        // ignore iDestAppId
        String key = "" + iDestAppId + "-" + iSeqId;
        Object enqLock = null;
        synchronized (ackReqList) {
            enqLock = ackReqList.remove(key);
            if (enqLock == null) {
                // no such enq.
                log("ACK Request not found: iDestAppI=" + iDestAppId + ", iSeqId=" + iSeqId);
                return;
            }

            ackReqList.put(key, pstMsg);
        }

        synchronized (enqLock) {
            // wakeup waiting.
            enqLock.notifyAll();
        }

    }

    private int espWriteMsgToSocket(Socket sock, CssShmMsgStruct stMsg) throws Exception {
        synchronized (sock) {
            byte[] buf = CssShmMsgStruct.pack(stMsg);
            int iRet = 0;
            OutputStream out = sock.getOutputStream();

            CssShmMsgStruct.writeNumberToStream(out, buf.length, 4);
            System.out.println("write msg = " + stMsg);

            out.write(buf);
            out.flush();
        }
        return 0;
    }

    CssShmMsgStruct espReadMsgFromSocket(Socket sock) throws Exception {
        // read first 4 bytes
        byte[] lenBuf = new byte[4];

        InputStream in = sock.getInputStream();

        int readLen = espReadFully(in, lenBuf, 4);

        if (readLen < 0)
            return null;

        int iLength = (int) CssShmMsgStruct.bytesToNumber(lenBuf, 0, 4);

        if (iLength <= 0) {
            // Read Wrong Data. Will Close Socket.
            return null;
        }

        if (iLength > ESP_MAX_PACKET_SIZE) {
            // Out of Max Size.
            return null;
        }


        byte[] msgBuf = new byte[iLength];
        readLen = espReadFully(in, msgBuf, iLength);

        if (readLen < 0)
            return null;
        // Check Size
        return CssShmMsgStruct.unpack(msgBuf);
    }

    private int espReadFully(InputStream in, byte[] msgBuf, int iLength) throws IOException {
        int iReadTotalLength = 0;
        // read Data
        while (true) {
            int readLen = in.read(msgBuf, iReadTotalLength, iLength - iReadTotalLength);

            if (readLen <= 0) {
                return -1;
            }

            iReadTotalLength += readLen;

            if (iReadTotalLength >= iLength)
                break;
        }

        return 0;
    }

    private class SocketProc implements Runnable {

        private int StartExchangeSPServer() {
            while (true) {
                try {
                    StartExchangeSPServer0();

                    log("SpServer StartUp Error, will restart after 10s.");

                    Thread.currentThread().sleep(100 * 1000);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        int StartExchangeSPServer0() throws IOException {
            log("Start Exchange Socket Proxy Server... \n");
            ServerSocket server = new ServerSocket(iPort);
            log("Exchange Socket Proxy Server running on port " + iPort);
            int seq = 1;
            while (true) {
                final Socket client = server.accept();
                if (client == null) {
                    break;
                }

                log("Accept a New Client.");
                /*accept_request((void *)client_sock);*/

                Thread th = new Thread(new Runnable() {
                    public void run() {
                        try {
                            espAcceptRequest(client);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }, "SocketProxy-Client#" + seq);

                th.start();
                seq++;

            }

            server.close();

            return -1;
        }

        void StartExchangeSPClient() {
            while (true) {
                try {

                    int i = StartExchangeSPClient0();

                    Log.d(TAG, "StartExchangeSPClient Result = " + i);
                    //                    Thread.currentThread().sleep(5 * 1000);
                } catch (Exception t) {
                    log("Client Connect Error, will restart after 5s.");
                    t.printStackTrace();
                    log(t.getMessage());
                    try {
                        Thread.sleep(RECONNECT_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        int StartExchangeSPClient0() throws Exception {
            Socket sock = null;

            int iDestAppId = g_LocalDestAppId;
            sock = new Socket(serverIpAddress, iPort);

            CssShmMsgStruct stMsg = new CssShmMsgStruct();
            stMsg.iAppId = g_LocalAppId;
            stMsg.iDestAppId = iDestAppId;
            stMsg.iNetFlag = 0; // ENQ ;
            stMsg.bNeedAck = true;

            try {
                System.out.println("hahah1 ");
                int iRet = espWriteMsgToSocket(sock, stMsg);

                if (iRet != 0) {
                    // Error, remove and Exit
                    log("Can't Write Msg.");
                    sock.close();
                    return -1;
                }

                byte pMsgBuf[] = new byte[ESP_MAX_PACKET_SIZE];
                stMsg = espReadMsgFromSocket(sock);

                if (stMsg == null) {
                    // Error, remove and Exit
                    sock.close();
                    return -2;
                }

                // Check is ACK & Dest AppId is Correct.

                if (stMsg.iNetFlag != 1 ||  // ACK
                    stMsg.stProcMsg.iCmd != 0 || // initialize
                    stMsg.stProcMsg.iParam0 != INIT_CMD ||// IPCMSG_OK || // OK
                    stMsg.iAppId != iDestAppId) {
                    // Error, remove and Exit .

                    sock.close();
                    return -3;
                }

                // Register to ESP Route Map.
                espRegisterRoute(iDestAppId, sock);

                while (true) {
                    // Read Socket for Package
                    CssShmMsgStruct pstMsg = espReadMsgFromSocket(sock);

                    if (pstMsg == null) {
                        // Error.
                        break;
                    }
                    log("Read Msg From Socket: iAppId=[" + pstMsg.iAppId +
                        "], iDestAppId=[" + pstMsg.iDestAppId + "], iCmd=[" +
                        pstMsg.stProcMsg.iCmd + "].");
                    log("readMsg = " + pstMsg.toString());
                    if (pstMsg.iNetFlag == 0) // ENQ
                    {
                        CssShmWriteEnqToMsgQ(pstMsg);
                    } else if (pstMsg.iNetFlag == 1) // ACK
                    {
                        CssShmWriteAckToMsgQ(pstMsg.iDestAppId, pstMsg.iSeqId, pstMsg);
                    } else {
                        ;
                    }

                    Thread.currentThread().sleep(10);

                }
            } catch (Exception _t) {
                throw _t;
            } finally {
                // Remove from ESP Route Map ;
                if (sock != null && !sock.isClosed()) {
                    sock.close();
                }
                espRemoveRoute(iDestAppId);
            }


            return 0;
        }

        int espAcceptRequest(Socket sock) throws Exception {
            CssShmMsgStruct stMsg;
            int iDestAppId = -1;
            try {

                stMsg = espReadMsgFromSocket(sock);
                System.out.println("espAcceptRequest stMsg= " + stMsg.toString());
                if (stMsg == null) {
                    // Error, remove and Exit
                    sock.close();
                    return -2;
                }
                // Check is ACK & Dest AppId is Correct.


                if (stMsg.iNetFlag != 0 ||  // ENQ
                    stMsg.stProcMsg.iCmd != INIT_CMD || // initialize
                    stMsg.iDestAppId != g_LocalAppId) {
                    sock.close();
                    return -3;
                }

                iDestAppId = stMsg.iAppId;

                stMsg.iAppId = g_LocalAppId;
                stMsg.iDestAppId = iDestAppId;
                stMsg.iNetFlag = 1; // ACK ;
                stMsg.bNeedAck = false;

                int iRet = espWriteMsgToSocket(sock, stMsg);
                System.out.println("hahah ");

                if (iRet != 0) {
                    // Error, remove and Exit
                    log("Can't Write Msg.");
                    sock.close();
                    return -4;
                }

                // Register to ESP Route Map.
                espRegisterRoute(iDestAppId, sock);

                while (true) {
                    // Read Socket for Package
                    CssShmMsgStruct pstMsg = espReadMsgFromSocket(sock);

                    if (pstMsg == null) {
                        // Error.
                        break;
                    }
                    log("Read Msg From Socket: iAppId=[" + pstMsg.iAppId +
                        "], iDestAppId=[" + pstMsg.iDestAppId + "], iCmd=[" +
                        pstMsg.stProcMsg.iCmd + "].");
                    log("Res : " + pstMsg);
                    if (pstMsg.iNetFlag == 0) // ENQ
                    {
                        CssShmWriteEnqToMsgQ(pstMsg);
                    } else if (pstMsg.iNetFlag == 1) // ACK
                    {
                        CssShmWriteAckToMsgQ(pstMsg.iDestAppId, pstMsg.iSeqId, pstMsg);
                    } else {
                        ;
                    }

                    Thread.currentThread().sleep(10);

                }
            } catch (Exception _t) {
                throw _t;
            } finally {
                // Remove from ESP Route Map ;
                if (sock != null && !sock.isClosed()) {
                    sock.close();
                }
                espRemoveRoute(iDestAppId);
            }

            return 0;
        }

        public void run() {
            if (g_LocalDestAppId != g_LocalAppId) {
                // As Client ;
                StartExchangeSPClient();
            } else {
                // As Server
                StartExchangeSPServer();
            }
        }


    }

}
