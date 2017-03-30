package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 2/23/2017.
 */

public  class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder>{

    public LayoutInflater inflater=null;
    List<Market.LoanHistory> loanHistoryList;
    public LoanAdapter(List<Market.LoanHistory> loanHistoryList){
        this.loanHistoryList=loanHistoryList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView amount,time,daysleft,expiry,interest;
        public Button payloan;
        public ViewHolder(View view) {
            super(view);
            amount = (TextView) view.findViewById(R.id.loan_amount);
            daysleft = (TextView) view.findViewById(R.id.loan_days_left);
            expiry = (TextView) view.findViewById(R.id.loan_expiry);
            time = (TextView) view.findViewById(R.id.loan_takendate);
            interest = (TextView) view.findViewById(R.id.loan_rate);
            payloan = (Button) view.findViewById(R.id.pay_loan);
        }
    }

    @Override
    public LoanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.loan_cards,parent,false); //????understand it later
        LoanAdapter.ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(LoanAdapter.ViewHolder holder, final int position) {
        holder.amount.setText("Amount :" +loanHistoryList.get(position).amount);
        holder.daysleft.setText("Downpayment :"+loanHistoryList.get(position).daysLeft);
        holder.expiry.setText("Expiry: "+loanHistoryList.get(position).expiry);
        holder.time.setText("Taken Date: "+ loanHistoryList.get(position).time);
        holder.interest.setText("Interest :" + loanHistoryList.get(position).interest);
        holder.payloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Market market=new Market();
                boolean value=market.payLoan(new Utilites().sharePref(view.getContext()),loanHistoryList.get(position).loanID,
                        loanHistoryList.get(position).amount);
                if(value){
                    loanHistoryList.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(), "Not Enough Money in account to Pay loan",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return loanHistoryList.size();
    }


}