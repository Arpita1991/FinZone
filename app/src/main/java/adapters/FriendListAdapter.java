package adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nox.finzone.ChatActivity;
import nox.finzone.Fragments.FriendListFragment;
import nox.finzone.HomeActivity;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 3/2/2017.
 */

public class FriendListAdapter extends BaseAdapter {
    ServerConnect serverConnect=new ServerConnect();
    List<ServerConnect.Friend> friendList;
    public FriendListAdapter(List<ServerConnect.Friend> friendList){
        this.friendList=friendList;
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_friend_list,null);
        ((TextView)view.findViewById(R.id.friend_name)).setText(friendList.get(i).name);
        ((ImageView)view.findViewById(R.id.message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("FriendID",friendList.get(i).id);

                view.getContext().startActivity(intent);
            }
        });
        ((ImageButton)view.findViewById(R.id.unfriend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverConnect.requestNotify(new Utilites().sharePref(view.getContext()),friendList.get(i).id,false);
                friendList.remove(i);
                notifyDataSetChanged();
            }
        });
        Bitmap bitmap= serverConnect.fetchImage(serverConnect.FETCH_IMAGE_URL+friendList.get(i).email);
        if(bitmap!=null) ((ImageView)view.findViewById(R.id.friend_image)).setImageBitmap(bitmap);
        else ((ImageView)view.findViewById(R.id.friend_image)).setImageDrawable(view.getResources().getDrawable(R.drawable.ic_person));

        return view;
    }
}
