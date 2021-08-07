package com.example.jobintentserviceveresultreceiverkullanimi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * Uzun sürecek işlemler için kullanılabilir, WorkerThread de çalışıyor, Async olarak.
 * AndroidMaifest dosyasında özel bir izin gerekiyor -> android.permission.BIND_JOB_SERVICE  !!!
 *
 * Avantajı şu ki; arka plan işlemi tamamlanmadan iptal edilirse, öldürülürse;
 * bir süre sonra kendisi arka planda baştan otomatik olarak başlayıp görevini bitiriyor.
 *
 *      Örnek kullanım: Arka planda internetten resim indirilecekse/yüklenecekse, yarıda kalma durumu olursa bile,
 *      daha sonra başlayıp o işi tamamlayacaktır.
 *
 * */


public class MyJobIntentService extends JobIntentService {

    public static boolean izin = true;  // Not: default olarak false verilirse, IntentService gibi çalışıyor;
                                        // Yani: işlem kill edilirse, daha sonra tekrar başlatılıyor ama izin=false
                                        // olduğu için doğrudan destroy ediyor kendisini.

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static void enqueueWork(Context context, Intent intent) { // Kenidimiz ekledik, işlemi başka classdan başlatabilmek için
        izin = true;
        enqueueWork(context,MyJobIntentService.class,123,intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate() {// Main Thread

        Log.i("tag_flow","onCreate "+Thread.currentThread().getName()+" thread üzerinden cağrıldı");
        super.onCreate();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onHandleWork(@NonNull Intent intent) {// Worker Thread
        Log.i("tag_flow","onHandleWork "+Thread.currentThread().getName()+" thread üzerinden cağrıldı");


        for (int i = 0; i < 100; i++) {
            Log.i("tag_process","process:"+i);
            if(!izin) return; // İstenildiğinde işlemi durdurmak için yöntem.. İzin kapatılırsa, otomatik onDestroy çalıştırıyor

            ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
            Bundle bundle = new Bundle();
            bundle.putInt("process",i);
            resultReceiver.send(15,bundle);

            SystemClock.sleep(2000);
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDestroy() { // Main Thread
        Log.i("tag_flow","onDestroy "+Thread.currentThread().getName()+" thread üzerinden cağrıldı");
        super.onDestroy();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onStopCurrentWork() {
        Log.i("tag_flow","onStopCurrentWork "+Thread.currentThread().getName()+" thread üzerinden cağrıldı");
        return super.onStopCurrentWork();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}

