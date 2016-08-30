package com.grabapp.myapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.R;
import com.grabapp.myapplication.model.Stock;
import com.grabapp.myapplication.activity.StockDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dy on 27/8/16.
 */

public class AddNotificationDialogActivity extends AppCompatActivity {


    @BindView(R.id.tvStock)TextView tvStock;
    @BindView(R.id.etHigh)EditText etHigh;
    @BindView(R.id.etLow)EditText etLow;

    Stock stock;
    List<String> NotifationStockList = new ArrayList<>();
    Boolean isexist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification_dialog);
        ButterKnife.bind(this);
        initViews();

    }
    private void initViews(){
        Gson gson = new Gson();
        stock = gson.fromJson(getIntent().getStringExtra("stock"), Stock.class);
        tvStock.setText(stock.getNameCn()+" "+stock.getNumber());
        NotifationStockList = FunctionManager.notifationKeyToStringList(FunctionManager.SharedPreferencesGet(getApplicationContext(),"NotifationKey")) ;
        isexist = false;
        for (int i =0;i<NotifationStockList.size();i++){

            String[] notifationStringB = NotifationStockList.get(i).split(",");
            if (notifationStringB[0].equals(stock.getNumber())){
                isexist = true;
                if (!notifationStringB[1].equals("null")){
                    etHigh.setText(notifationStringB[1]);
                }
                if (!notifationStringB[2].equals("null")){
                    etLow.setText(notifationStringB[2]);
                }
            }
        }

    }

    @OnClick({R.id.btConfirm,R.id.btCannel})
    void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btConfirm:
                if (!FunctionManager.isEmpty(etHigh)&&!FunctionManager.isEmpty(etLow)){
                    if (isexist){
                        for (int i =0;i<NotifationStockList.size();i++) {
                            String[] notifationStringB = NotifationStockList.get(i).split(",");
                            if (notifationStringB[0].equals(stock.getNumber())) {
                                NotifationStockList.set(i,stock.getNumber()+","+etHigh.getText().toString()+","+etLow.getText().toString());

                            }
                        }

                    }else {
                        NotifationStockList.add(stock.getNumber()+","+etHigh.getText().toString()+","+etLow.getText().toString());
                    }

                    FunctionManager.SharedPreferencesSave(AddNotificationDialogActivity.this,"NotifationKey",FunctionManager.notifationKeyStringListToString(NotifationStockList));
                    Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                    Gson gson = new Gson();
                    String stStock = gson.toJson(stock);
                    intent.putExtra("stock", stStock);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else if (FunctionManager.isEmpty(etHigh)&&FunctionManager.isEmpty(etLow)){
                    if (!isexist){
                        Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                        Gson gson = new Gson();
                        String stStock = gson.toJson(stock);
                        intent.putExtra("stock", stStock);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }else {
                        for (int i =0;i<NotifationStockList.size();i++) {
                            String[] notifationStringB = NotifationStockList.get(i).split(",");
                            if (notifationStringB[0].equals(stock.getNumber())) {
                                NotifationStockList.remove(i);

                            }
                        }
                        FunctionManager.SharedPreferencesSave(AddNotificationDialogActivity.this,"NotifationKey",FunctionManager.notifationKeyStringListToString(NotifationStockList));
                        Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                        Gson gson = new Gson();
                        String stStock = gson.toJson(stock);
                        intent.putExtra("stock", stStock);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                    }

                }else if (!FunctionManager.isEmpty(etHigh)){
                    if (isexist){
                        for (int i =0;i<NotifationStockList.size();i++) {
                            String[] notifationStringB = NotifationStockList.get(i).split(",");
                            if (notifationStringB[0].equals(stock.getNumber())) {
                                NotifationStockList.set(i,stock.getNumber()+","+etHigh.getText().toString()+",null");

                            }
                        }

                    }else {
                        NotifationStockList.add(stock.getNumber()+","+etHigh.getText().toString()+",null");
                    }

                    FunctionManager.SharedPreferencesSave(AddNotificationDialogActivity.this,"NotifationKey",FunctionManager.notifationKeyStringListToString(NotifationStockList));
                    Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                    Gson gson = new Gson();
                    String stStock = gson.toJson(stock);
                    intent.putExtra("stock", stStock);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else {
                    if (isexist){
                        for (int i =0;i<NotifationStockList.size();i++) {
                            String[] notifationStringB = NotifationStockList.get(i).split(",");
                            if (notifationStringB[0].equals(stock.getNumber())) {
                                NotifationStockList.set(i,stock.getNumber()+",null,"+etLow.getText().toString());

                            }
                        }

                    }else {
                        NotifationStockList.add(stock.getNumber()+",null,"+etLow.getText().toString());
                    }

                    FunctionManager.SharedPreferencesSave(AddNotificationDialogActivity.this,"NotifationKey",FunctionManager.notifationKeyStringListToString(NotifationStockList));
                    Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                    Gson gson = new Gson();
                    String stStock = gson.toJson(stock);
                    intent.putExtra("stock", stStock);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                break;
            case R.id.btCannel:
                Intent intent = new Intent(AddNotificationDialogActivity.this, StockDetailActivity.class);
                Gson gson = new Gson();
                String stStock = gson.toJson(stock);
                intent.putExtra("stock", stStock);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
    }


}
