package com.example.strive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Random; //Used for random number generation

import androidx.annotation.Nullable;

public class bluetoothcomm extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    //@Override
                    final Random rand = new Random();
                    public void run() {
                        while (true) {

                            int sensor_data[]={rand.nextInt(100),rand.nextInt(100),rand.nextInt(100),rand.nextInt(100)};
                            Log.e("Bluetooth-dummy-Service", String.valueOf(sensor_data[0]+" "+sensor_data[0]+" "+sensor_data[0]+" "+sensor_data[0]));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
