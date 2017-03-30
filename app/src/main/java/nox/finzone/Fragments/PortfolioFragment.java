package nox.finzone.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.StockHistoryAdapter;
import nox.finzone.Market;
import nox.finzone.R;
import nox.finzone.Utilites;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PortfolioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PortfolioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PortfolioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PortfolioFragment newInstance(String param1, String param2) {
        PortfolioFragment fragment = new PortfolioFragment();
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
        View view =inflater.inflate(R.layout.fragment_portfolio, container, false);
        ArrayList<Object> entries=new ArrayList<>();

        try {
            Market market=new Market(URLEncoder.encode("CADUSD=X","UTF-8"));
            entries=market.getPortfolioData(new Utilites().sharePref(getContext()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<PieEntry> pieEntries=new ArrayList<>();
        List<BarEntry> barEntries=new ArrayList<>();
        List<Integer> pieColors=new ArrayList<>();
        Collections.addAll(pieColors,getResources().getColor(R.color.redColor),getResources().getColor(R.color.purple),getResources().getColor(R.color.buttonLightBlue)
                ,getResources().getColor(R.color.lightYellow),getResources().getColor(R.color.greenColor));

        pieEntries= (List<PieEntry>) entries.get(0);
        barEntries= (List<BarEntry>) entries.get(1);


        //---------- PIE CHART ------------------//


        final PieChart pieChart=(PieChart)view.findViewById(R.id.pie_chart);
        final PieDataSet pieDataSet=new PieDataSet(pieEntries,"Portfolio");
        pieChart.setDrawCenterText(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.getHighlighted();
        pieDataSet.setSliceSpace(5.0f);
        pieDataSet.setColors(pieColors);
        pieDataSet.setHighlightEnabled(true);
        pieDataSet.setHighlightEnabled(true);
        pieDataSet.setDrawValues(true);
        pieChart.setUsePercentValues(true);




        pieDataSet.setValueLineColor(Color.GREEN);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pieEntry= (PieEntry) e;
                Log.i("index",pieEntry.getLabel());

                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                DetailsFragment detailsFragment=new DetailsFragment();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
                fragmentTransaction.add(R.id.content_home,detailsFragment,"details");
                if(pieEntry.getLabel().equals("Forex")){
                    Bundle bundle=new Bundle();
                    bundle.putString("Domain",pieEntry.getLabel());

                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.commit();

                } else if(pieEntry.getLabel().equals("Bank")){
                    Bundle bundle=new Bundle();
                    bundle.putString("Domain",pieEntry.getLabel());
                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }else if(pieEntry.getLabel().equals("Commodity")){
                    Bundle bundle=new Bundle();
                    bundle.putString("Domain",pieEntry.getLabel());
                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }else if(pieEntry.getLabel().equals("Stock")){
                    Bundle bundle=new Bundle();
                    bundle.putString("Domain",pieEntry.getLabel());
                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }else if(pieEntry.getLabel().equals("Loan")){
                    Bundle bundle=new Bundle();
                    bundle.putString("Domain",pieEntry.getLabel());
                    detailsFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }


            }

            @Override
            public void onNothingSelected() {

            }
        });

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        //------------

       /* StockHistoryAdapter stockHistoryAdapter=new StockHistoryAdapter(
                market.getStockHistory(new Utilites().sharePref(getContext())));
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.stock_portfolio);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(stockHistoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
*/

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
