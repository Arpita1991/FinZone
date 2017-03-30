package nox.finzone.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import org.w3c.dom.Text;

import java.io.IOException;

import Views.RoundCircleView;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public boolean flag = false;
    TextView emailAddr,badge,experience,dob,profileName,location,description,password,accountNumber;
    RoundCircleView profileImage;
    int CAMERA_PIC_REQUEST=100;
    int SELECT_PICTURE=101;
    Bitmap image;
    ImageView uploadImage,edit_password,edit_name,edit_desc,edit_location;

    RoundCircleView roundCircleView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.profileColor));
        toolbar.setTitle("Profile");
        view.setOnClickListener(new ViewClicks());
        emailAddr=(TextView)view.findViewById(R.id.email_addr);
        profileImage=(RoundCircleView)view.findViewById(R.id.user_image);
        profileName=(TextView) view.findViewById(R.id.profilename);
        location=(TextView) view.findViewById(R.id.location);
        dob=(TextView)view.findViewById(R.id.dob);
        badge=(TextView)view.findViewById(R.id.badges);
        experience=(TextView)view.findViewById(R.id.experience);
        description=(TextView) view.findViewById(R.id.description);
        password=(TextView) view.findViewById(R.id.password);
        accountNumber=(TextView)view.findViewById(R.id.account_no);

        edit_password=(ImageView) view.findViewById(R.id.edit_password);
        edit_location=(ImageView) view.findViewById(R.id.edit_location);
        edit_name=(ImageView) view.findViewById(R.id.edit_name);
        edit_desc=(ImageView) view.findViewById(R.id.edit_desc);

        final ViewSwitcher nameSwitcher= (ViewSwitcher) view.findViewById(R.id.switch_name);
        final ViewSwitcher descriptionSwitcher= (ViewSwitcher) view.findViewById(R.id.switch_desc);
        final ViewSwitcher locationSwitcher= (ViewSwitcher) view.findViewById(R.id.switch_location);
        final ViewSwitcher passSwitcher= (ViewSwitcher) view.findViewById(R.id.switch_password);
        nameSwitcher.showNext();
        descriptionSwitcher.showNext();
        locationSwitcher.showNext();
        passSwitcher.showNext();

        final FloatingActionButton morebtn = (FloatingActionButton) view.findViewById(R.id.more);
        FloatingActionButton saveData = (FloatingActionButton) view.findViewById(R.id.save);
        FloatingActionButton uploadImage = (FloatingActionButton) view.findViewById(R.id.upload_image);
        final LinearLayout floatbtn_layout = (LinearLayout) view.findViewById(R.id.floatbtn_layout);

        profileName.clearFocus();

        final ServerConnect serverConnect=new ServerConnect();
        String[] credentials=serverConnect.getCredentials(new Utilites().sharePref(getContext()));
        emailAddr.setText(credentials[0]);
        profileName.setText(credentials[1]);
        badge.setText(credentials[2]);
        experience.setText(credentials[3]);
        description.setText(credentials[4]);
        location.setText(credentials[5]);
        dob.setText(credentials[6]);
        accountNumber.setText(credentials[7]);
        Bitmap bitmap=serverConnect.fetchImage(serverConnect.FETCH_IMAGE_URL+new Utilites().sharePref(getContext()));
        image=bitmap;
        if(bitmap!=null) profileImage.setImageBitmap(bitmap);
        else profileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDialog();
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                passSwitcher.showNext();
                ((EditText)passSwitcher.getChildAt(0)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ((TextView)passSwitcher.getNextView()).setText(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        edit_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionSwitcher.showNext();
                ((EditText)descriptionSwitcher.getChildAt(0)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ((TextView)descriptionSwitcher.getNextView()).setText(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSwitcher.showNext();
                ((EditText)locationSwitcher.getChildAt(0)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ((TextView)locationSwitcher.getNextView()).setText(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameSwitcher.showNext();
                ((EditText)nameSwitcher.getChildAt(0)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ((TextView)nameSwitcher.getNextView()).setText(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value = serverConnect.register(new String[]{
                        profileName.getText().toString(),
                        emailAddr.getText().toString(),
                        new Utilites().md5(password.getText().toString()),description.getText().toString(),dob.getText().toString(),location.getText().toString()
                },image,true);
                if (value) {

                }
            }
        });

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                Animation animBottom= AnimationUtils.loadAnimation(getContext(),
                        R.anim.bottom);
                Animation animTop= AnimationUtils.loadAnimation(getContext(),
                        R.anim.top);
                if(!flag){
                    ViewCompat.animate(morebtn)
                            .rotation(180.0F)
                            .withLayer()
                            .setDuration(300L)
                            .setInterpolator(new OvershootInterpolator(10.0F))
                            .start();
                    morebtn.setImageResource(R.drawable.ic_clear_black_24dp);
                    flag = true;
                    floatbtn_layout.setAnimation(animTop);
                    floatbtn_layout.setVisibility(View.VISIBLE);
                }else {
                    ViewCompat.animate(morebtn)
                            .rotation(0.0F)
                            .withLayer()
                            .setDuration(300L)
                            .setInterpolator(new OvershootInterpolator(10.0F))
                            .start();
                    morebtn.setImageResource(R.drawable.ic_more_horiz_black_24dp);
                    flag = false;
                    floatbtn_layout.setAnimation(animBottom);
                    floatbtn_layout.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public class ViewClicks implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(!(view instanceof EditText)) {
                profileName.clearFocus();
                password.clearFocus();
                location.clearFocus();
                description.clearFocus();
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            image = (Bitmap) data.getExtras().get("data");
            createBitmap(image);

        }
        if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                if(selectedImageUri!=null){
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        profileImage.setImageDrawable(new BitmapDrawable(bitmap));

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
