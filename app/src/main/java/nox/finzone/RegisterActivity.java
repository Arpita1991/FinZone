package nox.finzone;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import CustomDialogs.StockDialogs;
import Views.RoundCircleView;

public class RegisterActivity extends AppCompatActivity {
    int CAMERA_PIC_REQUEST=100;
    int SELECT_PICTURE=101;
    Bitmap image;
    ImageView uploadImage;
    RoundCircleView roundCircleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView description = (TextView) findViewById(R.id.tagline);
        final TextView emailAddress = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView personName = (TextView) findViewById(R.id.person_name);
        final TextView dob = (TextView) findViewById(R.id.DOB);
        final TextView errorMsg = (TextView) findViewById(R.id.error_msg);
        roundCircleView=(RoundCircleView)findViewById(R.id.circle_image);
        uploadImage = (ImageView) findViewById(R.id.upload_image);


        image = new Utilites().getBitmapFromVector(getResources().getDrawable(R.drawable.ic_person));

        Button submit = (Button) findViewById(R.id.submit);
        final Calendar myCalendar = Calendar.getInstance();


        //------------------------------------Listeners----------------------------------------------//
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()).toString());
            }

        };

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockDialogs stockDialogs = new StockDialogs(RegisterActivity.this);
                stockDialogs.taglineDialog(description);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDialog();
            }
        });
        roundCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailAddress.getText().toString().equals("")) {
                    errorMsg.setText("Email Address field is empty");
                } else if (password.getText().toString().equals("")) {
                    errorMsg.setText("Password field is empty");
                } else if (personName.getText().toString().equals("")) {
                    errorMsg.setText("Password field is empty");
                } else {
                    ServerConnect serverConnect = new ServerConnect();
                    boolean value = serverConnect.register(new String[]{
                            personName.getText().toString(),
                            emailAddress.getText().toString(),
                           new Utilites().md5(password.getText().toString()),description.getText().toString(),dob.getText().toString()
                    },image,false);
                    if (value) {
                        Utilites utilites = new Utilites();

                        String [] values=serverConnect.getCredentials(emailAddress.getText().toString());
                        utilites.sharePref(getApplicationContext(), emailAddress.getText().toString(), password.getText().toString(),values[8]);
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
        //---------------------------------------------------------------------------------------------//

    public void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }
    public void openGallery(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            image = (Bitmap) data.getExtras().get("data");
            createBitmap(image);

        }
        if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                if(selectedImageUri!=null){
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                createBitmap(image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Bitmap getImageFromSource(){
        return image;
    }
    public void listDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Utilites utilites=new Utilites();
        CharSequence[] items = { "BROWSE IMAGE", "OPEN CAMERA"};
        builder.setTitle("Image Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        openGallery();

                        break;
                    case 1:
                        openCamera();

                        break;

                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public void createBitmap(Bitmap bitmap){

        Bitmap bitmap1=bitmap.copy(Bitmap.Config.ARGB_4444,true);
        roundCircleView.setImageDrawable(new BitmapDrawable(bitmap));
        roundCircleView.setVisibility(View.VISIBLE);
        uploadImage.setVisibility(View.INVISIBLE);

    }
}

