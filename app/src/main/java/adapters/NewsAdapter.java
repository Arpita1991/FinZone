package adapters;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.ServerConnect;

/**
 * Created by Apatel9273 on 2/4/2017.
 */

public  class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    public LayoutInflater inflater=null;
    List<Market.NewsList> newsLists;
    public NewsAdapter(List<Market.NewsList> newsLists){
        this.newsLists=newsLists;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView newstitle;
        public ImageView newsimage;
        public ViewHolder(View view) {
            super(view);
             newstitle = (TextView) view.findViewById(R.id.newstitle);
             newsimage = (ImageView) view.findViewById(R.id.newsimage);
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.news_cardlist,parent,false); //????understand it later
        NewsAdapter.ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {
            holder.newstitle.setText(newsLists.get(position).title);
            holder.newsimage.setImageBitmap(newsLists.get(position).image);
            holder.newstitle.setLinkTextColor(Color.BLUE);
            holder.newstitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(newsLists.get(position).link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    view.getContext().startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {

        return newsLists.size();
    }


}
