package adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 2/12/2017.
 */

public class TransfermoneyLogAdapter extends RecyclerView.Adapter<TransfermoneyLogAdapter.ViewHolder>{



    List<Market.TransfermoneyLogHistory> transfermoneyLogHistories = new ArrayList<>();
    public TransfermoneyLogAdapter(List<Market.TransfermoneyLogHistory> transfermoneyLogHistories){
        this.transfermoneyLogHistories=transfermoneyLogHistories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView senddate,amount,username;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            senddate = (TextView) itemView.findViewById(R.id.senddate);
            amount = (TextView) itemView.findViewById(R.id.amount);
            username = (TextView) itemView.findViewById(R.id.username);
        }
    }

    @Override
    public TransfermoneyLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transermoneylog_history_list,parent,false);

        TransfermoneyLogAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransfermoneyLogAdapter.ViewHolder holder, final int position) {
        holder.amount.setText("$"+transfermoneyLogHistories.get(position).amount);
        holder.username.setText(transfermoneyLogHistories.get(position).username);
        holder.senddate.setText(transfermoneyLogHistories.get(position).date);
    }

    @Override
    public int getItemCount() {
        return transfermoneyLogHistories.size();
    }

}
