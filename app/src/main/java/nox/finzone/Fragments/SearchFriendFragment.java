package nox.finzone.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import adapters.FriendSearchAdapter;
import adapters.NotifyFriendAdapter;
import nox.finzone.R;
import nox.finzone.ServerConnect;
import nox.finzone.Utilites;

import static android.R.id.tabhost;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFriendFragment newInstance(String param1, String param2) {
        SearchFriendFragment fragment = new SearchFriendFragment();
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
        View view= inflater.inflate(R.layout.fragment_search_friend, container, false);
         SearchView searchView=(SearchView)view.findViewById(R.id.friend_search);

        TextView emptyView=new TextView(getContext());
        emptyView.setGravity(View.TEXT_ALIGNMENT_CENTER);

        final ListView listView=(ListView)view.findViewById(R.id.friends_list);
        final FriendSearchAdapter friendSearchAdapter=new FriendSearchAdapter(view.getContext());
        ListView notifylist= (ListView) view.findViewById(R.id.notification_list);
        emptyView.setText("No Notifications");
        notifylist.setEmptyView(emptyView);
        NotifyFriendAdapter notifyFriendAdapter=new NotifyFriendAdapter(new ServerConnect().getNotifications(new Utilites().sharePref(view.getContext())));
        notifylist.setAdapter(notifyFriendAdapter);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(friendSearchAdapter);
                friendSearchAdapter.filter(new ServerConnect().searchFriend(s));
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.INVISIBLE);
                return false;
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
