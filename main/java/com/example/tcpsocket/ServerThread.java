package com.example.tcpsocket;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ServerThread extends Thread implements View.OnClickListener {
    Socket socket;
    MainActivity activity;
    String TAG = "cjh";
    public ServerThread(Socket socket, MainActivity activity) {
        this.socket = socket;
        this.activity = activity;
    }

    @Override
    public void run() {
        Log.d(TAG,"服务器得到客户端连接，建立socket：" + this.socket);
        this.activity.tv1.append("\n服务器得到客户端连接，建立socket：" + this.socket);
        this.activity.btn3.setOnClickListener(this);
        this.activity.btn4.setOnClickListener(this);

        while(this.socket !=null && this.socket.isConnected() && this.socket.isBound() && !this.socket.isClosed()){
            try {
//                Log.d(TAG, socket.isInputShutdown()+"");
//                Log.d(TAG, socket.isOutputShutdown()+"");
//                Log.d(TAG, socket.isConnected()+"");
//                Log.d(TAG, socket.isClosed()+"");
//                Log.d(TAG, socket.isBound()+"");
                // 获取服务端返回的信息
                InputStream inputStream = this.socket.getInputStream();
                byte[] byteArray = new byte[1024*50];
                int length = inputStream.read(byteArray);
                Log.d(TAG,"server inputStream length = "+length);
                Log.d(TAG,"server inputStream = "+ Arrays.toString(byteArray));
                if (length == -1) {
                    serverCloseSocket();
                    break;
                } else {
                    String replyMessage = new String(byteArray,0,length);
                    Log.d(TAG,"服务器收到的消息:"+replyMessage+" "+this.socket);
                    this.activity.tv1.append("\n服务器收到的消息:"+replyMessage);
                }
            } catch (IOException e) {
                Log.d(TAG,"server socket error, " + e);
                this.activity.tv1.append("\nserver socket error, " + e);
            }
        }
        Log.d(TAG,"ServerThread Closed");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn3:
                serverSendMsg();
                break;
            case R.id.btn4:
                serverCloseSocket();
                break;
        }
    }

    private void serverSendMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!socket.isClosed()) {
                    String serverMsg = activity.et5.getText().toString().trim();
                    if(serverMsg.length()!=0){
                        Log.d(TAG, "服务器发送: " + serverMsg + ", socket:" + socket);
                        activity.tv1.append("\n服务器发送: " + serverMsg);
                        try {
                            // 使用输出流给客户端发送一条信息
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(serverMsg.getBytes());
                            // 只要涉及管道的都建议刷新一下
                            outputStream.flush();
                        } catch (IOException e) {
                            Log.d(TAG,"server outputStream error, " + e);
                            activity.tv1.append("\nserver outputStream error, " + e);
                        }
                    }
                }
            }
        }).start();
    }

    private void serverCloseSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, socket.isInputShutdown()+"");
//                Log.d(TAG, socket.isOutputShutdown()+"");
//                Log.d(TAG, socket.isConnected()+"");
                Log.d(TAG, socket.isClosed()+"");
                Log.d(TAG, socket.isBound()+"");
                if (!socket.isClosed()){
                    try {
                        // 资源关闭
                        Log.d(TAG,"服务器断开连接"+socket);
                        activity.tv1.append("\n服务器断开连接");
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException e) {
                        Log.d(TAG,"server close socket error, " + e);
                        activity.tv1.append("\nserver close socket error, " + e);
                    }
                }
            }
        }).start();
    }
}
