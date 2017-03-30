package nox.finzone;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Apatel9273 on 1/20/2017.
 */

public class ServerConnect{
    String mainUrl="https://finzone-db-arpitapatel.c9users.io/login.php?";
    String userUrl="https://finzone-db-arpitapatel.c9users.io/userprofile.php?";
    String signupUrl="https://finzone-db-arpitapatel.c9users.io/signup.php?";
    String bsstockUrl="https://finzone-db-arpitapatel.c9users.io/bsstock.php?";
    String getLoanUrl="https://finzone-db-arpitapatel.c9users.io/getLoan.php?";
    String getChatmsgsUrl="https://finzone-db-arpitapatel.c9users.io/getmessages.php?";
    String SendChatmsgsUrl="https://finzone-db-arpitapatel.c9users.io/sendmessages.php?";
    String SendfriendUrl="https://finzone-db-arpitapatel.c9users.io/searchfriend.php?";
    String getfriendDetailUrl="https://finzone-db-arpitapatel.c9users.io/getfriendDetail.php?";
    String addFrdUrl="https://finzone-db-arpitapatel.c9users.io/addFrd.php?";
    String getnotifyFrdUrl="https://finzone-db-arpitapatel.c9users.io/fnotify.php?";
    String notifyRequestUrl="https://finzone-db-arpitapatel.c9users.io/request.php?";
    String transferUrl="https://finzone-db-arpitapatel.c9users.io/transfer.php?";
    String friendListUrl="https://finzone-db-arpitapatel.c9users.io/flist.php?";
    String updateProfile="https://finzone-db-arpitapatel.c9users.io/updateprofile.php?";
    public String FETCH_IMAGE_URL="https://finzone-db-arpitapatel.c9users.io/getImage.php?email=";



    public String MESSAGE;

    boolean addfriend(String cuser,String frduser)
    {
        URL url = null;
        try {
            url = new URL(addFrdUrl+"cuser="+cuser+"&frdid="+frduser);
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


    boolean checklogin(String username,String password)  {
        URL url = null;
        try {
            url = new URL(mainUrl+"email="+username+"&password="+password+"&sl=false");
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

    public List<Friend> searchFriend(String query)
    {
        URL url = null;
        List<Friend> list=new ArrayList<>();
        try {
            url = new URL(SendfriendUrl+"searchdata="+query);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            System.out.println(SendfriendUrl+"searchdata="+query);
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return list;
            else if(code==200){
                InputStream in = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String data, value = "";
                while ((data = bufferedReader.readLine()) != null)
                {
                    value += data;
                }

                JSONArray jsonArray=new JSONArray(value);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    ServerConnect.Friend friend=new Friend(jsonObject.getString("userID"),
                            jsonObject.getString("username"),jsonObject.getString("type"),jsonObject.getString("emailID"));
                    list.add(friend);
                }
                System.out.print(list.size());
                return list;
            }
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

    public Friend getFriendDetails(String cuser,String fuser)
    {

        URL url = null;
        try {
            url = new URL(getfriendDetailUrl+"currentuser="+cuser+"&frduser="+fuser);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return new Friend();
            else if(code==200) {
                InputStream in = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String data, value = "";
                while ((data = bufferedReader.readLine()) != null)
                {
                    value += data;
                }
                Log.i("Message",value);
                JSONArray jsonArray=new JSONArray(value);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                return new Friend(jsonObject.getString("userID"),jsonObject.getString("username")
                      ,jsonObject.getString("type"),jsonObject.getString("badgeID"),jsonObject.getString("friend"),jsonObject.getString("emailID"));

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {


        }
        return new Friend();
    }



    public class Friend
    {
        public String name,xp,id,badge,friend,email;
        public Friend(){

        }
        public Friend(String id,String name,String xp,String email)
        {
            this.id=id;
            this.name=name;
            this.xp=xp;
            this.email = email;
        }
        public Friend(String id,String name,String xp,String badge,String friend,String email)
        {
            this.id=id;
            this.name=name;
            this.xp=xp;
            this.badge=badge;
            this.friend=friend;
            this.email = email;
        }
    }


    boolean sendChatMsg(String[] values)  {
        URL url = null;
        try {
            url = new URL(SendChatmsgsUrl+"from="+values[0]+"&to="+values[1]+"&message="+URLEncoder.encode(values[2],"utf-8")+"&timestamp="+values[3]);
            System.out.println(SendChatmsgsUrl+"from="+values[0]+"&to="+values[1]+"&message="+values[2]+"&timestamp="+values[3]);
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
    boolean getLoan(String amount,String date,String username)  {
        URL url = null;
        try {
            url = new URL(getLoanUrl+"email="+username+"&amount="+amount+"&date=date");
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

    public List<ServerConnect.ChatMsgHistory> getChatMessages(String fromUser,String toUser,String timestamp) {
        URL url = null;
        List<ChatMsgHistory> list=new ArrayList<>();
        try {
            url = new URL(getChatmsgsUrl+"fromuserID="+fromUser+"&touserID="+toUser+"&timestamp="+timestamp);
           Log.i("Link",url.toString());
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401)
            {
                 return list;
            }
            else if(code==200)
            {
                InputStream in = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String data, value = "";
                while ((data = bufferedReader.readLine()) != null)
                {
                    value += data;
                }


                JSONArray jsonArray=new JSONArray(value);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    ServerConnect.ChatMsgHistory chatMsgHistory= new ServerConnect.ChatMsgHistory();
                    chatMsgHistory.message=jsonObject.getString("message");
                    chatMsgHistory.from_userID=jsonObject.getString("from_userID");
                    chatMsgHistory.to_userID=jsonObject.getString("to_userID");
                    chatMsgHistory.messageID= jsonObject.getString("messageID");
                    chatMsgHistory.timstamp=jsonObject.getString("DateTime");

                    list.add(chatMsgHistory);
                }
                return list;

            }
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

    public static class ChatMsgHistory {

        public String from_userID,message,messageID,to_userID,timstamp;
        public ChatMsgHistory()
        {

        }
        public ChatMsgHistory(String[] array)
        {

        }
    }

    boolean socialCheckLogin(String username,String password,String[] values){
        URL url = null;
        try {
            url = new URL(mainUrl+ "email="+username+"&password="+password+"&sl=true&img="+
                    values[0]+"&username="+values[1]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);

            connection.connect();
            int code = connection.getResponseCode();
            System.out.print("Value"+String.valueOf(code));
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
    public String[] getCredentials(String username){
        URL url = null;
        try{
            url=new URL(userUrl+"email="+username);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream())) ;
            String jsondata="";
            String value;
            while((value=bufferedReader.readLine())!=null){
                jsondata+=value;
            }
            JSONObject jsonObject=new JSONObject(jsondata);
            String emailAddr= (String) jsonObject.get("emailID");
            String type= (String) jsonObject.get("type");
            String uname= (String) jsonObject.get("username");
            String description= (String) jsonObject.get("bio");
            String location=(String)jsonObject.get("location");
            String dob=(String)jsonObject.get("dob");
            String expID= (String) jsonObject.get("expID");
            String accountNo=(String) jsonObject.get("accountNo");
            String userID=(String) jsonObject.get("userID");
            return  new String[]{emailAddr,uname,type,expID,description,location,dob,accountNo,userID};
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public Bitmap fetchImage(String address){
        URL url=null;
        System.out.print(address);
        try{
            url=new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            InputStream in =  httpURLConnection.getInputStream();

            Bitmap image= BitmapFactory.decodeStream(in);
            in.close();
            return image;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean register(String [] credentials,Bitmap bitmap,boolean update)
    {
        String attachmentName = "image";
        String attachmentFileName = "bitmap.png";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        URL url=null;
        try {
            if(update){
                url = new URL(updateProfile + "username=" + credentials[0] + "&email=" + credentials[1] + "&password=" + credentials[2] + "&bio=" + URLEncoder.encode(credentials[3], "UTF-8") + "&dob=" + credentials[4]+ "&location=" + credentials[5]);
                System.out.println(updateProfile+"username="+credentials[0]+"&email="+credentials[1]+"&password="+credentials[2]+"&bio="+credentials[3]+"&dob="+credentials[4]);
            }else {
                url = new URL(signupUrl + "username=" + credentials[0] + "&email=" + credentials[1] + "&password=" + credentials[2] + "&bio=" + URLEncoder.encode(credentials[3], "UTF-8") + "&dob=" + credentials[4]);
                System.out.println(signupUrl+"username="+credentials[0]+"&email="+credentials[1]+"&password="+credentials[2]+"&bio="+credentials[3]+"&dob="+credentials[4]);
            }
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream request = new DataOutputStream(
                    connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

            request.write(Base64.encode(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);
            request.flush();
            request.close();

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

    public boolean bsstock(String [] credentials)
    {
        URL url=null;
        try {
            url = new URL(bsstockUrl+"companyName="+URLEncoder.encode(credentials[0],"utf-8")+"&email="+credentials[1]+
                    "&qty="+credentials[2]+"&price="+credentials[3]+"&symbol="+URLEncoder.encode(credentials[4],"utf-8")+"&domain="+credentials[5]+"&transtype="+credentials[6]);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

            System.out.println(bsstockUrl+"companyName="+URLEncoder.encode(credentials[0],"utf-8")+"&email="+credentials[1]+
                    "&qty="+credentials[2]+"&price="+credentials[3]+"&symbol="+URLEncoder.encode(credentials[4],"utf-8")+"&domain="+credentials[5]+"&transtype="+credentials[6]);
            connection.connect();
            int code = connection.getResponseCode();
            System.out.println(code);
            if(code==401) return false;
            else if(code==201) return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

public List<NotificationFriends> getNotifications(String username){
    URL url = null;
    List<NotificationFriends> notificationFriends=new ArrayList<>();
    try {
        url = new URL(getnotifyFrdUrl+"username="+username);
System.out.println(getnotifyFrdUrl+"username="+username);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.connect();
        int code = connection.getResponseCode();
        if(code==401) return notificationFriends;
        else if(code==200) {
            InputStream in = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String data, value = "";
            while ((data = bufferedReader.readLine()) != null)
            {
                value += data;
            }

            JSONArray jsonArray=new JSONArray(value);

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                notificationFriends.add(new NotificationFriends(jsonObject.getString("username"),jsonObject.getString("userID")));
            }


            return  notificationFriends;

        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {


    }
    return notificationFriends;
}

    public class NotificationFriends{
        public String name,userid;
        public NotificationFriends(String name,String userid){
            this.name=name;
            this.userid=userid;
        }
    }


    public boolean requestNotify(String username,String fuser,boolean status)
    {
        URL url=null;
        try {
            url = new URL(notifyRequestUrl+"username="+username+"&fuser="+fuser+"&status="+status);
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
    public boolean tranferMoney(String amount,String cuser,String fuser){
        URL url=null;
        try {
            url = new URL(transferUrl+"username="+cuser+"&fuser="+fuser+"&amount="+amount);
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

    public List<Friend> getFriendsList(String username){
        URL url = null;
        List<Friend> friendList=new ArrayList<>();
        try {
            url = new URL(friendListUrl+"email="+username);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if(code==401) return friendList;
            else if(code==200) {
                InputStream in = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String data, value = "";
                while ((data = bufferedReader.readLine()) != null)
                {
                    value += data;
                }
                Log.i("Message",value);
                JSONArray jsonArray=new JSONArray(value);

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    friendList.add(new Friend(jsonObject.getString("userID"),jsonObject.getString("username")
                                                ,jsonObject.getString("type"),jsonObject.getString("emailID")));
                }


                return  friendList;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {


        }
        return friendList;
    }
}
