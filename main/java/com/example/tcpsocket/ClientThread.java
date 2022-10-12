package com.example.tcpsocket;

import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientThread extends Thread implements View.OnClickListener {
    private MainActivity activity;
    private Socket socket;
    private String TAG = "cjh";

    public ClientThread(MainActivity activity) {
        this.activity = activity;
        this.activity.btn5.setOnClickListener(this);
        this.activity.btn6.setOnClickListener(this);
    }
    @Override
    public void run() {
        String targetIp;
        int targetPort;
        if (this.activity.et3.getText().toString().trim().equals("")){
            try {
                this.activity.et3.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        targetIp = this.activity.et3.getText().toString().trim();
        try {
            targetPort = Integer.parseInt(this.activity.et4.getText().toString().trim());
        } catch (RuntimeException e) {
            Log.d(TAG, "target port is not legal, set to 9999." + e);
            this.activity.tv2.append("\ntarget port is not legal, set to 9999." + e);
            this.activity.et4.setText("9999");
            targetPort = 9999;
        }


        try {
            socket = new Socket(targetIp,targetPort);
            Log.d(TAG,"客户端连接到服务器：" + socket);
            this.activity.tv2.append("\n客户端连接到服务器：" + socket);
            while(this.socket !=null && this.socket.isConnected() && this.socket.isBound()){
                InputStream inputStream = this.socket.getInputStream();
                byte[] byteArray = new byte[1024*50];
                int length = inputStream.read(byteArray);
                Log.d(TAG,"client inputStream length = "+length);
                Log.d(TAG,"client inputStream = "+ Arrays.toString(byteArray));
                if (length == -1) {
                    clientCloseSocket();
                    break;
                } else {
                    String replyMessage = new String(byteArray,0,length);
                    Log.d(TAG,"客户端收到的消息:"+replyMessage+" "+this.socket);
                    this.activity.tv2.append("\n客户端收到的消息:"+replyMessage);
                }
            }
        } catch (IOException e) {
            Log.d(TAG,"client socket error, " + e);
            this.activity.tv2.append("\nclient socket error, " + e);
        }
        Log.d(TAG,"ClientThread Closed");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn5:
                clientSendMsg();
                break;
            case R.id.btn6:
                clientCloseSocket();
                break;
        }
    }


    private void clientSendMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!socket.isClosed()){
                    String clientMsg = activity.et6.getText().toString().trim();
                    if(clientMsg.length()!=0){
                        Log.d(TAG, "客户端发送: " + clientMsg + ", socket:" + socket);
                        activity.tv2.append("\n客户端发送: " + clientMsg);
                        try {
                            // 使用输出流给客户端发送一条信息
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(clientMsg.getBytes());
                            // 只要涉及管道的都建议刷新一下
                            outputStream.flush();
                        } catch (IOException e) {
                            Log.d(TAG,"client outputStream error, " + e);
                            activity.tv2.append("\nclient outputStream error, " + e);
                        }
                    }
                }
            }
        }).start();
    }

    private void clientCloseSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!socket.isClosed()){
                    try {
                        // 资源关闭
                        Log.d(TAG,"客户端断开连接"+socket);
                        activity.tv2.append("\n客户端断开连接");
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException e) {
                        Log.d(TAG,"client close socket error, " + e);
                        activity.tv2.append("\nclient close socket error, " + e);
                    }
                }
            }
        }).start();
    }
}
