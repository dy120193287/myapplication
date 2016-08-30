package com.grabapp.myapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.activity.MainActivity;
import com.grabapp.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dy on 18/8/16.
 */


public class AddStockDialogActivity extends AppCompatActivity {
    @BindView(R.id.editText)TextView editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_dialog);
        ButterKnife.bind(this);
        initViews();

    }
    private void initViews(){

    }

    private void addStock(String stcok){
        List<String> strings = FunctionManager.stringToListWOhsi(FunctionManager.SharedPreferencesGet(getApplicationContext(),"StockKey"));
        strings.add("rt_hk"+stcok);
        FunctionManager.SharedPreferencesSave(this,"StockKey",FunctionManager.stocksKeyStringListToString(strings));
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btConfirm,R.id.btCannel})
    void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btConfirm:
                if (!editText.getText().toString().equals("")){
                    if (editText.getText().toString().length()==5){
                        addStock(editText.getText().toString());
                    }else if (editText.getText().toString().length()==4){
                        addStock("0"+editText.getText().toString());
                    }else if (editText.getText().toString().length()==3){
                        addStock("00"+editText.getText().toString());
                    }else if (editText.getText().toString().length()==2){
                        addStock("000"+editText.getText().toString());
                    }else if (editText.getText().toString().length()==1){
                        addStock("0000"+editText.getText().toString());
                    }

                }

                break;
            case R.id.btCannel:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }


}
