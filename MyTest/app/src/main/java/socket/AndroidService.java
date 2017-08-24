package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AndroidService {


    public static void main(String[] args) throws IOException {
        ServerSocket serivce = new ServerSocket(30000);
        while (true) {
            //等待客户端连接
            Socket socket = serivce.accept();
            new Thread(new AndroidRunable(socket)).start();
        }
    }

}
