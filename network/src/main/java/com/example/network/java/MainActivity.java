package com.example.network.java;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.network.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final static String URL = "https://www.baidu.com";
    private final static int CONNECT_TIME_OUT = 100;
    private final static int READ_TIME_OUT = 100;
    private final static int WRITE_TIME_OUT = 100;
    private final static int MSG_OKHTTP = 1001;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_okhttp).setOnClickListener(v -> handleOkHttp());
    }

    private void handleOkHttp() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)// 连接超时
//                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)// 读超时
//                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)// 写超时
                .build();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                // 要想将response显示在UI，要推到主线程
                mHandler.post(() -> handleResponse(response));
            }
        });
    }

    private void handleResponse(Response response) {
        TextView resultTv = findViewById(R.id.tv_result);
        resultTv.setText(response.toString());
    }
}
