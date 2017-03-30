package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

/**
 * Created by Apatel9273 on 3/2/2017.
 */

public class NotifyFriendAdapter extends BaseAdapter {
    List<ServerConnect.NotificationFriends> notificationFriends=new ArrayList<>();
    public  NotifyFriendAdapter(List<ServerConnect.NotificationFriends> notificationFriendsList){
        this.notificationFriends=notificationFriendsList;
    }
    @Override
    public int getCount() {

        return notificationFriends.size();

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
        view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notify_friends,null);
        ((TextView)view.findViewById(R.id.name_friend)).setText(notificationFriends.get(i).name);
        final ServerConnect serverConnect=new ServerConnect();
        ((Button)view.findViewById(R.id.accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverConnect.requestNotify(new Utilites().sharePref(view.getContext()),notificationFriends.get(i).userid,true);
                notificationFriends.remove(i);
                notifyDataSetChanged();
            }
        });
        ((Button)view.findViewById(R.id.decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverConnect.requestNotify(new Utilites().sharePref(view.getContext()),notificationFriends.get(i).userid,false);
                notificationFriends.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
