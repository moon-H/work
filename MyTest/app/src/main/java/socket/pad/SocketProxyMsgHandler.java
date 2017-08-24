package socket.pad;

/**
 * Created by IntelliJ IDEA.
 * User: wangjf
 * Date: 2016-3-10
 * Time: 2:45:04
 * To change this template use File | Settings | File Templates.
 */
public interface SocketProxyMsgHandler {

    /**
     * When a ENQ recieved, will call this method.
     * CAUTION: this method call still run in socket reading Thread.
     * Next version will use a thread to Execute msgHandler.
     *
     * @param sp
     * @param msg
     * @return
     */
    public abstract int OnMsgEnq(SocketProxy sp, CssShmMsgStruct msg) throws Exception;
}
