package com.example.tcpsocket;

import android.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpServer extends Thread {
    private MainActivity activity;
    private String TAG = "cjh";
    public TcpServer(MainActivity activity) {
        this.activity = activity;
    }
    @Override
    public void run() {
        // 创建ServerSocket
        ServerSocket serverSocket;
        int serverPort;
        try {
            serverPort = Integer.parseInt(this.activity.et2.getText().toString().trim());
        } catch (RuntimeException e) {
            Log.d(TAG, "server port is not legal, set to 9999." + e);
            this.activity.tv3.append("\nserver port is not legal, set to 9999." + e);
            this.activity.et2.setText("9999");
            serverPort = 9999;
        }
        try {
            serverSocket = new ServerSocket(serverPort);
            // 监听端口，等待客户端连接
            while (!serverSocket.isClosed()) {
                Log.d(TAG,"--服务器持续监听端口--" + serverSocket);
                this.activity.tv3.append("\n--服务器持续监听端口--" + serverSocket);
                Socket socket = serverSocket.accept(); //等待客户端连接
                new ServerThread(socket, this.activity).start();
            }
        } catch (IOException e) {
            Log.d(TAG, "serverSocket error." + e);
            this.activity.tv3.append("\nserverSocket error." + e);
        }
    }
}

