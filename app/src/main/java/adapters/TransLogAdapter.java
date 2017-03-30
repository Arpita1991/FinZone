package adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 2/12/2017.
 */

public class    TransLogAdapter extends RecyclerView.Adapter<TransLogAdapter.ViewHolder>{


    List<Market.TransLogHistory> transLogHistoryList = new ArrayList<>();
    public TransLogAdapter(List<Market.TransLogHistory> transLogHistoryList){
        this.transLogHistoryList=transLogHistoryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView buy_sell;
        public TextView companyName,type,price,quantity,date;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            buy_sell = (TextView) itemView.findViewById(R.id.buy_sell);
            companyName = (TextView) itemView.findViewById(R.id.companyName);
            price = (TextView) itemView.findViewById(R.id.bought_price);
            quantity = (TextView) itemView.findViewById(R.id.qty);
            date = (TextView) itemView.findViewById(R.id.date);
            type = (TextView) itemView.findViewById(R.id.trans_type);
        }
    }

    @Override
    public TransLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.translog_history_list,parent,false);
        TransLogAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransLogAdapter.ViewHolder holder, final int position) {
        holder.buy_sell.setText(transLogHistoryList.get(position).type);
        holder.price.setText("$"+transLogHistoryList.get(position).price);
        holder.companyName.setText(transLogHistoryList.get(position).symbol);
        holder.quantity.setText(transLogHistoryList.get(position).quantity);
        holder.date.setText(transLogHistoryList.get(position).DateTime);
       //    holder.type.setText(transLogHistoryList.get(position).shareType);
        //String transType=transLogHistoryList.get(position).shareType;
        /*if(transType.equals("stock")) holder.shareType.setImageResource(R.drawable.ic_increasing_stocks_graphic_of_bars);
        else if(transType.equals("commodity")) holder.shareType.setImageResource(R.drawable.ic_brick_pile);
        else holder.shareType.setImageResource(R.drawable.money);*/
    }

    @Override
    public int getItemCount() {
        return transLogHistoryList.size();
    }

}
