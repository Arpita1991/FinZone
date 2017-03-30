package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nox.finzone.FriendProfileActivity;
import nox.finzone.HomeActivity;
import nox.finzone.R;
import nox.finzone.ServerConnect;

/**
 * Created by Apatel9273 on 2/25/2017.
 */

public class FriendSearchAdapter extends BaseAdapter {

    public List<ServerConnect.Friend> friendList = new ArrayList<>();
    Context context;
    public FriendSearchAdapter(Context context){
        this.context=context;
    }


    @Override
    public int getCount() {
        return friendList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view =LayoutInflater.from(context).inflate(R.layout.friend_search,null);
        TextView name = (TextView) view.findViewById(R.id.frd_name);
        ImageView profileImage=(ImageView)view.findViewById(R.id.profile_image);
        name.setText(friendList.get(i).name);
        ServerConnect serverConnect=new ServerConnect();
        Bitmap bitmap= serverConnect.fetchImage(serverConnect.FETCH_IMAGE_URL+friendList.get(i).email);
        if(bitmap!=null) profileImage.setImageBitmap(bitmap);
        else profileImage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_person));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FriendProfileActivity.class);
                intent.putExtra("friendID",friendList.get(i).id);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }
    public void filter(List<ServerConnect.Friend> friendList){
        this.friendList=friendList;
        notifyDataSetChanged();
    }
}
