package com.example.tcpsocket;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientThread extends Thread {
    private Socket socket;
    private String targetIp;
    private int targetPort;
    private final String TAG = "cjh ClientThread";
    public boolean clientClosed;
    public boolean clientInputShutdown;
    public boolean clientOutputShutdown;
    public boolean clientConnected;
    public boolean clientBounded;

    public ClientThread(String targetIp, int targetPort) {
        this.targetIp = targetIp;
        this.targetPort = targetPort;
    }

    public void closeSocket() {
        try{
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {



        try {
            socket = new Socket(targetIp,targetPort);
            Log.d(TAG,"客户端连接到服务器：" + socket);



            //告诉main成功了


//            refreshStatus();
            while(this.socket !=null && this.socket.isConnected() && this.socket.isBound()){
                InputStream inputStream = this.socket.getInputStream();
                byte[] byteArray = new byte[1024*50];
                int length = 0;
                try {
                    length = inputStream.read(byteArray);
                } catch (Exception e) {
                    Log.d(TAG,"client: inputStream read error, " + e);
                }
                Log.d(TAG, "client: socket.isClosed()=" + socket.isClosed()+"");
                Log.d(TAG, "client: socket.isInputShutdown()=" + socket.isInputShutdown()+"");
                Log.d(TAG, "client: socket.isOutputShutdown()=" + socket.isOutputShutdown()+"");
                Log.d(TAG,"client inputStream length = "+length);
                Log.d(TAG,"client inputStream = "+ Arrays.toString(byteArray));
                if (length == -1) {
//                    clientCloseSocket();
                    break;
                } else {
                    String replyMessage = new String(byteArray,0,length);
                    Log.d(TAG,"客户端收到的消息:"+replyMessage+" "+this.socket);
                }
            }
        } catch (Exception e) {
            Log.d(TAG,"client socket error, " + e);
        }
        Log.d(TAG,"ClientThread Closed");
    }

    public void refreshStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(socket.isClosed()){
//                        activity.tv9.setText("isClose==true");
                    }else {
//                        activity.tv9.setText("isClose==false");
                    }
                    if(socket.isInputShutdown()){
//                        activity.tv10.setText("isInputShutdown==true");
                    }else {
//                        activity.tv10.setText("isInputShutdown==false");
                    }
                    if(socket.isOutputShutdown()){
//                        activity.tv11.setText("isOutputShutdown==true");
                    }else {
//                        activity.tv11.setText("isOutputShutdown==false");
                    }
                    if(socket.isConnected()){
//                        activity.tv12.setText("isConnected==true");
                    }else {
//                        activity.tv12.setText("isConnected==false");
                    }
                    if(socket.isBound()){
//                        activity.tv13.setText("isBound==true");
                    }else {
//                        activity.tv13.setText("isBound==false");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }



    public void clientShutdownOutput() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isOutputShutdown()){
            try {
                Log.d(TAG,"client shutdownOutput");
                socket.shutdownOutput();
            } catch (IOException e) {
                Log.d(TAG,"client shutdownOutput error, " + e);
            }
        } else {
            Log.d(TAG,"client socket.isOutputShutdown() == true");
        }
            }
        }).start();
    }

    public void clientShutdownInput() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isInputShutdown()){
            try {
                Log.d(TAG,"client shutdownInput");
                socket.shutdownInput();
            } catch (IOException e) {
                Log.d(TAG,"client shutdownInput error, " + e);
            }
        } else {
            Log.d(TAG,"client socket.isInputShutdown() == true");
        }
            }
        }).start();
    }


    public void clientSendMsg(String clientMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isClosed()){

            if(clientMsg.length()!=0){
                Log.d(TAG, "客户端发送: " + clientMsg + ", socket:" + socket);
                try {
                    // 使用输出流给客户端发送一条信息
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(clientMsg.getBytes());
                    // 只要涉及管道的都建议刷新一下
//                    outputStream.flush();
                } catch (IOException e) {
                    Log.d(TAG,"client outputStream error, " + e);
                }
            }
        }
            }
        }).start();
    }

    public void clientCloseSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isClosed()){
            try {
                Log.d(TAG,"client socket.close");
                socket.close();
            } catch (IOException e) {
                Log.d(TAG,"client close socket error, " + e);
            }
        } else {
            Log.d(TAG,"client socket.isClosed() == true");
        }
            }
        }).start();
    }
}