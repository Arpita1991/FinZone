package adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import CustomDialogs.StockDialogs;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.StockDetailActivity;

/**
 * Created by Apatel9273 on 2/22/2017.
 */

public class ForexAdapter extends RecyclerView.Adapter<ForexAdapter.ViewHolder>{

    private List<Market.ForexList> forexLists;
    public ForexAdapter(List<Market.ForexList> forexLists){
        this.forexLists=forexLists;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView price,title,change;
        public ImageView chartImage;
        public LineChart lineChart;
        Button details,buy,sell;
        public ViewHolder(View view) {
            super(view);
         //   details=(Button)view.findViewById(R.id.forex_details);
           // buy=(Button)view.findViewById(R.id.forex_buy);
         //   sell=(Button)view.findViewById(R.id.forex_sell);
            price= (TextView) view.findViewById(R.id.forex_price);
            title= (TextView) view.findViewById(R.id.forex_title);
            change= (TextView) view.findViewById(R.id.forex_change);

            lineChart=(LineChart)view.findViewById(R.id.chart);
            //  List<L> entries=new ArrayList<PieEntry>();

            //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //


        }
    }

    @Override
    public ForexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forex_cards,parent,false);
        ForexAdapter.ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForexAdapter.ViewHolder holder, final int position) {
        holder.title.setText(forexLists.get(position).title);
        holder.price.setText(forexLists.get(position).price);
        holder.change.setText(forexLists.get(position).change);

        if(changeColor(forexLists.get(position).change)){
            holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.design_layout_5));
            holder.title.setTextColor(holder.itemView.getResources().getColor(R.color.forexGreen));
            holder.price.setTextColor(holder.itemView.getResources().getColor(R.color.forexGreen));
            holder.change.setTextColor(holder.itemView.getResources().getColor(R.color.forexGreen));
            lineChartStyle(forexLists.get(position).symbol,holder.lineChart,true);
        }
        else{
            holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.design_layout_4));
            holder.title.setTextColor(holder.itemView.getResources().getColor(R.color.redColor));
            holder.price.setTextColor(holder.itemView.getResources().getColor(R.color.redColor));
            holder.change.setTextColor(holder.itemView.getResources().getColor(R.color.redColor));
            lineChartStyle(forexLists.get(position).symbol,holder.lineChart,false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(), StockDetailActivity.class);
                intent.putExtra("symbol",forexLists.get(position).symbol);
                view.getContext().startActivity(intent);
            }
        });

       /* holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Buy").BuySellDialog(forexLists.get(position));
            }
        });
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(view.getContext(),"Sell").BuySellDialog(forexLists.get(position));
            }
        });*/
    }
    public boolean changeColor(String change){
        if(change.contains("+"))   return true;
        else return false;

    }
    public void lineChartStyle(String symbol,LineChart lineChart,boolean up){
        Market market= null;
        try {
            market = new Market(URLEncoder.encode(symbol,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArrayList<Entry> entries=new ArrayList<>();
        entries=market.getChartData("1d");
        LineDataSet lineDataSet=new LineDataSet(entries,"");
        LineData data = new LineData(lineDataSet);
        lineDataSet.setDrawFilled(true);

        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineChart.setData(data);
        lineChart.animateX(2000);
        Description description=new Description();
        description.setText("");
        lineChart.setDescription(description);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setScaleX(1f);
        lineChart.setScaleY(1f);
        lineChart.setDrawBorders(false);
        lineDataSet.setDrawValues(false);
        lineChart.getXAxis().setDrawLabels(false);

        if(up){
            lineDataSet.setColor(Color.GREEN);

        }else{
            lineDataSet.setColor(Color.RED);

    }


    }
    @Override
    public int getItemCount() {
        return forexLists.size();
    }

}
