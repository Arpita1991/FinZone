package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;

/**
 * Created by Apatel9273 on 2/12/2017.
 */

public class PaymentLogAdapter extends RecyclerView.Adapter<PaymentLogAdapter.ViewHolder>{

    List<Market.PaymentLogHistory> paymentLogHistories = new ArrayList<>();
    public PaymentLogAdapter(List<Market.PaymentLogHistory> paymentLogHistories){
        this.paymentLogHistories=paymentLogHistories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView paidDate,amount,loanDate,expirationDate;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            paidDate = (TextView) itemView.findViewById(R.id.paidDate);
            amount = (TextView) itemView.findViewById(R.id.amount);
            loanDate = (TextView) itemView.findViewById(R.id.loanDate);
            expirationDate = (TextView) itemView.findViewById(R.id.expirationDate);
        }
    }

    @Override
    public PaymentLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentlog_history_list,parent,false);
        PaymentLogAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentLogAdapter.ViewHolder holder, final int position) {
        holder.amount.setText("$"+paymentLogHistories.get(position).amount);
        holder.loanDate.setText(paymentLogHistories.get(position).loanDate);
        holder.paidDate.setText(paymentLogHistories.get(position).paidDate);
        holder.expirationDate.setText(paymentLogHistories.get(position).expirationDate);
    }

    @Override
    public int getItemCount() {
        return paymentLogHistories.size();
    }

}
