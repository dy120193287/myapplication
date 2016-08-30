package com.grabapp.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.google.gson.Gson;
import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.R;
import com.grabapp.myapplication.model.Stock;
import com.grabapp.myapplication.dialog.AddNotificationDialogActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dy on 27/8/16.
 */

public class StockDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tvNameCn) com.ivankocijan.magicviews.views.MagicTextView tvNameCn;
    @BindView(R.id.tvNumber) TextView tvNumber;
    @BindView(R.id.tvNow) TextView tvNow;
    @BindView(R.id.tvChg) TextView tvChg;
    @BindView(R.id.tvtvChgP) TextView tvtvChgP;
    @BindView(R.id.tvVolume) TextView tvVolume;
    @BindView(R.id.tvTurnover) TextView tvTurnover;
    @BindView(R.id.tvDateTIme) TextView tvDateTIme;


    MenuItem mPlay,madd;
    Boolean isRunning;
    Handler handler = new Handler( );
    Stock stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);
        Gson gson = new Gson();
        stock = gson.fromJson(getIntent().getStringExtra("stock"), Stock.class);
        initViews();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isRunning){
            this.isRunning=false;
            handler.removeCallbacks(runnable);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_play_arrow_black_48dp));
        }
        Log.d("d","onStop event");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_stock_detail, menu);
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
                    Intent intent = new Intent(StockDetailActivity.this, AddNotificationDialogActivity.class);
                    Gson gson = new Gson();
                    String stStock = gson.toJson(stock);
                    intent.putExtra("stock", stStock);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                return true;
            }
        });

        if (stock!=null){
            tvNameCn.setText(stock.getNameCn());
            if (stock.getChg().indexOf("+")!=-1){
                tvChg.setTextColor(Color.parseColor("#FF056900"));
                tvtvChgP.setTextColor(Color.parseColor("#FF056900"));
                tvNow.setTextColor(Color.parseColor("#FF056900"));
            }else {
                tvChg.setTextColor(Color.parseColor("#FF940000"));
                tvtvChgP.setTextColor(Color.parseColor("#FF940000"));
                tvNow.setTextColor(Color.parseColor("#FF940000"));

            }
            tvChg.setText(stock.getChg());
            tvDateTIme.setText(stock.getDate()+" "+stock.getTime());
            tvNow.setText(stock.getNow());
            tvNumber.setText(FunctionManager.formatNumber(stock.getNumber()));
            tvTurnover.setText(stock.getTurnover());
            tvtvChgP.setText(stock.getChgP()+"%");
            tvVolume.setText(stock.getVolume());
        }



//


    }

    private Runnable runnable = new Runnable( ) {
        public void run ( ) {

            handler.postDelayed(this,3000);


        }
    };

    private void mPlayButton(Boolean isRunning){
        if (isRunning){

            this.isRunning=false;
            handler.removeCallbacks(runnable);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_play_arrow_black_48dp));
        }else {
            this.isRunning=true;
            handler.postDelayed(runnable,3000);
            mPlay.setIcon(getResources().getDrawable(R.mipmap.ic_stop_black_48dp));

        }
    }






}
