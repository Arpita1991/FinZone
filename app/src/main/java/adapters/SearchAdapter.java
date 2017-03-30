package adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nox.finzone.R;
import nox.finzone.StockDetailActivity;
import nox.finzone.StockSearchList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by nox on 1/26/2017.
 */
public class SearchAdapter extends BaseAdapter {


    private Activity activity;
    private List<StockSearchList> stockList;
    private static LayoutInflater inflater = null;
    List<StockSearchList> list=new ArrayList<>();

    public SearchAdapter(Activity activity, List<StockSearchList> stockList) {
        this.activity = activity;
        this.stockList = stockList;

        inflater = (LayoutInflater) activity.
                getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_search_adapter, null);
        TextView textView = (TextView) view.findViewById(R.id.searchitem);
        textView.setText(list.get(i).name);
        final String symbol=list.get(i).symbol;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),StockDetailActivity.class);
                intent.putExtra("symbol",symbol+".TO");
                activity.startActivity(intent);
            }
        });
        return view;
    }

    public void getFilter(String searchData){
        searchData = searchData.toLowerCase();
        list.clear();
        if(searchData.length() > 0){
            for(StockSearchList stocks:stockList){
                if(stocks.name.contains(searchData)){
                    list.add(stocks);
                }
            }
        }
        notifyDataSetChanged();
    }

}
