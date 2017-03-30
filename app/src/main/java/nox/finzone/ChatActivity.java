package nox.finzone;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.security.cert.Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Classes.DBHelper;
import adapters.ChatAdapter;

import static nox.finzone.R.id.qty;
import static nox.finzone.R.id.textmsg;

public class ChatActivity extends AppCompatActivity {
     EditText textmsg;
    ChatAdapter chatAdapter;
    ServerConnect serverConnect;
    String timestamp,messageTime;
     DBHelper db;
    List<ServerConnect.ChatMsgHistory> stockList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_chat);

        serverConnect = new ServerConnect();
        Log.i("Friend",getIntent().getStringExtra("FriendID"));
        System.out.print(getIntent().getStringExtra("FriendID"));

        //stockList = serverConnect.getChatMessages(new String[]{ new Utilites().sharePref(getApplicationContext()),getIntent().getStringExtra("FriendID")});

        db = new DBHelper(getApplicationContext());

        Cursor cursor= db.getData(Integer.parseInt(new Utilites().sharePrefID(getApplicationContext())),Integer.parseInt(getIntent().getStringExtra("FriendID")));
        cursor.moveToFirst();
        Log.i("Count", String.valueOf(cursor.getColumnNames()[1])+"  "+cursor.getCount());

        while (!cursor.isAfterLast())
        {
            ServerConnect.ChatMsgHistory chatMsgHistory= new ServerConnect.ChatMsgHistory();
            chatMsgHistory.message=cursor.getString(cursor.getColumnIndex("message"));
            chatMsgHistory.from_userID=cursor.getString(cursor.getColumnIndex("from_userID"));
            chatMsgHistory.to_userID=cursor.getString(cursor.getColumnIndex("to_userID"));
            chatMsgHistory.messageID= String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
            chatMsgHistory.timstamp=cursor.getString(cursor.getColumnIndex("dateTime"));
            timestamp=cursor.getString(cursor.getColumnIndex("dateTime"));
            stockList.add(chatMsgHistory);
            cursor.moveToNext();
        }
        chatAdapter = new ChatAdapter(stockList,getApplicationContext());
        Button sendmsgbtn = (Button) findViewById(R.id.sendmsg);
        textmsg = (EditText) findViewById(R.id.textmsg);
        Thread thread=new Thread(new CheckMessageThread(serverConnect));
       thread.start();

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.msg_list);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(chatAdapter);

         sendmsgbtn.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View view) {

                 messageTime= String.valueOf(new Date().getTime());

                 boolean dbquery = db.addmessage(new Utilites().sharePrefID(getApplicationContext()),getIntent().getStringExtra("FriendID"),textmsg.getText().toString(),messageTime);


                 if(dbquery) {

                 }
                 serverConnect.MESSAGE=textmsg.getText().toString();

                 Thread thread=new Thread(new SendMessageThread(serverConnect));
                 thread.start();

                 textmsg.setText("");
                 ServerConnect.ChatMsgHistory chatMsgHistory= new ServerConnect.ChatMsgHistory();
                 chatMsgHistory.message=serverConnect.MESSAGE;
                 chatMsgHistory.to_userID=getIntent().getStringExtra("FriendID");
                 chatMsgHistory.from_userID=new Utilites().sharePref(getApplicationContext());
                 chatMsgHistory.timstamp=messageTime;
                 stockList.add(chatMsgHistory);
                 timestamp=messageTime;
                 chatAdapter.notifyDataSetChanged();
             }
         });

    }

    public class SendMessageThread implements Runnable{
        ServerConnect serverConnect;
        SendMessageThread(ServerConnect serverConnect){
            this.serverConnect=serverConnect;

        }

        @Override
        public void run() {
            serverConnect.sendChatMsg(new String[]{ new Utilites().sharePrefID(getApplicationContext()),getIntent().getStringExtra("FriendID")
                    ,serverConnect.MESSAGE,messageTime});
        }

    }
    public class CheckMessageThread implements Runnable{
        ServerConnect serverConnect;
        CheckMessageThread(ServerConnect serverConnect){
            this.serverConnect=serverConnect;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            while(true) {
                List<ServerConnect.ChatMsgHistory> chatMsgHistories=serverConnect.getChatMessages(new Utilites().sharePrefID(getApplicationContext()),
                        getIntent().getStringExtra("FriendID"),timestamp);
                for(int i=0;i<chatMsgHistories.size();i++){
                    stockList.add(chatMsgHistories.get(i));
                    db.addmessage(chatMsgHistories.get(i).from_userID,chatMsgHistories.get(i).to_userID,chatMsgHistories.get(i).message,chatMsgHistories.get(i).timstamp);
                    timestamp=chatMsgHistories.get(i).timstamp;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        chatAdapter.notifyDataSetChanged();

                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

   /* public class ChatInsetData extends AsyncTask<ServerConnect,Void,Boolean> {

        @Override
        protected Boolean doInBackground(ServerConnect... serverConnects) {
            return Boolean.valueOf(serverConnects[0].sendChatMsg(new String[]{ new Utilites().sharePref(getApplicationContext()),getIntent().getStringExtra("FriendID")
                    ,serverConnects[0].MESSAGE}));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

           // stockList = serverConnect.getChatMessages(new String[]{ new Utilites().sharePref(getApplicationContext()),"testing@gmail.com"});
            while(true){
                stockList.clear();
                stockList.addAll(serverConnect.getChatMessages(new String[]{ new Utilites().sharePref(getApplicationContext()),
                        getIntent().getStringExtra("FriendID")}));
                chatAdapter.notifyDataSetChanged();

            }



        }
    }*/
}
