package adapters;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import CustomDialogs.StockDialogs;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.StockDetailActivity;

/**
 * Created by Apatel9273 on 2/22/2017.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{

    public LayoutInflater inflater=null;
    List<Market.StockList> stockLists;
    public StockAdapter(List<Market.StockList> stockLists){
        this.stockLists=stockLists;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView price,title,change,symbol;
        public ImageView changeImage;
        public ImageButton options;
        Button details,buy,sell;
        public View view;
        public ViewHolder(View view) {
            super(view);
            this.view=view;
           // details=(Button)view.findViewById(R.id.stock_details);
            //buy=(Button)view.findViewById(R.id.stock_buy);
           // sell=(Button)view.findViewById(R.id.stock_sell);
            options=(ImageButton)view.findViewById(R.id.options);
            price= (TextView) view.findViewById(R.id.stock_price);
            title= (TextView) view.findViewById(R.id.stock_title);
            symbol= (TextView) view.findViewById(R.id.stock_quote);
            change= (TextView) view.findViewById(R.id.stock_change);
            changeImage= (ImageView) view.findViewById(R.id.change_image);
        }
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_cards,parent,false);
        StockAdapter.ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StockAdapter.ViewHolder holder, final int position) {
        holder.title.setText(stockLists.get(position).title);
        holder.price.setText(stockLists.get(position).price);
        holder.change.setText(stockLists.get(position).change);
        holder.symbol.setText(stockLists.get(position).symbol);

        if(changeColor(stockLists.get(position).change)){
            holder.changeImage.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.ic_up_arrow));
            holder.change.setText(stockLists.get(position).change);
            holder.change.setTextColor(holder.view.getResources().getColor(R.color.profit));
        }
        else{
            holder.changeImage.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.ic_down_arrow));
            holder.change.setText(stockLists.get(position).change);
            holder.change.setTextColor(holder.view.getResources().getColor(R.color.loss));
        }
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(holder.view.getContext(),holder.options);
                popupMenu.inflate(R.menu.stock_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Buy")){
                            new StockDialogs(holder.view.getContext(),"Buy").BuySellDialog(stockLists.get(position));
                        }
                        else if(item.getTitle().equals("Sell")){
                            new StockDialogs(holder.view.getContext(),"Sell").BuySellDialog(stockLists.get(position));
                        }
                        else if(item.getTitle().equals("Details")){
                            Intent intent =new Intent(holder.view.getContext(), StockDetailActivity.class);
                            intent.putExtra("symbol",stockLists.get(position).symbol);
                            holder.view.getContext().startActivity(intent);
                        }
                        return false;
                    }
                });
            }
        });

        ;
      /*  holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(), StockDetailActivity.class);
                intent.putExtra("symbol",stockLists.get(position).symbol);
                view.getContext().startActivity(intent);
            }
        });*/

    /*    holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Buy").BuySellDialog(stockLists.get(position));
            }
        });
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Sell").BuySellDialog(stockLists.get(position));
            }
        });*/
    }
    public boolean changeColor(String change){
        if(change.contains("+"))   return true;
        else return false;

    }
    @Override
    public int getItemCount() {

        return stockLists.size();
    }


}
