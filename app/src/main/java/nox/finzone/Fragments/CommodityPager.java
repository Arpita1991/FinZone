package nox.finzone.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import adapters.CommodityAdapter;
import nox.finzone.Market;
import nox.finzone.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommodityPager.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommodityPager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommodityPager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton floatEnergy;
    FloatingActionButton floatMetal;
    FloatingActionButton floatAgri;
    FloatingActionButton floatLivestock;
    FloatingActionButton floatConsumer;
    RelativeLayout relLayout;
    LinearLayout linearLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CommodityPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommodityPager.
     */
    // TODO: Rename and change types and number of parameters
    public static CommodityPager newInstance(String param1, String param2) {
        CommodityPager fragment = new CommodityPager();
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
        View view= inflater.inflate(R.layout.fragment_commodity_pager, container, false);
        final Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        final RecyclerView energyView= (RecyclerView) view.findViewById(R.id.energy);
        final RecyclerView metalView= (RecyclerView) view.findViewById(R.id.metals);
        final RecyclerView agriView= (RecyclerView) view.findViewById(R.id.agiriculture);
        final RecyclerView livestockView= (RecyclerView) view.findViewById(R.id.livestock);
        final RecyclerView consumerView= (RecyclerView) view.findViewById(R.id.consumer);

        final TextView headerText = (TextView) view.findViewById(R.id.head);
        floatEnergy= (FloatingActionButton) view.findViewById(R.id.float_energy);
        floatMetal= (FloatingActionButton) view.findViewById(R.id.float_metal);
        floatAgri = (FloatingActionButton) view.findViewById(R.id.float_agri);
        floatLivestock= (FloatingActionButton) view.findViewById(R.id.float_livestock);
        floatConsumer = (FloatingActionButton) view.findViewById(R.id.float_consumer);
        relLayout= (RelativeLayout) view.findViewById(R.id.rel_lay);
        linearLayout= (LinearLayout) view.findViewById(R.id.linear_layout);
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
        relLayout.setBackgroundColor(getResources().getColor(R.color.purple));
        headerText.setText("Metals");

        floatAgri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
                relLayout.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
                headerText.setText("Agriculture");
                agriView.setVisibility(View.VISIBLE);
                energyView.setVisibility(View.GONE);
                metalView.setVisibility(View.GONE);
                livestockView.setVisibility(View.GONE);
                consumerView.setVisibility(View.GONE);

            }
        });

        floatEnergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setBackgroundColor(Color.parseColor("#D24958"));
                relLayout.setBackgroundColor(Color.parseColor("#D24958"));
                headerText.setText("Energy");
                agriView.setVisibility(View.GONE);
                energyView.setVisibility(View.VISIBLE);
                metalView.setVisibility(View.GONE);
                livestockView.setVisibility(View.GONE);
                consumerView.setVisibility(View.GONE);
            }
        });

        floatMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
                relLayout.setBackgroundColor(getResources().getColor(R.color.purple));
                headerText.setText("Metals");
                agriView.setVisibility(View.GONE);
                energyView.setVisibility(View.GONE);
                metalView.setVisibility(View.VISIBLE);
                livestockView.setVisibility(View.GONE);
                consumerView.setVisibility(View.GONE);
            }
        });

        floatConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.buttonGreen));
                relLayout.setBackgroundColor(getResources().getColor(R.color.buttonGreen));
                headerText.setText("Consumer");
                agriView.setVisibility(View.GONE);
                energyView.setVisibility(View.GONE);
                metalView.setVisibility(View.GONE);
                livestockView.setVisibility(View.GONE);
                consumerView.setVisibility(View.VISIBLE);
            }
        });

        floatLivestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.lightYellow));
                relLayout.setBackgroundColor(getResources().getColor(R.color.lightYellow));
                headerText.setText("Livestock");
                agriView.setVisibility(View.GONE);
                energyView.setVisibility(View.GONE);
                metalView.setVisibility(View.GONE);
                livestockView.setVisibility(View.VISIBLE);
                consumerView.setVisibility(View.GONE);
            }
        });





        Market market=new Market();

        //Commodity Energy
        CommodityAdapter commodityAdapter=new CommodityAdapter(market.getCommodityInfo(0));

        energyView.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        energyView.setAdapter(commodityAdapter);

        //Metal
        commodityAdapter=new CommodityAdapter(market.getCommodityInfo(1));

        metalView.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        metalView.setAdapter(commodityAdapter);

        commodityAdapter=new CommodityAdapter(market.getCommodityInfo(2));

        agriView.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        agriView.setAdapter(commodityAdapter);

        commodityAdapter=new CommodityAdapter(market.getCommodityInfo(3));

        livestockView.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        livestockView.setAdapter(commodityAdapter);

        commodityAdapter=new CommodityAdapter(market.getCommodityInfo(4));

        consumerView.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        consumerView.setAdapter(commodityAdapter);

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
