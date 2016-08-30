package com.grabapp.myapplication.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grabapp.myapplication.R;
import com.grabapp.myapplication.activity.MainActivity;
import com.grabapp.myapplication.model.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 12/8/16.
 */
public class FunctionManager {
    static public List<Stock> responseToStocks(String response, List<String> stockList){
        Log.d("responseToStocks",response);
        List<Stock> stocks = new ArrayList<>();
        response = response.replaceAll("\n", "");
        String[] stockA = response.split(";");
        for (int i =0;i<stockA.length;i++){
            if (stockA[i].length()>30){
                Stock stock = new Stock();
                String string = stockA[i].substring((stockA[i].indexOf("=\"")+1),(stockA[i].lastIndexOf("\"")));
                String[] subStockA = string.split(",");
                stock.setNumber(stockList.get(i));
                stock.setNameEng(subStockA[0]);
                stock.setNameCn(subStockA[1]);
                stock.setOpen(subStockA[2]);
                stock.setPrevClose(subStockA[3]);
                stock.setHigh(subStockA[4]);
                stock.setLow(subStockA[5]);
                stock.setNow(subStockA[6]);
                if (subStockA[7].indexOf("-")==-1){
                    stock.setChg("+"+subStockA[7]);
                }else {
                    stock.setChg(subStockA[7]);
                }
                if (subStockA[8].indexOf("-")==-1){
                    stock.setChgP("+"+subStockA[8]);
                }else {
                    stock.setChgP(subStockA[8]);
                }

                stock.setBuy1(subStockA[9]);
                stock.setSell1(subStockA[10]);
                stock.setTurnover(subStockA[11]);
                stock.setVolume(subStockA[12]);
                stock.setUnknown1(subStockA[13]);
                stock.setUnknown2(subStockA[14]);
                stock.setHigh52(subStockA[15]);
                stock.setLow52(subStockA[16]);
                stock.setDate(subStockA[17]);
                stock.setTime(subStockA[18]);
                stocks.add(stock);
            }

        }
        return stocks;
    }

    static public List<String> stringToList(String string){
        List<String> stocks = new ArrayList<>();
        String[] subStockA = string.split(",");
        stocks.add("rt_hkHSI");
        for (int i =0;i<subStockA.length;i++){
            if (!subStockA[i].equals("")){
                stocks.add(subStockA[i]);
            }

        }
        return stocks;
    }
    static public List<String> stringToListWOhsi(String string){
        List<String> stocks = new ArrayList<>();
        String[] subStockA = string.split(",");
        for (int i =0;i<subStockA.length;i++){
            stocks.add(subStockA[i]);
        }
        return stocks;
    }

    static public List<String> notifationKeyToStockList(String string){
        Log.d("d","notifationKeyToStockList");
        Log.d("dd",string);
        List<String> stocks = new ArrayList<>();
        String[] subStockA = string.split("!");
        for (int i =0;i<subStockA.length;i++){
            String[] subStockB = subStockA[i].split(",");
            stocks.add(subStockB[0]);
        }
        return stocks;
    }

    static public void SharedPreferencesSave(Context context,String key, String object){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,object);
        editor.commit();
    }

    static public String SharedPreferencesGet(Context context,String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key ,"");
    }

    static public void querySinaStocks(Context context,final List<String> stringList,final RequestListener listener){
        Log.d("ddd","querySinaStocks");

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://hq.sinajs.cn/list=";
        for (int i=0;i<stringList.size();i++){
            if (i==(stringList.size()-1)){
                url+=stringList.get(i);
            }else {
                url+=stringList.get(i)+",";
            }
        }
        Log.d("querySinaStocks",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ddd",response);
                        listener.onSuccess(FunctionManager.responseToStocks(response,stringList));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request",error.toString());
                        listener.onError(error.toString());
                    }
                });

        queue.add(stringRequest);
    }

    static public void sendNotifation(Context context,int id, String title, String text){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0, notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder nBuilder =new NotificationCompat.Builder(context);
        nBuilder.setSmallIcon(R.mipmap.ic_launcher);
        nBuilder.setContentTitle(title);
        nBuilder.setContentText(text);
        nBuilder.setVibrate(new long[]{100, 100, 100});
        nBuilder.setLights(Color.RED, 1000, 1000);
        nBuilder.setDefaults(Notification.DEFAULT_SOUND);
        nBuilder.setContentIntent(contentIntent);
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notifyMgr.notify(id, nBuilder.build());

    }

    static public void checkHighLow(Context context,List<Stock> stocks,String notifationString){

        for (int i=0;i<stocks.size();i++){

            List<String> notifationStringA = notifationKeyToStringList(notifationString);
            for (int k =0;k<notifationStringA.size();k++){
                String[] notifationStringB = notifationStringA.get(k).split(",");
                if (notifationStringB[0].equals(stocks.get(i).getNumber())){
                    if (!notifationStringB[1].equals("null")&&Double.parseDouble(notifationStringB[1])<Double.parseDouble(stocks.get(i).getNow())){
                        sendNotifation(context,0,formatNumber(stocks.get(i).getNumber())+stocks.get(i).getNameCn(),formatNumber(stocks.get(i).getNumber())+" 現價:"+stocks.get(i).getNow()+", 高於設定價:"+notifationStringB[1]);
                        notifationStringA.remove(k);
                        SharedPreferencesSave(context,"NotifationKey",notifationKeyStringListToString(notifationStringA));
                    }
                    if (!notifationStringB[2].equals("null")&&Double.parseDouble(notifationStringB[2])>Double.parseDouble(stocks.get(i).getNow())){
                        sendNotifation(context,0,formatNumber(stocks.get(i).getNumber())+stocks.get(i).getNameCn(),formatNumber(stocks.get(i).getNumber())+" 現價:"+stocks.get(i).getNow()+", 低於設定價:"+notifationStringB[2]);
                        notifationStringA.remove(k);
                        SharedPreferencesSave(context,"NotifationKey",notifationKeyStringListToString(notifationStringA));
                    }
                }
            }
        }

    }

    static public List<String> notifationKeyToStringList(String notifationString){
        String[] notifationStringA = notifationString.split("!");
        List<String> stringList = new ArrayList<>();
        for (int i=0;i<notifationStringA.length;i++){
            stringList.add(notifationStringA[i]);
        }
        return stringList;
    }

    static public String notifationKeyStringListToString(List<String> notifationStringList){
        String notifationString="";
        for (int i=0;i<notifationStringList.size();i++){
            if (i==(notifationStringList.size()-1)){
                notifationString+=notifationStringList.get(i);
            }else {
                notifationString+=notifationStringList.get(i)+"!";
            }
        }
        Log.d("ListToString",notifationString);
        return notifationString;
    }

    static public String stocksKeyStringListToString(List<String> notifationStringList){
        String notifationString="";
        for (int i=0;i<notifationStringList.size();i++){
            if (i==(notifationStringList.size()-1)){
                notifationString+=notifationStringList.get(i);
            }else {
                notifationString+=notifationStringList.get(i)+",";
            }
        }
        Log.d("ListToString",notifationString);
        return notifationString;
    }

    static public String formatNumber(String oriSting){
        return oriSting.substring(5,10);
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().length() == 0;
    }

    public static boolean isEmpty(EditText et) {
        if (et == null) {
            return true;
        }
        return et.getText().toString().trim().length() == 0;
    }

}
