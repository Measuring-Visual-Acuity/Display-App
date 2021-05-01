package com.example.graphviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    int SERVER_PORT = 5004;
    String SERVER_IP = "";
    Thread thread1 = null;
    private PrintWriter output;
    private BufferedReader input;
    Socket socket;
    Button socketBtn,openSocket;

    DataInputStream dis;
    ImageView imageView;
    private TextView dpi;
    private int deviceDpi;
    private float ppi;
    private float mxdpi;
    private float widthInPixels;
    private float widthInInches;
    EditText inputIpaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getSupportActionBar().hide();
        deviceDpi = getResources().getDisplayMetrics().densityDpi;
        ppi = getResources().getDisplayMetrics().density;
        widthInPixels = getResources().getDisplayMetrics().widthPixels;
        mxdpi = getResources().getDisplayMetrics().xdpi;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        widthInInches = widthInPixels/mxdpi;

        openSocket = findViewById(R.id.openConnection);
        imageView = findViewById(R.id.imageViews);
        inputIpaddress = findViewById(R.id.inputIpaddress);
        //dpi.setText(String.valueOf(deviceDpi));

        openSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SERVER_IP = inputIpaddress.getText().toString().trim();
                thread1 = new Thread(new Thread1());
                thread1.start();
            }
        });
//        socketBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                output.write("bc");
//                socket.isClosed();
//            }
//        });
    }

    class Thread1 implements Runnable
    {
        @Override
        public void run() {
            try
            {
                socket = new Socket(SERVER_IP,SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                dis = new DataInputStream(socket.getInputStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("isConnected", "Connection Successfull.");
                        Toast.makeText(MainActivity.this, "Connected Successfully.",
                                Toast.LENGTH_SHORT).show();
                        openSocket.setVisibility(View.INVISIBLE);
                        inputIpaddress.setVisibility(View.INVISIBLE);
                        //dpi.setVisibility(View.INVISIBLE);
                    }
                });
                new Thread(new Thread2()).start();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
