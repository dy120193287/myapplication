package com.grabapp.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.R;
import com.grabapp.myapplication.util.RequestListener;
import com.grabapp.myapplication.service.Service;
import com.grabapp.myapplication.model.Stock;
import com.grabapp.myapplication.adapter.Adapter;
import com.grabapp.myapplication.dialog.AddStockDialogActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tvHsi)TextView tvHsi;
    @BindView(R.id.tvDate)TextView tvDate;
    @BindView(R.id.listView)ListView listView;



    MenuItem mPlay,madd;
    Boolean isRunning;
    Handler handler = new Handler( );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        initViews();



    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isRunning){
            this.isRunning=false;
            handler.removeCallbacks(runnable);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_play_arrow_black_48dp));
            Intent intent = new Intent(MainActivity.this,Service.class);
            startService(intent);
        }
        Log.d("d","onStop event");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mPlay  = menu.findItem(R.id.mPlay);
        madd = menu.findItem(R.id.madd);

        return true;
    }

    private void initViews(){
        isRunning = false;

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.mPlay) {
                    mPlayButton(isRunning);
                } else if (menuItemId == R.id.madd) {
                    Intent intent = new Intent(MainActivity.this,AddStockDialogActivity.class);
                     startActivity(intent);
                }
                return true;
            }
        });



//        FunctionManager.SharedPreferencesSave(getApplicationContext(),"StockKey", "rt_hk08090,rt_hk01776,rt_hk06886,rt_hk02382,rt_hk02018");
//        FunctionManager.SharedPreferencesSave(getApplicationContext(),"NotifationKey", "rt_hk06886,16,10!rt_hk01776,null,10");

        FunctionManager.querySinaStocks(getApplicationContext(), FunctionManager.stringToList(FunctionManager.SharedPreferencesGet(getApplicationContext(),"StockKey")), new RequestListener() {
            @Override
            public void onSuccess(Object object) {
                updateStockListView((List<Stock>)object);
                Date date = new Date();
                tvDate.setText((new SimpleDateFormat("h:mm:ss a").format(date)));
            }

            @Override
            public void onError(Object object) {

            }
        });


    }

    private Runnable runnable = new Runnable( ) {
        public void run ( ) {
            FunctionManager.querySinaStocks(getApplicationContext(), FunctionManager.stringToList(FunctionManager.SharedPreferencesGet(getApplicationContext(),"StockKey")), new RequestListener() {
                @Override
                public void onSuccess(Object object) {
                    updateStockListView((List<Stock>)object);
                    Date date = new Date();
                    tvDate.setText((new SimpleDateFormat("h:mm:ss a").format(date)));
                }

                @Override
                public void onError(Object object) {

                }
            });
            handler.postDelayed(this,10000);


        }
    };

    private void mPlayButton(Boolean isRunning){
        if (isRunning){
            Intent intent = new Intent(MainActivity.this,Service.class);
            stopService(intent);
            this.isRunning=false;
            handler.removeCallbacks(runnable);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_play_arrow_black_48dp));
        }else {
            this.isRunning=true;
            handler.postDelayed(runnable,10000);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_stop_black_48dp));

        }
    }


    public void  updateStockListView(final List<Stock> stocks){
        if (stocks.get(0).getChg().indexOf("+")!=-1){
            tvHsi.setTextColor(Color.parseColor("#FF056900"));
        }else {
            tvHsi.setTextColor(Color.parseColor("#FF940000"));
        }
        tvHsi.setText("HSI "+stocks.get(0).getNow()+" "+stocks.get(0).getChg()+"("+stocks.get(0).getChgP()+")");
        stocks.remove(0);
        FunctionManager.checkHighLow(getApplicationContext(),stocks,FunctionManager.SharedPreferencesGet(getApplicationContext(),"NotifationKey"));



        Adapter adapter = new Adapter(MainActivity.this,stocks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, StockDetailActivity.class);
                Gson gson = new Gson();
                String stock = gson.toJson(stocks.get(position));
                intent.putExtra("stock", stock);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
