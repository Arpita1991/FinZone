package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import CustomDialogs.StockDialogs;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.StockDetailActivity;
import nox.finzone.Utilites;

/**
 * Created by nox on 2/9/2017.
 */
public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.ViewHolder> {
    private  List<Market.CommodityList> commodityLists;

    public CommodityAdapter(List<Market.CommodityList> commodityLists){
        this.commodityLists=commodityLists;

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price,title,change;
        public ImageView chartImage;
        Button details,buy,sell;
        public ViewHolder(View view) {
            super(view);
         //   details=(Button)view.findViewById(R.id.commodity_details);
            buy=(Button)view.findViewById(R.id.commodity_buy);
            sell=(Button)view.findViewById(R.id.commodity_sell);
            price= (TextView) view.findViewById(R.id.commodity_price);
            title= (TextView) view.findViewById(R.id.commodity_title);
            change= (TextView) view.findViewById(R.id.commodity_change);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_card,parent,false);
        CommodityAdapter.ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(commodityLists.get(position).title);
        holder.price.setText(commodityLists.get(position).price);
        holder.change.setText(commodityLists.get(position).change);

//        holder.chartImage.setImageBitmap(new Market(commodityLists.get(position).symbol).getGraph());
        setAnimation(holder.itemView.getContext(),holder.itemView);
        /*holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(), StockDetailActivity.class);
                intent.putExtra("symbol",commodityLists.get(position).symbol);
                view.getContext().startActivity(intent);
            }
        });*/

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Buy").BuySellDialog(commodityLists.get(position));
            }
        });
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Sell").BuySellDialog(commodityLists.get(position));
            }
        });
    }

    public void setAnimation(Context context,View animateView){
        Animation animationIn= AnimationUtils.makeInAnimation(context,true);
        Animation animationOut=AnimationUtils.makeOutAnimation(context,true);
        animateView.setAnimation(animationIn);


    }
    @Override
    public int getItemCount() {
        return commodityLists.size();
    }
}
