package nox.finzone;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nox on 1/24/2017.
 */

public class Market {
    private HashMap<String, String> stockDetails = new HashMap<>();
    private String REQ_URL = " http://query.yahooapis.com/v1/public/yql?";
    private String YQL_SELECT = "q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
    private String env_Str = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json";
    private String CHART_URL = "http://chart.finance.yahoo.com/z?s=";
    private String STOCLLIST_URL="https://finzone-db-arpitapatel.c9users.io/getStockList.php?";
    private String TRANSLOGLIST_URL="https://finzone-db-arpitapatel.c9users.io/getTransList.php?";
    private String chart_prm = "&t=6m&q=l&l=on&z=s";
    private String symbol;
    private String NEWSFEED_URL="https://finzone-db-arpitapatel.c9users.io/NewsFeed.php";
    private String DELTRANS_URL="https://finzone-db-arpitapatel.c9users.io/DelTrans.php?";
    private String GETLOAN_URL="https://finzone-db-arpitapatel.c9users.io/getLoan.php?";
    private String GETLOANLIST_URL="https://finzone-db-arpitapatel.c9users.io/getLoanlist.php?";
    private String PAY_LOAN="https://finzone-db-arpitapatel.c9users.io/payloan.php?";
    private String GETBANKDETAILS_URL="https://finzone-db-arpitapatel.c9users.io/bankdetails.php?";
    private String CHARTURL = "http://chartapi.finance.yahoo.com/instrument/1.0/";
    private String CHARTPARAM = "/chartdata;type=quote";
    private String CHARTRANGE = ";range=";
    private  String PORTFOLIO_URL="https://finzone-db-arpitapatel.c9users.io/getPortfolio.php?";
    private  String BSSTOCK_URL="https://finzone-db-arpitapatel.c9users.io/updatebsstock.php?";
    private String GETTRASMONEYLIST="https://finzone-db-arpitapatel.c9users.io/getTrasfermoneylog.php?";
    private String GETPAYMENTLOG="https://finzone-db-arpitapatel.c9users.io/getPaymentlog.php?";

    public ArrayList<String[]> arrayList=new ArrayList<>();
    public ArrayList<String> forexLists=new ArrayList<>();
    public ArrayList<String> stockLists=new ArrayList<>();

    public Market(){
        Collections.addAll(arrayList,new String[]{"cl=f","ng=f","bz=f"},
                new String[]{"gc=f","si=f","pl=f","hg=f"},
                new String[]{"c=f","s=f","rr=f"},
                new String[]{"lh=f","lc=f","fc=f"},
                new String[]{"cc=f","kc=f","cd=f","sb=f"});
        Collections.addAll(forexLists,"cadusd=x", "cadeur=x", "cadgbp=x", "cadcny=x", "gbpusd=x", "audusd=x", "audjpy=x",
                "nzdusd=x", "eurgbp=x", "eurchf=x", "usdrub=x", "usdphp=x");
        Collections.addAll(stockLists,"RY.to","TD.to","BNS.to","CNR.to","SU.to","BMO.to","TRP.to","ENB.to","BCE.to","MFC.to");
    }
    public Market(String symbol) {
        this.symbol = symbol;
    }



    public HashMap<String, String> getQuoteInfo() {
        try {
            URL url = new URL(REQ_URL + YQL_SELECT + this.symbol + env_Str);
            System.out.println(url.toString());
         //   System.out.println(url);
            //make a single function
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }
            JSONObject jsonObject = new JSONObject(value);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("quote");
          //  System.out.println(jsonObject);

               // stockDetails.put("AverageDailyVolume", (String) jsonObject.get("AverageDailyVolume"));
           //     System.out.println(String.valueOf(jsonObject.get("Change")));
                stockDetails.put("Change", (String) jsonObject.get("Change"));
               // stockDetails.put("DaysLow", (String) jsonObject.get("DaysLow"));
                //stockDetails.put("DaysHigh", (String) jsonObject.get("DaysHigh"));
                stockDetails.put("YearLow", (String) jsonObject.get("YearLow"));
         //       stockDetails.put("YearHigh", (String) jsonObject.get("YearHigh"));
                stockDetails.put("symbol", (String) jsonObject.get("symbol"));
               // stockDetails.put("MarketCapitalization", (String) jsonObject.get("MarketCapitalization"));
                stockDetails.put("LastTradePriceOnly", (String) jsonObject.get("LastTradePriceOnly"));
                stockDetails.put("DaysRange", (String) jsonObject.get("DaysRange"));
                stockDetails.put("Name", (String) jsonObject.get("Name"));
                stockDetails.put("Volume", (String) jsonObject.get("Volume"));
                stockDetails.put("StockExchange", (String) jsonObject.get("StockExchange"));
                return stockDetails;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return null;
    }

    public boolean delTrans(String username,String transID)  {
        URL url = null;
        try {
            url = new URL(DELTRANS_URL+"email="+username+"&transID="+transID);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return false;
            else if(code==201) return  true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean payLoan(String username,String loanID,String amount)
    {
        URL url = null;
        try {
            url = new URL(PAY_LOAN+"username="+username+"&loanID="+loanID+"&amount="+amount);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return false;
            else if(code==201) return  true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean getLoan(String[] values)
    {
        URL url = null;
        try {
            url = new URL(GETLOAN_URL+"username="+values[0]+"&amount="+values[1]+"&rate="+values[2]+"&time="+ URLEncoder.encode(values[3],"utf-8"));
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            System.out.println(GETLOAN_URL+"username="+values[0]+"&amount="+values[1]+"&rate="+values[2]+"&time="+URLEncoder.encode(values[3],"utf-8"));
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return false;
            else if(code==201) return  true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStock(String stockid,String domain,String email,String profit,String loss,
                            String type,String price,String qty,String cname,String invest)
    {
        URL url = null;
        try {
            url = new URL(BSSTOCK_URL+"stockID="+stockid+"&email="+email+"&domain="+domain+"&profit="
                    +profit+"&loss="+loss+"&transtype="+type+"&price="
                    +price+"&quantity="+qty+"&cname="+cname+"&invest="+invest);
            System.out.println("mytest"+url);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return false;
            else if(code==201) return  true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<NewsList> getNewsfeed()
    { List<NewsList> list=new ArrayList<>();
        try {
            URL url = new URL(NEWSFEED_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String StringUrl = jsonObject.getJSONObject("image_url").getString("0");

                ServerConnect serverConnect = new ServerConnect();
                NewsList newsList=new NewsList(jsonObject.getJSONObject("title").getString("0"),
                        serverConnect.fetchImage(StringUrl),
                        jsonObject.getJSONObject("link").getString("0"));
                list.add(newsList);

            }
            return list;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Entry> getChartData(String days)
    {
        ArrayList<Entry> list=new ArrayList<>();
        try {
            URL url = new URL(CHARTURL+URLEncoder.encode(symbol,"utf-8")+CHARTPARAM+CHARTRANGE+days+"/json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }
            value=value.replace("finance_charts_json_callback(","");
            value=value.replace(")","");
            JSONObject jsonObject=new JSONObject(value);
            JSONArray array=jsonObject.getJSONArray("series");
            array.length();
            for(int i=0;i<array.length();i++){
                float x=(float)array.getJSONObject(i).getInt("Timestamp");
                float y=(float)array.getJSONObject(i).getDouble("open");
                list.add(new Entry(x,y));

            }

            return list;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Object> getPortfolioData(String username)
    {
        ArrayList<Object> object=new ArrayList<>();
        List<PieEntry> pieEntries=new ArrayList<>();
        List<BarEntry> barEntries=new ArrayList<>();


        try {
            URL url = new URL(PORTFOLIO_URL+"email="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String stock=jsonObject.getString("stockInvest");
            String bank=jsonObject.getString("amount");
            String commodity=jsonObject.getString("commoditiesInvest");
            String forex=jsonObject.getString("forexInvest");
            String loan=jsonObject.getString("loanamount");
            String numStock=jsonObject.getString("numstock");
            String numCommodity=jsonObject.getString("numcommodity");
            String numForex=jsonObject.getString("numforex");

            pieEntries.add(new PieEntry(Float.parseFloat(stock),"Stock"));
            pieEntries.add(new PieEntry(Float.parseFloat(commodity),"Commodity"));
            pieEntries.add(new PieEntry(Float.parseFloat(forex),"Forex"));
            pieEntries.add(new PieEntry(Float.parseFloat(bank),"Bank"));
            pieEntries.add(new PieEntry(Float.parseFloat(loan),"Loan"));
            barEntries.add(new BarEntry(0,Float.parseFloat(numStock)));
            barEntries.add(new BarEntry(1,Float.parseFloat(numCommodity)));
            barEntries.add(new BarEntry(2,Float.parseFloat(numForex)));

            object.add(pieEntries);
            object.add(barEntries);
            object.add(bank);
            return object;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public List<StockSearchList> getStockList(Context context) {
        List<StockSearchList> stockSearchLists=new ArrayList<StockSearchList>();
        try {

            InputStream raw = context.getAssets().open("quotes.csv");
            BufferedReader is = new BufferedReader(new InputStreamReader(raw, "UTF8"));

           String data;

            while((data = is.readLine()) != null)
            {
                    String[] stockValue  = data.split(",");
                    StockSearchList stockSearchList=new StockSearchList(stockValue[0],stockValue[1]);
                    stockSearchLists.add(stockSearchList);
            }

            return stockSearchLists;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockSearchLists;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Bitmap getGraph() {

        URL url = null;
        try {
            url = new URL(CHART_URL+this.symbol+chart_prm);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();
            Log.i("Height", String.valueOf(httpURLConnection.getResponseCode()));
            InputStream in =  httpURLConnection.getInputStream();

            Bitmap image= BitmapFactory.decodeStream(in);

            //Bitmap image=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight());


            return image;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


public List<Market.StockHistory> getStockHistory(String username,String type) {
    List<StockHistory> list=new ArrayList<>();
    try {
        URL url = new URL(STOCLLIST_URL+"email="+username+"&type="+type);
        System.out.println(url.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.connect();
        InputStream in = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String data, value = "";
        while ((data = bufferedReader.readLine()) != null) {
            value += data;

        }
       // HashMap<String ,String > hashMap=getQuoteInfo();
        System.out.print(value);
        JSONArray jsonArray=new JSONArray(value);
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            Market.StockHistory stockHistoryList=new StockHistory(new String[]{jsonObject.getString("companyName"),
                    jsonObject.getString("price"),
                    jsonObject.getString("qty"),
                    "",
                    //hashMap.get("LastTradePriceOnly"),
                    jsonObject.getString("type"),
                    jsonObject.getString("price"),
            jsonObject.getString("shareType"),jsonObject.getString("days"),jsonObject.getString("compnayTicker"),jsonObject.getString("stockID")});
            list.add(stockHistoryList);
        }
        return list;

    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return list;
}

    public static class StockHistory
    {
        public String title,price,profit_loss,qty,bprice,option,share_type,days,symbol,stockid;

        public StockHistory(String[] array)
        {
            this.title = array[0];
            this.price = array[1];
            this.qty = array[2];
            this.profit_loss = array[3];
            this.option = array[4];
            this.bprice = array[5];
            this.share_type=array[6];
            this.days=array[7];
            this.symbol=array[8];
            this.stockid=array[9];
        }
    }



    public List<Market.TransLogHistory> getTransLogHistory(String username) {
        List<TransLogHistory> list=new ArrayList<>();
        try {
            System.out.println("TransLogHistory");
            URL url = new URL(TRANSLOGLIST_URL+"email="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;

            }
            System.out.println(value);

            JSONArray jsonArray=new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Market.TransLogHistory transLogHistoryList=new TransLogHistory(new String[]{jsonObject.getString("companyName"),
                        jsonObject.getString("shareType"),jsonObject.getString("Type"),jsonObject.getString("price"),jsonObject.getString("quantity"),
                        jsonObject.getString("tansID"),
                        jsonObject.getString("symbol"),
                        jsonObject.getString("DateTime")});
                list.add(transLogHistoryList);
            }
            return list;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class TransLogHistory {

        public String shareType,companyName,type,price,quantity,trasnID,symbol,DateTime;

        public TransLogHistory(String[] array)
        {
            this.companyName = array[0];
            this.shareType = array[1];
            this.type = array[2];
            this.price = array[3];
            this.quantity = array[4];
            this.trasnID=array[5];
            this.symbol=array[6];
            this.DateTime=array[7];
        }
    }

    public List<Market.TransfermoneyLogHistory> getTransfermoneyLogHistory(String username) {
        List<TransfermoneyLogHistory> list=new ArrayList<>();
        try {
            URL url = new URL(GETTRASMONEYLIST+"username="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Market.TransfermoneyLogHistory transfermoneyLogHistory=new TransfermoneyLogHistory(new String[]{
                        jsonObject.getString("userID_to"),
                        jsonObject.getString("createdDate"),
                        jsonObject.getString("amount"),
                        jsonObject.getString("username")
                        });
                list.add(transfermoneyLogHistory);
            }
            return list;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class TransfermoneyLogHistory {

        public String toID,date,amount,username;

        public TransfermoneyLogHistory(String[] array)
        {
            this.toID = array[0];
            this.date = array[1];
            this.amount = array[2];
            this.username = array[3];
        }
    }


    public List<Market.PaymentLogHistory> getPaymentLogHistory(String username) {
        List<PaymentLogHistory> list=new ArrayList<>();
        try {
            URL url = new URL(GETPAYMENTLOG+"email="+username);
            System.out.println(GETPAYMENTLOG+"username="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Market.PaymentLogHistory paymentLogHistory=new PaymentLogHistory(new String[]{
                        jsonObject.getString("amount"),
                        jsonObject.getString("paidDate"),
                        jsonObject.getString("loanDate"),
                        jsonObject.getString("expirationDate")
                });
                list.add(paymentLogHistory);
            }
            return list;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class PaymentLogHistory {

        public String amount,paidDate,loanDate,expirationDate;

        public PaymentLogHistory(String[] array)
        {
            this.amount = array[0];
            this.paidDate = array[1];
            this.loanDate = array[2];
            this.expirationDate = array[3];
        }
    }



    public List<Market.LoanHistory> getLoanHistory(String username) {
        List<LoanHistory> list=new ArrayList<>();
        try {
            URL url = new URL(GETLOANLIST_URL+"username="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Market.LoanHistory loanHistory=new LoanHistory(jsonObject.getString("loanamount"),
                        jsonObject.getString("loanDate"),jsonObject.getString("downpayment"),
                        jsonObject.getString("expirationDate"),jsonObject.getString("interest"),jsonObject.getString("loanID"));
                list.add(loanHistory);
            }
            return list;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public class LoanHistory {
        public String amount,time,expiry,daysLeft,interest,downpayment,loanID;
        public LoanHistory(String amount,String time,String downpayment,String expiry,String interest,
                           String loanID){
            this.amount=amount;
            this.expiry=expiry;
            this.time=time;
            this.interest=interest;
            this.downpayment=downpayment;
            this.loanID=loanID;
        }
    }



    public List<CommodityList> getCommodityInfo(int index){
        List<CommodityList> commodityLists=new ArrayList<>();

        for(int i=0;i<arrayList.get(index).length;i++){
            this.symbol=arrayList.get(index)[i];
            HashMap<String,String> value=this.getQuoteInfo();
            System.out.println(value.toString());
            CommodityList commodityList=new CommodityList(  value.get("Name"),
                    value.get("LastTradePriceOnly"), value.get("Change"),value.get("symbol"));
            commodityLists.add(commodityList);
        }
        return commodityLists;
    }
    public List<ForexList> getForexInfo(){
        List<ForexList> forex=new ArrayList<>();
        for(int i=0;i<forexLists.size();i++){
            this.symbol=forexLists.get(i);
            HashMap<String,String> value=this.getQuoteInfo();
            ForexList forexlist=new ForexList(value.get("Name"),value.get("LastTradePriceOnly"), value.get("Change"),value.get("symbol"));
            forex.add(forexlist);
        }

        return forex;
    }

    public List<StockList> getstockInfo(){
        List<StockList> stock=new ArrayList<>();
        for(int i=0;i<stockLists.size();i++){
            this.symbol=stockLists.get(i);
            HashMap<String,String> value=this.getQuoteInfo();
            StockList stocklist=new StockList(value.get("Name"),
                    value.get("LastTradePriceOnly"), value.get("Change"),value.get("symbol"));
            stock.add(stocklist);
        }
        return stock;
    }


    // refactor classes into one class
    //----------------------------------------------------------------------
    public static class CommodityList {
        public String title,price,change,symbol;
        public CommodityList(String title,String price,String change,String symbol){
            this.title=title;
            this.price=price;
            this.change=change;
            this.symbol=symbol;
        }
    }
    public static class StockList {
        public String title,price,change,symbol;
        public StockList(String title,String price,String change,String symbol){
            this.title=title;
            this.price=price;
            this.change=change;
            this.symbol=symbol;
        }
    }
    public static class ForexList {
        public String title,price,change,symbol;
        public ForexList(String title,String price,String change,String symbol){
            this.title=title;
            this.price=price;
            this.change=change;
            this.symbol=symbol;

        }
    }
    //-----------------------------------------------------


    public class NewsList {
        public String title,link;
        public Bitmap image;

        public NewsList(String title, Bitmap image , String link)
        {
            this.title = title;
            this.link = link;
            this.image = image;
        }
    }

    public String  getBankAmount(String username) {
        List<LoanHistory> list=new ArrayList<>();
        try {
            URL url = new URL(GETBANKDETAILS_URL+"username="+username);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null) {
                value += data;

            }


                JSONObject jsonObject=new JSONObject(value);


            return jsonObject.getString("amount");

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


}

