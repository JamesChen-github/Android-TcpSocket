package com.example.tcpsocket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServerThread extends Thread {
    private final String TAG = "cjh MainServer";
    private int serverPort;
    public static final int mainServerStarted = 1;
    public static final int serverThreadStarted = 2;
    private Handler mUIhandler ; //主线程的Handler

//    /*
//     * 执行初始化任务
//     * */
//    @Override
//    protected void onLooperPrepared() {
//        Log.d(TAG, "onLooperPrepared: 线程开始执行");
//        super.onLooperPrepared();
//    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setHandler(Handler handler) {
        this.mUIhandler = handler;
    }

    @Override
    public void run(){
        // 创建ServerSocket
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
            Message msg = Message.obtain();
            msg.obj = "content";
            msg.what = mainServerStarted;
            mUIhandler.sendMessage(msg);
            // 监听端口，等待客户端连接
            while (!serverSocket.isClosed()) {
                Log.d(TAG,"--服务器持续监听端口--" + serverSocket);
                Socket socket = serverSocket.accept(); //等待客户端连接
                Log.d(TAG,"服务器得到客户端连接，建立socket：" + socket);
                new ServerThread(socket).start();
                Message msg2 = Message.obtain();
                msg.obj = "content2";
                msg.what = serverThreadStarted;
                mUIhandler.sendMessage(msg2);
            }
        } catch (IOException e) {
            Log.d(TAG, "serverSocket error." + e);
        }
    }
}
