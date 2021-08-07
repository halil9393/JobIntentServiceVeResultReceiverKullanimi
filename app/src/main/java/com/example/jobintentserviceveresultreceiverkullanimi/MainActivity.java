package com.example.jobintentserviceveresultreceiverkullanimi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    TextView textSayac;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSayac = findViewById(R.id.textSayac);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void StartMyJobIntentService(View view) {

        //Servisden veri alabilmek için ResultReceiver kullanıldı.
        //BroadcastReceiver da kullanılabilirdi, örnek için bakınız= Github halil9393/IntentServiceVeBroadcastReceiverKullanimi repository

        MyResultReceiver myResultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(this, MyJobIntentService.class);
        intent.putExtra("receiver", myResultReceiver);
        MyJobIntentService.enqueueWork(this, intent);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void StopMyJobIntentService(View view) {
        MyJobIntentService.izin = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {//Worker Thread
            super.onReceiveResult(resultCode, resultData);

            Log.i("tag_flow", "MainActivity.MyResultReceiver onReceiveResult çalıştı ");
            Log.i("tag_receive", "ResultCode:" + resultCode + " ResultData:" + resultData.get("process"));

            // UI güncellemek için handler kullanıldı...Main thread de çalışmadığı için UI doğrudan güncellenemez
            handler.post(() -> {
                Toast.makeText(MainActivity.this, "" + resultData.get("process"), Toast.LENGTH_SHORT).show();
                textSayac.setText("" + resultData.get("process"));
            });

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}