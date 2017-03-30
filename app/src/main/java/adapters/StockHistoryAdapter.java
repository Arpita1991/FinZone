package adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 2/10/2017.
 */

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.ViewHolder>  {

    public LayoutInflater inflater=null;
    OnItemClickListener onItemClickListener;
    int itemPostion;
    List<Market.StockHistory> stockHistoryList = new ArrayList<>();
    public StockHistoryAdapter(List<Market.StockHistory> stockHistoryList){
        this.stockHistoryList=stockHistoryList;
       // this.lineChart=lineChart;

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price,title,qty,bprice,pl;
        public Button option;
        public ImageView shareType;
        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            shareType=(ImageView)view.findViewById(R.id.share_type);
            option = (Button) view.findViewById(R.id.buy_sell);
            pl = (TextView) view.findViewById(R.id.profit_loss);
            title = (TextView) view.findViewById(R.id.companyName);
            bprice = (TextView) view.findViewById(R.id.bought_price);
            price = (TextView) view.findViewById(R.id.Price);
            qty = (TextView) view.findViewById(R.id.qty);

        }


    }

    @Override
    public StockHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_history_list,parent,false);
        StockHistoryAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StockHistoryAdapter.ViewHolder holder, final int position) {
        if(stockHistoryList.get(position).option.equals("Buy")){
            holder.option.setText("Buy");
        }else{
            holder.option.setText("Sell");
        }
        itemPostion=position;
        String stype=stockHistoryList.get(position).share_type;
        if(stype.equals("stock")) holder.shareType.setImageResource(R.drawable.ic_increasing_stocks_graphic_of_bars);
        else if(stype.equals("commodity")) holder.shareType.setImageResource(R.drawable.ic_commodities);
        else if(stype.equals("forex")) holder.shareType.setImageResource(R.drawable.money);
        holder.pl.setText(stockHistoryList.get(position).profit_loss);
        holder.title.setText(stockHistoryList.get(position).title);
        holder.bprice.setText(stockHistoryList.get(position).bprice);
        holder.price.setText(stockHistoryList.get(position).price);
        holder.qty.setText(stockHistoryList.get(position).qty);
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Market.StockHistory stockHistory=stockHistoryList.get(position);
                Market market = new Market(stockHistory.symbol);
                HashMap<String,String> hashMap=market.getQuoteInfo();
                Double invest=Double.parseDouble(stockHistory.price)*Double.parseDouble(stockHistory.qty);
                Double profit=Double.parseDouble(hashMap.get("LastTradePriceOnly"))- Double.parseDouble(stockHistory.price);
                Double loss;

                if(profit<0) {
                    profit=0.0;
                    loss = profit*Double.parseDouble(stockHistory.qty);
                }else{
                    loss=0.0;
                }
                if(stockHistory.option.equalsIgnoreCase("Sell")){
                    Double temp=profit;
                    profit=loss;
                    loss=temp;
                }
                market.updateStock(stockHistory.stockid,stockHistory.option,new Utilites().sharePref(view.getContext()),
                        String.valueOf(profit), String.valueOf(loss),
                        stockHistory.share_type,stockHistory.price,stockHistory.qty
                        ,stockHistory.title,String.valueOf(invest));
                stockHistoryList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, stockHistoryList.size());
        }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(stockHistoryList.get(position).symbol,stockHistoryList.get(position).days);
            }
        });


    }

    @Override
    public int getItemCount() {
        return stockHistoryList.size();
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener{
        public void onItemClick(String symbol, String days);
    }
}
