package com.example.tcpsocket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class ServerThread extends Thread {
    static Socket socket;
    static String TAG = "cjh ServerThread";
    public boolean serverClosed;
    public boolean serverInputShutdown;
    public boolean serverOutputShutdown;
    public boolean serverConnected;
    public boolean serverBounded;
    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public static void closeSocket() {
        try{
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.d(TAG,"服务器得到客户端连接，建立socket：" + this.socket);
//        refreshStatus();

        while(this.socket !=null && this.socket.isConnected() && this.socket.isBound() && !this.socket.isClosed()){
            try {
                // 获取服务端返回的信息
                InputStream inputStream = this.socket.getInputStream();
                byte[] byteArray = new byte[1024*50];
//                byte[] byteArray = new byte[10];
                int length = 0;
                socket.setSoTimeout(10000);
                socket.setKeepAlive(false);
                socket.setTcpNoDelay(true);
//                socket.setReceiveBufferSize(1);
//                socket.setSendBufferSize(1);
                socket.sendUrgentData(333);

                try {
                    length = inputStream.read(byteArray);
                } catch (SocketTimeoutException e) {
                    Log.d(TAG,"server: SocketTimeoutException error, " + e);
                } catch (Exception e) {
                    Log.d(TAG,"server: inputStream read error, " + e);
                }
                Log.d(TAG, "server: socket.isClosed()=" + socket.isClosed()+"");
                Log.d(TAG, "server: socket.isInputShutdown()=" + socket.isInputShutdown()+"");
                Log.d(TAG, "server: socket.isOutputShutdown()=" + socket.isOutputShutdown()+"");
                Log.d(TAG,"server inputStream length = "+length);
                Log.d(TAG,"server inputStream = "+ Arrays.toString(byteArray));
                if (length == -1) {
//                    serverCloseSocket();
                    break;
                } else {
                    String replyMessage = new String(byteArray,0,length);
                    Log.d(TAG,"服务器收到的消息:"+replyMessage+" "+ socket);
                }
            } catch (Exception e) {
                Log.d(TAG,"server socket error, " + e);
            }
        }
        Log.d(TAG,"ServerThread Closed");
    }


    public void refreshStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(socket.isClosed()){
//                        activity.tv4.setText("isClose==true");
                    }else {
//                        activity.tv4.setText("isClose==false");
                    }
                    if(socket.isInputShutdown()){
//                        activity.tv5.setText("isInputShutdown==true");
                    }else {
//                        activity.tv5.setText("isInputShutdown==false");
                    }
                    if(socket.isOutputShutdown()){
//                        activity.tv6.setText("isOutputShutdown==true");
                    }else {
//                        activity.tv6.setText("isOutputShutdown==false");
                    }
                    if(socket.isConnected()){
//                        activity.tv7.setText("isConnected==true");
                    }else {
//                        activity.tv7.setText("isConnected==false");
                    }
                    if(socket.isBound()){
//                        activity.tv8.setText("isBound==true");
                    }else {
//                        activity.tv8.setText("isBound==false");
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


    public static void serverShutdownOutput() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isOutputShutdown()){
            try {
                Log.d(TAG,"server shutdownOutput");
                socket.shutdownOutput();
            } catch (IOException e) {
                Log.d(TAG,"server shutdownOutput error, " + e);
            }
        } else {
            Log.d(TAG,"server socket.isOutputShutdown() == true");
        }
            }
        }).start();
    }

    public static void serverShutdownInput() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isInputShutdown()){
            try {
                Log.d(TAG,"server shutdownInput");
                socket.shutdownInput();
            } catch (IOException e) {
                Log.d(TAG,"server shutdownInput error, " + e);
            }
        } else {
            Log.d(TAG,"server socket.isInputShutdown() == true");
        }
            }
        }).start();
    }
    public static void serverSendMsg(String serverMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isClosed()) {
            if(serverMsg.length()!=0){
                Log.d(TAG, "服务器发送: " + serverMsg + ", socket:" + socket);
                try {
                    // 使用输出流给客户端发送一条信息
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(serverMsg.getBytes());
                    // 只要涉及管道的都建议刷新一下
//                    outputStream.flush();
                } catch (IOException e) {
                    Log.d(TAG,"server outputStream error, " + e);
                }
            }
        }
            }
        }).start();
    }

    public static void serverCloseSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        if (!socket.isClosed()){
            try {
                Log.d(TAG,"server socket.close");
                socket.close();
            } catch (IOException e) {
                Log.d(TAG,"server close socket error, " + e);
            }
        } else {
            Log.d(TAG,"server socket.isClosed() == true");
        }
            }
        }).start();
    }
}