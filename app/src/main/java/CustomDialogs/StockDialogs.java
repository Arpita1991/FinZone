package CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.StockDetailActivity;
import nox.finzone.Utilites;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Apatel9273 on 2/21/2017.
 */

public class StockDialogs{
    Context context;
    String type;

    public StockDialogs(Context context){
        this.context=context;

    }
    public StockDialogs(Context context,String type){
        this.context=context;
        this.type=type;
    }



    public void BuySellDialog(final Object object)
    {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_buy);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout linearLayout= (LinearLayout) dialog.findViewById(R.id.linear_below);
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.right);
        linearLayout.setAnimation(animation);
        Button tradeButton = (Button) dialog.findViewById(R.id.buybtn);
        tradeButton.setText(type);
        FloatingActionButton cancelButton = (FloatingActionButton) dialog.findViewById(R.id.cancelbtn);
        final EditText qty = (EditText) dialog.findViewById(R.id.quantity);

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerConnect serverConnect = new ServerConnect();

                String options=null,cname = null,sprice = null,symbol = null;
                if(object instanceof Market.CommodityList){
                    options = "Commodity";
                    cname=((Market.CommodityList) object).title;
                    sprice=((Market.CommodityList) object).price;
                    symbol=((Market.CommodityList) object).symbol;


                }
                else if(object instanceof  Market.StockList){
                    options = "Stock";
                    cname=((Market.StockList) object).title;
                    sprice=((Market.StockList) object).price;
                    symbol=((Market.StockList) object).symbol;
                }
                else if(object instanceof Market.ForexList){
                    options = "Forex";
                    cname=((Market.ForexList) object).title;
                    sprice=((Market.ForexList) object).price;
                    symbol=((Market.ForexList) object).symbol;
                }

                boolean value = serverConnect.bsstock(new String[]{
                       cname,new Utilites().sharePref(context), qty.getText().toString(),sprice,symbol,type,options
                });

                if(value){
                    dialog.dismiss();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void transferMoneyDialog(final String fuser){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_buy);
        Button tradeButton = (Button) dialog.findViewById(R.id.buybtn);
        tradeButton.setText("Send Money");
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelbtn);
        ((TextView)dialog.findViewById(R.id.lable)).setText("Amount to Transfer");
        final EditText qty = (EditText) dialog.findViewById(R.id.quantity);
        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              boolean value=new ServerConnect().tranferMoney(qty.getText().toString(),new Utilites().sharePref(context),fuser);
                if(value) dialog.dismiss();

            }
        });
        dialog.show();
    }
public void taglineDialog(final TextView m_Text){
    final Dialog dialog = new Dialog(context);
    dialog.setContentView(R.layout.app_buy);

    Button tradeButton = (Button) dialog.findViewById(R.id.buybtn);
    tradeButton.setText("Send Money");
    Button cancelButton = (Button) dialog.findViewById(R.id.cancelbtn);
    tradeButton.setText("UPDATE");
    ((TextView)dialog.findViewById(R.id.lable)).setText("Add Your Description");
    final EditText qty = (EditText) dialog.findViewById(R.id.quantity);
    tradeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(qty.toString().equals("")) m_Text.setText("Your Description");
            else m_Text.setText(qty.toString());
        }
    });
    cancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });
    dialog.show();


}

public void loanTaken(){
    final Dialog dialog=new Dialog(context);
    dialog.setContentView(R.layout.loan_taken_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    ImageView cancelView= (ImageView) dialog.findViewById(R.id.cancel_button);
    cancelView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });
    dialog.show();
}

}


