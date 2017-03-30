package nox.finzone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nox on 1/22/2017.
 */
public class Utilites {

    private static final String MY_PREFS_NAME = "FinZonePref";
    public String createPassword(String password,String id){

        SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher= null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(id.getBytes("UTF-8"));
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String encryptValue=md.digest(encrypted).toString();
            return encryptValue;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Bitmap getBitmapFromVector(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void sharePref(Context context, String email, String password,String userId)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("userid", userId);
        editor.commit();
    }

    public String sharePref(Context context)
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("email",null);
    }
    public String sharePrefID(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("userid",null);
    }

}
