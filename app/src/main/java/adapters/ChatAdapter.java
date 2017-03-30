package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Classes.DBHelper;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 2/13/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    List<ServerConnect.ChatMsgHistory> stockList=new ArrayList<>();
    Context context;
    public ChatAdapter(List<ServerConnect.ChatMsgHistory> stockList,Context context){
        this.stockList=stockList;
        this.context=context;
    }
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view,parent,false); //????understand it later
        ChatAdapter.ViewHolder viewholder = new ViewHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ViewHolder holder, final int position) {
        holder.message.setText(stockList.get(position).message);
        holder.message.setTag(stockList.get(position).messageID);
        if(stockList.get(position).from_userID.equals(new Utilites().sharePref(context))){
            holder.setPosition(true);
        }else holder.setPosition(false);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              //  DBHelper dbHelper = new DBHelper(holder.itemView.getContext());
              //  dbHelper.deleteMessge(Integer.parseInt(stockList.get(position).messageID));
                stockList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, stockList.size());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public ViewHolder(final View itemView) {
            super(itemView);
            message= (TextView) itemView.findViewById(R.id.user_msg);
        }
        public void setPosition(boolean right){

            RelativeLayout.LayoutParams relativeLayout= (RelativeLayout.LayoutParams) message.getLayoutParams();
            if(right) relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            else relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            message.setLayoutParams(relativeLayout);
        }
    }
}
