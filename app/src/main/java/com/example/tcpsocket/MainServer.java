package com.example.tcpsocket;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer extends Thread {
    private String TAG = "cjh MainServer";
    private int serverPort;
    public MainServer(MainActivity activity, int serverPort) {
        this.serverPort = serverPort;
    }
    @Override
    public void run() {
        // 创建ServerSocket
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(serverPort);



            //告诉main  mainserver start了


            // 监听端口，等待客户端连接
            while (!serverSocket.isClosed()) {
                Log.d(TAG,"--服务器持续监听端口--" + serverSocket);
                Socket socket = serverSocket.accept(); //等待客户端连接
                new ServerThread(socket).start();


                //告诉main  serverthread start了

            }
        } catch (IOException e) {
            Log.d(TAG, "serverSocket error." + e);
        }
    }
}
