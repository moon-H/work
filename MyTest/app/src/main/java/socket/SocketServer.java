package socket;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.utils.ByteConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    BufferedWriter writer = null;
    ServerSocket server;
    BufferedReader reader;
    Socket socket = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SocketServer socket = new SocketServer();
        socket.startServer();
    }

    public void startServer() {


        try {
            server = new ServerSocket(9666);
            System.out.println("服务器启动.....");
            socket = server.accept();
            manageConnection(socket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
    }

    private static final int ESP_MAX_PACKET_SIZE = 4096;

    private byte[] tempLength = new byte[2];

    public void manageConnection(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Client connected.....");
                while (true) {
                    try {
                        System.out.println("start ----------------------");
                        byte[] headBuff = new byte[16];


                        InputStream in = socket.getInputStream();

                        int readLen = espReadFully(in, headBuff, 16);
                        if (readLen < 0) {
                            System.out.println("readLen < 0.....");
                            //            MLog.d(TAG, "Read head length = " + readLen);
                        }
                        System.arraycopy(headBuff, 15, tempLength, 0, 1);
                        System.arraycopy(headBuff, 14, tempLength, 1, 1);
                        System.out.println("org byte info  = " + HexConverter.bytesToHexString(tempLength));
                        int iLength = ByteConverter.byteToShortAsc(tempLength)+2;//最后2个字节是报文校验码
                        System.out.println("trace1 ----------------------= " + iLength);
                        if (iLength <= 0) {
                            // Read Wrong Data. Will Close Socket.
                            //                        return null;
                            System.out.println("iLength <= 0.....");
                        }
                        //                    if (iLength > ESP_MAX_PACKET_SIZE) {
                        //                        // Out of Max Size.
                        //                        MLog.d(TAG, "readMsgFromSocket Out of Max Size : " + ESP_MAX_PACKET_SIZE);
                        //                        return null;
                        //                    }

                        System.out.println("trace1 ----------------------");

                        byte[] msgBuf = new byte[iLength];
                        readLen = espReadFully(in, msgBuf, iLength);
                        if (readLen < 0)
                            break;
                        System.out.println("readMsgFromSocket head : " + HexConverter.bytesToHexString(headBuff) + " length = " + iLength + " body = " + HexConverter.bytesToHexString(msgBuf));

                    } catch (IOException e1) {
                        e1.printStackTrace();
                        break;
                    }
                }
                System.out.println("end ----------------------");

                //                    DataInputStream input = new DataInputStream(socket.getInputStream());
                //                    byte[] data = new byte[0];
                //                    input.read(data);
                //
                //                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //                    //                    new Timer().schedule(new TimerTask() {
                //                    //
                //                    //                        @Override
                //                    //                        public void run() {
                //                    //                            try {
                //                    //                                System.out.println("heart beat once....");
                //                    //                                String msga = "heart beat once...#哈哈#";
                //                    //                                String ss = new String(msga.getBytes("utf-8"), "utf-8");
                //                    //                                writer.write(ss);
                //                    //                                writer.flush();
                //                    //                            } catch (IOException e) {
                //                    //                                // TODO Auto-generated catch block
                //                    //                                e.printStackTrace();
                //                    //                            }
                //                    //
                //                    //                        }
                //                    //                    }, 3000, 3000);
                //                    String receiveMsg = "";
                //                    while ((receiveMsg = reader.readLine()) != null) {
                //                        receiveMsg += reader.readLine();
                //                        System.out.println("Res = " + receiveMsg);
                //
                //                        //                        if (receiveMsg.equals("#AA#")) {
                //                        //
                //                        //
                //                        //                            writer.write("server reply : " + receiveMsg + "\n");
                //                        //                            writer.flush();
                //                        //                        }
                //                    }
            }

        }

        ).start();

    }

    private int espReadFully(InputStream in, byte[] msgBuf, int iLength) throws IOException {
        int iReadTotalLength = 0;
        // read Data
        while (true) {
            int readLen = in.read(msgBuf, iReadTotalLength, iLength - iReadTotalLength);
            System.out.println("start in  ---------------------- " + iReadTotalLength + " iLength = " + iLength);

            if (readLen <= 0) {
                return -1;
            }

            iReadTotalLength += readLen;

            if (iReadTotalLength >= iLength)
                break;
        }

        return 0;
    }
}
