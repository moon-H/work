package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class AndroidRunable implements Runnable {

    Socket socket = null;

    public AndroidRunable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 向android客户端输出hello worild
        System.out.println("new---------------------");
        String line = null;
        InputStream input;
        OutputStream output;
        int i = 0;
        String str = "hello world!";
        str = str + i;
        try {
            //向客户端发送信息
            output = socket.getOutputStream();
            input = socket.getInputStream();
            BufferedReader bff = new BufferedReader(new InputStreamReader(input));
            output.write(str.getBytes("gbk"));
            output.flush();
            //半关闭socket
            socket.shutdownOutput();
            //获取客户端的信息
            while ((line = bff.readLine()) != null) {
                System.out.println("LWX:: " + new String(line.getBytes(), "utf-8"));
            }
            i++;
            //关闭输入输出流
            output.close();
            bff.close();
            input.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}