package nox.finzone;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import CustomDialogs.StockDialogs;

public class StockDetailActivity extends AppCompatActivity {
    ImageView image;
    String symbol,stockprice;
    TextView CName,Price,change,dayLow,dayHigh,yearLow,yearHigh,avgDailVol,volume,mark_cap;
    Utilites utilites = new Utilites();
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        symbol = getIntent().getStringExtra("symbol");

        Market market = new Market(symbol);
        final HashMap<String,String> stockInfo = market.getQuoteInfo();
        CName = (TextView) findViewById(R.id.companyName);
        Price = (TextView) findViewById(R.id.Price);
        change=(TextView)findViewById(R.id.Change);
        ImageView changeIcon=(ImageView)findViewById(R.id.changeIcon);
        dayLow=(TextView)findViewById(R.id.day_low);
        dayHigh=(TextView)findViewById(R.id.day_high);
        yearHigh=(TextView)findViewById(R.id.year_high);
        yearLow=(TextView)findViewById(R.id.year_low);
        avgDailVol=(TextView)findViewById(R.id.avg_daily_vol);
        volume=(TextView)findViewById(R.id.volume);
        mark_cap=(TextView)findViewById(R.id.mark_cap);
        image = (ImageView) findViewById(R.id.chartView);
        if(stockInfo!=null){
            stockprice = stockInfo.get("LastTradePriceOnly");
            CName.setText(stockInfo.get("Name"));
            Price.setText("$"+stockInfo.get("LastTradePriceOnly"));
            if(changeColor(stockInfo.get("Change"))){
                changeIcon.setImageDrawable(getDrawable(R.drawable.ic_expand_more_24dp));
                change.setText(stockInfo.get("Change"));
                change.setTextColor(Color.GREEN);
            }
            else{
                changeIcon.setImageDrawable(getDrawable(R.drawable.ic_expand_less_24dp));
                change.setText(stockInfo.get("Change"));
                change.setTextColor(Color.RED);
            }
            TableLayout tableLayout=(TableLayout)findViewById(R.id.table_layout);
            for(int i=0;i<tableLayout.getChildCount();i++){
                TableRow tableRow=(TableRow) tableLayout.getChildAt(i);
                if(i%2==0){
                    tableRow.setBackgroundColor(Color.parseColor("#64B5F6"));
                }
            }



            change.setText(stockInfo.get("Change"));
            dayHigh.setText(stockInfo.get("DaysHigh"));
            dayLow.setText(stockInfo.get("DaysLow"));
            yearHigh.setText(stockInfo.get("YearLow"));
            yearLow.setText(stockInfo.get("YearHigh"));
            mark_cap.setText(stockInfo.get("MarketCapitalization"));
            volume.setText(stockInfo.get("Volume"));
            avgDailVol.setText(stockInfo.get("AverageDailyVolume"));
                    new BitmapGraph().execute(market);


        }
        final Market.StockList stockList=new Market.StockList(stockInfo.get("Name"),stockInfo.get("LastTradePriceOnly"),stockInfo.get("Change"),symbol);

        Button buyShare = (Button) findViewById(R.id.buy);

        Button sellShare = (Button) findViewById(R.id.sell);

        sellShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(StockDetailActivity.this,"Sell").BuySellDialog(stockList);
            }
        });


        buyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StockDialogs(StockDetailActivity.this,"Buy").BuySellDialog(stockList);
            }
        });
    }


    public class BitmapGraph extends AsyncTask <Market,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(Market... markets) {

            return markets[0].getGraph();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
    public boolean changeColor(String change){
        if(change.contains("+"))   return true;
        else return false;


    }


}
