package nox.finzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "FinZonePref";
    CallbackManager callbackManager;
    byte[] key_decrypt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };
    int RC_SIGN_IN= 1230;
    Utilites utilites;
    GoogleApiClient mGoogleApiClient;
    ServerConnect serverConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_sign_in);

        serverConnect = new ServerConnect();
        final EditText form_email = (EditText) findViewById(R.id.username);
        final EditText form_password = (EditText) findViewById(R.id.password);
        final TextView error_message = (TextView) findViewById(R.id.textView3);

        MaterialRippleLayout form_button = (MaterialRippleLayout) findViewById(R.id.form_login_button);


        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String email = sharedPref.getString("email",null);
        String password = sharedPref.getString("password", null);
        String userID = sharedPref.getString("userid", null);

        if(email != null)
        {
            Bundle bundle=new Bundle();
            bundle.putString("username",email);
            bundle.putString("password", password);
            bundle.putString("userid", userID);
            moveHomeActivtiy(bundle);
        }


        // market.getStockList(getApplicationContext());

        form_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(form_email.getText().toString().equals("")){
                    error_message.setText("Email Address field is empty");
                }else if(form_password.getText().toString().equals("")){
                    error_message.setText("Password field is empty");
                }else {

                    boolean serverreturn = serverConnect.checklogin(form_email.getText().toString(), form_password.getText().toString());
                    if (serverreturn) {
                        Utilites utilites = new Utilites();
                        ServerConnect serverConnect=new ServerConnect();
                        String [] values=serverConnect.getCredentials(form_email.getText().toString());
                        utilites.sharePref(getApplicationContext(), form_email.getText().toString(), form_password.getText().toString(),values[8]);
                        moveHomeActivtiy(new Bundle());
                    } else {
                        error_message.setText("Invalid Email or Password");
                        error_message.setTextColor(Color.parseColor("#ea0a20"));
                    }
                }
            }
        });



        final ServerConnect serverConnect=new ServerConnect();
        utilites=new Utilites();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.i("Gmail Error Message:",connectionResult.getErrorMessage());
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });


        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken();

                Profile profile = Profile.getCurrentProfile();
                //System.out.print(profile.getFirstName() + profile.getLastName());
                //System.out.print(profile.getProfilePictureUri(400,400).toString());
                //String password=utilites.createPassword("D461DAE2249509C8F8EAB830ECAA9212", profile.getId());
                String password=utilites.md5(profile.getId());
                String person_id=profile.getId();
                String img_url= profile.getProfilePictureUri(400, 400).toString();
                String name=profile.getFirstName() + " " + profile.getLastName();
                Bundle bundle=new Bundle();
                bundle.putString("username",profile.getId());
                bundle.putString("password", password);
                //String img_url= profile.getProfilePictureUri(400, 400).toString();
                boolean value=serverConnect.socialCheckLogin(person_id,password,new String[]{img_url,name});
                moveHomeActivtiy(bundle);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("resultme",String.valueOf(resultCode));
        if(resultCode==-1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        //Uri uri=data.getData();
        //System.out.print(uri.getEncodedPath());
    }
    private void handleSignInResult(GoogleSignInResult result) {

        if(result!=null){
            Log.i("handleSignInResult:", String.valueOf(result.isSuccess()));
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                // String password=utilites.createPassword("D461DAE2249509C8F8EAB830ECAA9212", acct.getEmail());
                String password=utilites.md5(acct.getEmail());
                System.out.println(password);
                Bundle bundle=new Bundle();
                bundle.putString("username",acct.getEmail());
                bundle.putString("password", password);
                String img_url=acct.getPhotoUrl().toString();
                boolean value=serverConnect.socialCheckLogin(acct.getEmail(), password, new String[]{img_url, acct.getGivenName() + " " + acct.getFamilyName()});
                if(value) moveHomeActivtiy(bundle);
            } else {
                // Signed out, show unauthenticated UI.

            }
        }

    }



    private void moveHomeActivtiy(Bundle bundle){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
