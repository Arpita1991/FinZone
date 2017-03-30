package nox.finzone;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import CustomDialogs.StockDialogs;

public class FriendProfileActivity extends AppCompatActivity {

    TextView name,experience,dob,description,addfrdlabel;
    ImageView profileImage,badgeImage,addFrd,transferMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        final ServerConnect serverConnect=new ServerConnect();
        final String friendID=getIntent().getStringExtra("friendID");
        final ServerConnect.Friend friend= serverConnect.getFriendDetails(new Utilites().sharePref(getApplicationContext()),friendID);
        //getActionBar().setTitle(friend.name+"'s Profile");
        //getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.buttonGreen)));
        Animation cycleAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cycle);
        cycleAnimation.setRepeatCount(Animation.INFINITE);
        name = (TextView) findViewById(R.id.name);
        name.setAnimation(cycleAnimation);
        experience = (TextView) findViewById(R.id.experience);
        dob = (TextView) findViewById(R.id.dob);
        description = (TextView) findViewById(R.id.bio);
        addFrd = (ImageView) findViewById(R.id.add_friend);
        transferMoney = (ImageView) findViewById(R.id.transfer_money);
        profileImage = (ImageView) findViewById(R.id.profileimage);
        badgeImage = (ImageView) findViewById(R.id.badgeimage);
        addfrdlabel= (TextView) findViewById(R.id.addfrd);


        name.setText(friend.name);
        experience.setText("XP LEVEL: "+friend.xp);
        if(friend.friend.equals("friend")){

            addFrd.setEnabled(false);

        }else if(friend.friend.equals("pending")){
            addFrd.setEnabled(false);
            addFrd.setBackground(getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
           addfrdlabel.setText("Pending");
        }


        Bitmap bitmap= serverConnect.fetchImage(serverConnect.FETCH_IMAGE_URL+friend.email);
        if(bitmap!=null) profileImage.setImageBitmap(bitmap);
        else profileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));

        addFrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value = serverConnect.addfriend(new Utilites().sharePref(getApplicationContext()),friendID);
                addFrd.setBackground(getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
                if(value) addfrdlabel.setText("Pending");
            }
        });

        transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockDialogs stockDialogs=new StockDialogs(view.getContext());
                stockDialogs.transferMoneyDialog(friend.id);

            }
        });
    }
}
