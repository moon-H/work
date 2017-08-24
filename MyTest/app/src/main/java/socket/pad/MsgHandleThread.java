package socket.pad;

import java.util.ArrayList;

/**
 * To Process Msg outside of Socket Read Thread.
 * This Class use a LinkedList as a Queue to Execute MsgHandler one by one.
 *
 * @see SocketProxyMsgHandler
 */
public class MsgHandleThread implements Runnable {
    private Thread th;
    private SocketProxy sp;

    private ArrayList msgQueue = new ArrayList(512);

    public MsgHandleThread(SocketProxy sp) {
        this.sp = sp;
        th = new Thread(this, "SP-MsgHandleThread: iAppId#" + sp.getLocalAppId());

        th.start();
    }

    public void run() {

        while (true) {
            Object item = null;
            synchronized (msgQueue) {
                if (msgQueue.isEmpty()) {
                    try {
                        msgQueue.wait();
                    } catch (Throwable _t) {
                        _t.printStackTrace();
                    } finally {
                        continue;
                    }
                }
                // remove first
                item = msgQueue.remove(0);
            }
            if (item == null || !(item instanceof CssShmMsgStruct))
                continue;
            // Call to MsgHandler

            try {
                sp.callToMsgHandler((CssShmMsgStruct) item);
            } catch (Throwable _t) {
                System.out.println("Call to msgHandler");
                _t.printStackTrace();
            }
        }
    }

    public void Enqueue(CssShmMsgStruct msg) {
        synchronized (msgQueue) {
            msgQueue.add(msg);
            msgQueue.notifyAll();
        }


    }
}
