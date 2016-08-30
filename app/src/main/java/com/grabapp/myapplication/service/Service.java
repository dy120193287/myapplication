package com.grabapp.myapplication.service;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.util.RequestListener;
import com.grabapp.myapplication.model.Stock;

import java.util.Date;
import java.util.List;

/**
 * Created by dy on 18/8/16.
 */
public class Service extends android.app.Service {

    private Handler handler = new Handler();

    private Runnable showTime = new Runnable() {
        public void run() {
            List<String> stockList = FunctionManager.notifationKeyToStockList(FunctionManager.SharedPreferencesGet(getApplicationContext(),"NotifationKey")) ;
            FunctionManager.querySinaStocks(getApplicationContext(), stockList, new RequestListener() {
                @Override
                public void onSuccess(Object object) {
                    List<Stock>stocks=((List<Stock>)object);
                    FunctionManager.checkHighLow(getApplicationContext(),stocks,FunctionManager.SharedPreferencesGet(getApplicationContext(),"NotifationKey"));
                }

                @Override
                public void onError(Object object) {

                }
            });
            Log.i("time:", new Date().toString());
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i("服務", "建立");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("服務", "銷毀");
        handler.removeCallbacks(showTime);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("服務", "執行");
        handler.postDelayed(showTime, 60000);

        return super.onStartCommand(intent, flags, startId);
    }



}