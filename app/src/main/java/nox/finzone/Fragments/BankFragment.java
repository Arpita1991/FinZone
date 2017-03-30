package nox.finzone.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import CustomDialogs.StockDialogs;
import adapters.LoanAdapter;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.Utilites;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int rate = 9;
    boolean flag=false;
    String time=null;
    private OnFragmentInteractionListener mListener;

    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankFragment newInstance(String param1, String param2) {
        BankFragment fragment = new BankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.homeColor));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bank, container, false);

        RecyclerView bankView=(RecyclerView)view.findViewById(R.id.loan_history);
        LinearLayout emptyView = (LinearLayout) view.findViewById(R.id.linear_below);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lin_below);
        final LinearLayout formLayout = (LinearLayout) view.findViewById(R.id.form_view);
        final FloatingActionButton floatingActionButton= (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    ViewCompat.animate(floatingActionButton)
                            .rotation(45.0F)
                            .withLayer()
                            .setDuration(300L)
                            .setInterpolator(new OvershootInterpolator(10.0F))
                            .start();

                    linearLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);
                    flag=false;
                }else{
                    ViewCompat.animate(floatingActionButton)
                            .rotation(180.0F)
                            .withLayer()
                            .setDuration(300L)
                            .setInterpolator(new OvershootInterpolator(10.0F))
                            .start();
                    linearLayout.setVisibility(View.VISIBLE);
                    formLayout.setVisibility(View.GONE);
                    flag=true;
                }
            }
        });
        final EditText amountValue = (EditText) view.findViewById(R.id.amount);

        Button getloanbtn = (Button) view.findViewById(R.id.form_getloan_button);
        final TextView textView=(TextView)view.findViewById(R.id.interest_rate);
        final StockDialogs stockDialogs=new StockDialogs(getContext());
        //------------modifcation needed------
        final TextView bankAmount=(TextView)view.findViewById(R.id.bank_amount);
        final String amount=new Market().getBankAmount(new Utilites().sharePref(getContext()));
        bankAmount.setText("Bank Amount: "+amount);
        amountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    Double value=Double.parseDouble(amount)
                            -Double.parseDouble(charSequence.toString());
                    bankAmount.setText("Bank Amount: "+value);
                }
                else{
                    bankAmount.setText("Bank Amount: "+amount);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //--------------------------------------------
        final Spinner spinner=(Spinner)view.findViewById(R.id.time_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[]{"1 week","2 week ","3 week"," 1 month"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rate =i+9;
                textView.setText("Interest Rate :"+String.valueOf(i+9)+"%");
                time= (String) spinner.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getloanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Market market=new Market();
                boolean value=market.getLoan(new String[]{new Utilites().sharePref(getContext()),amountValue.getText().toString(),String.valueOf(rate),time});
                if(!value) Toast.makeText(getContext(), "Not have enough credit to take loan",
                        Toast.LENGTH_LONG).show();
                else{
                    stockDialogs.loanTaken();
                }
            }
        });

        // loan list view

        List<Market.LoanHistory> loanHistoryList=new Market().getLoanHistory(new Utilites().sharePref(getContext()));
        if(loanHistoryList.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
            bankView.setVisibility(View.GONE);
        }else{
            LoanAdapter loanAdapter=new LoanAdapter(loanHistoryList);
            bankView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            bankView.setAdapter(loanAdapter);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
