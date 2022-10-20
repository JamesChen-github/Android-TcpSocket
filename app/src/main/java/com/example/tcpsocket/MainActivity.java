package com.example.tcpsocket;

import static com.example.tcpsocket.ServerThread.serverCloseSocket;
import static com.example.tcpsocket.ServerThread.serverSendMsg;
import static com.example.tcpsocket.ServerThread.serverShutdownInput;
import static com.example.tcpsocket.ServerThread.serverShutdownOutput;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    TextView tv11;
    TextView tv12;
    TextView tv13;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;
    EditText et6;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn10;
    String TAG = "cjh MainActivity";
    Boolean serverStarted = false;
    Boolean serverConnected = false;
    Boolean clientStarted = false;
    MainServer mainServer;
    ClientThread clientThread;
    String str="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv10 = findViewById(R.id.tv10);
        tv11 = findViewById(R.id.tv11);
        tv12 = findViewById(R.id.tv12);
        tv13 = findViewById(R.id.tv13);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn10 = findViewById(R.id.btn10);
        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv2.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv3.setMovementMethod(ScrollingMovementMethod.getInstance());
        et1.setText("127.0.0.1");
        et3.setText("127.0.0.1");
        et2.setText("9999");
        et4.setText("9999");
        et5.setText("hello");
        et6.setText("hi");
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);

        for (int i=0;i<100;i++){
            for(int j=0;j<10;j++){
                str += "12345678901234567890123456789012345678901234567890";
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                try {
                    startServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn2:
                try {
                    connectNet();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn3:
                if(serverConnected) {
                    String serverMsg = et5.getText().toString().trim();
                    serverSendMsg(serverMsg);
                }
                break;
            case R.id.btn4:
                if(serverConnected) {
                    serverCloseSocket();
                }
                break;
            case R.id.btn5:
                if(clientStarted){
                    String clientMsg = et6.getText().toString().trim();
                    clientThread.clientSendMsg(str);
                }
                break;
            case R.id.btn6:
                if(clientStarted) {
                    clientThread.clientCloseSocket();
                }
                break;
            case R.id.btn7:
                if(serverConnected) {
                    serverShutdownInput();
                }
                break;
            case R.id.btn8:
                if(serverConnected) {
                    serverShutdownOutput();
                }
                break;
            case R.id.btn9:
                if(clientStarted) {
                    clientThread.clientShutdownInput();
                }
                break;
            case R.id.btn10:
                if(clientStarted) {
                    clientThread.clientShutdownOutput();
                }
                break;
        }
    }

    private void startServer() throws IOException {
        if (!serverStarted){
            int serverPort;
            try {
                serverPort = Integer.parseInt(et2.getText().toString().trim());
            } catch (RuntimeException e) {
                Log.d(TAG, "server port is not legal, set to 9999." + e);
                tv3.append("\nserver port is not legal, set to 9999." + e);
                et2.setText("9999");
                serverPort = 9999;
            }
            mainServer = new MainServer(this, serverPort);
            mainServer.start();
            serverStarted = true;
        }
    }

    private void connectNet() throws IOException {
        if (!clientStarted){
            String targetIp;
            int targetPort;
            if (et3.getText().toString().trim().equals("")){
                try {
                    et3.setText(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            targetIp = et3.getText().toString().trim();
            try {
                targetPort = Integer.parseInt(et4.getText().toString().trim());
            } catch (RuntimeException e) {
                Log.d(TAG, "target port is not legal, set to 9999." + e);
                tv2.append("\ntarget port is not legal, set to 9999." + e);
                et4.setText("9999");
                targetPort = 9999;
            }
            clientThread = new ClientThread(targetIp, targetPort);
            clientThread.start();
            clientStarted = true;
            serverConnected = true;
            btn2.setText("断开所有网络");
        }else{
            ServerThread.closeSocket();
            clientThread.closeSocket();
            clientStarted = false;
            serverConnected = false;
            btn2.setText("连接网络");
        }
    }
}












