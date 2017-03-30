package nox.finzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.util.List;

import adapters.FriendSearchAdapter;
import adapters.SearchAdapter;
import nox.finzone.Fragments.BankFragment;
import nox.finzone.Fragments.CommodityPager;
import nox.finzone.Fragments.DetailsFragment;
import nox.finzone.Fragments.ForexPager;
import nox.finzone.Fragments.FriendListFragment;
import nox.finzone.Fragments.HistoryFragment;
import nox.finzone.Fragments.HomeFragment;
import nox.finzone.Fragments.NewsPager;
import nox.finzone.Fragments.PortfolioFragment;
import nox.finzone.Fragments.ProfileFragment;
import nox.finzone.Fragments.SearchFriendFragment;
import nox.finzone.Fragments.StockPager;

import static nox.finzone.R.id.search_frag;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BankFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener
,ProfileFragment.OnFragmentInteractionListener,HistoryFragment.OnFragmentInteractionListener,PortfolioFragment.OnFragmentInteractionListener,FriendListFragment.OnFragmentInteractionListener
,SearchFriendFragment.OnFragmentInteractionListener,StockPager.OnFragmentInteractionListener,ForexPager.OnFragmentInteractionListener,NewsPager.OnFragmentInteractionListener,
        CommodityPager.OnFragmentInteractionListener,DetailsFragment.OnFragmentInteractionListener{
    Intent intent;
    SearchAdapter searchAdapter;
    RelativeLayout  searchLayout;
    private static final String MY_PREFS_NAME = "FinZonePref";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_home);
        searchLayout=(RelativeLayout)findViewById(R.id.search_rel);
        searchLayout.setVisibility(View.INVISIBLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.homeColor));




        if(intent!=null){
            startActivity(intent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Market market = new Market();
        List<StockSearchList> stockList = market.getStockList(getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.searchList);


         searchAdapter = new SearchAdapter(this,stockList);
        listView.setAdapter(searchAdapter);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        // on back presssed fragment

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        final RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.home_layout);
        SearchView searchView=(SearchView) menu.getItem(0).getActionView();


        final ListView listView = (ListView) findViewById(R.id.searchList);
        searchView.setQueryHint("Search Quotes...");
        searchView.setIconified(true);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listView.bringToFront();
                listView.setVisibility(View.VISIBLE);
                searchAdapter.getFilter(s);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.GONE);
                return false;
            }
        });




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.frdsearch) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.content_home);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            SearchFriendFragment searchFriendFragment=(SearchFriendFragment) getSupportFragmentManager().findFragmentByTag("Search_Fragment");

            if (searchFriendFragment != null && searchFriendFragment.isVisible()) {
                fragmentTransaction.hide(searchFriendFragment);
                fragmentTransaction.remove(searchFriendFragment).commit();
                getSupportFragmentManager().popBackStack();
            }
            else {
                SearchFriendFragment searchFriendFragment1=new SearchFriendFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);
                fragmentTransaction.add(R.id.content_home, searchFriendFragment1,"Search_Fragment");
                fragmentTransaction.commit();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.content_home);

        if (id == R.id.nav_profile) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ProfileFragment profileFragment = new ProfileFragment();
            rl.removeAllViews();

            transaction.replace(R.id.content_home, profileFragment);
            transaction.commit();

        } else if (id == R.id.nav_history) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HistoryFragment historyFragment = new HistoryFragment();

            rl.removeAllViews();
            transaction.replace(R.id.content_home, historyFragment);
            transaction.commit();

        }
        else if (id == R.id.nav_home) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            rl.removeAllViews();
            transaction.replace(R.id.content_home, homeFragment);

            transaction.commit();

        }
        else if (id == R.id.nav_bank) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BankFragment bankFragment = new BankFragment();

            rl.removeAllViews();
            transaction.replace(R.id.content_home, bankFragment);
            transaction.commit();

        }
        else if (id == R.id.nav_portfolio) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            PortfolioFragment portfolioFragment = new PortfolioFragment();
            rl.removeAllViews();

            transaction.replace(R.id.content_home, portfolioFragment,"portfolio");
            transaction.commit();

        }
        else if (id == R.id.nav_frdList) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FriendListFragment friendListFragment = new FriendListFragment();
            rl.removeAllViews();
            transaction.replace(R.id.content_home, friendListFragment);
            transaction.commit();

        }
        else if (id == R.id.nav_logout) {
            SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            sharedPref.edit().putString("email",null).commit();
            sharedPref.edit().putString("password",null).commit();
            intent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
