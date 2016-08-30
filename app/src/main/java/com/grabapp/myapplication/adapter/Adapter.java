package com.grabapp.myapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.grabapp.myapplication.util.FunctionManager;
import com.grabapp.myapplication.R;
import com.grabapp.myapplication.model.Stock;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dy on 15/8/16.
 */
public class Adapter extends BaseAdapter {

    private Context context;
    private List<Stock> stocks;
    private ProgressDialog progress;
    private LayoutInflater li;
    public Adapter(Context context, List<Stock> stocks) {
        this.context = context;
        this.li = LayoutInflater.from(context);
        this.stocks = stocks;
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int position) {
        return stocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final Stock stock = stocks.get(position);

        if (viewHolder != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = li.inflate(R.layout.adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        initView(viewHolder, stock);





        return convertView;
    }


    void initView(ViewHolder viewHolder,final Stock stock){

        viewHolder.tvNameCn.setText(stock.getNameCn());
        if (stock.getChg().indexOf("+")!=-1){
            viewHolder.tvChg.setTextColor(Color.parseColor("#FF056900"));
            viewHolder.tvtvChgP.setTextColor(Color.parseColor("#FF056900"));
            viewHolder.tvNow.setTextColor(Color.parseColor("#FF056900"));
        }else {
            viewHolder.tvChg.setTextColor(Color.parseColor("#FF940000"));
            viewHolder.tvtvChgP.setTextColor(Color.parseColor("#FF940000"));
            viewHolder.tvNow.setTextColor(Color.parseColor("#FF940000"));

        }
        viewHolder.tvChg.setText(stock.getChg());
        viewHolder.tvNow.setText(stock.getNow());
        viewHolder.tvNumber.setText(FunctionManager.formatNumber(stock.getNumber()));
        viewHolder.tvTurnover.setText(stock.getTurnover());
        viewHolder.tvtvChgP.setText(stock.getChgP()+"%");
        viewHolder.tvVolume.setText(stock.getVolume());
        viewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<String> strings = FunctionManager.stringToListWOhsi(FunctionManager.SharedPreferencesGet(context.getApplicationContext(),"StockKey"));
                for (int i = 0;i<strings.size();i++){
                    if (strings.get(i).contains(stock.getNumber())){
                        strings.remove(i);

                        FunctionManager.SharedPreferencesSave(context,"StockKey",FunctionManager.stocksKeyStringListToString(strings));
                    }
                }

            }
        });
    }


    static class ViewHolder {
        @BindView(R.id.tvNameCn) com.ivankocijan.magicviews.views.MagicTextView tvNameCn;
        @BindView(R.id.tvNumber) TextView tvNumber;
        @BindView(R.id.tvNow) TextView tvNow;
        @BindView(R.id.tvChg) TextView tvChg;
        @BindView(R.id.tvtvChgP) TextView tvtvChgP;
        @BindView(R.id.tvVolume) TextView tvVolume;
        @BindView(R.id.tvTurnover) TextView tvTurnover;
        @BindView(R.id.btDelete) Button btDelete;




        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

