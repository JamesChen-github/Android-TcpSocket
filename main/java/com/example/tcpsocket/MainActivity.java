package com.example.tcpsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv1;
    TextView tv2;
    TextView tv3;
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
    String TAG = "cjh";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
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
        }
    }

    private void startServer() throws IOException {
        new TcpServer(this).start();
    }

    private void connectNet() throws IOException {
        new ClientThread(this).start();
    }


}

