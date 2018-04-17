package com.diot.safepasswords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvnavsessionemail;
    AllUtiityOneClass allUtiityOneClass;
    public static final String MyPREFERENCES = "Session" ;
    public static String sessionemail = "";

    //custome listview
    List<ListDataModal> dataList;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        allUtiityOneClass = new AllUtiityOneClass();

        //navDataSetOnLogin();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(HomeActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        sessionemail = sharedpreferences.getString("sessionemail","null");

//        Toast.makeText(this,""+sessionemail,Toast.LENGTH_SHORT).show();
//        tvnavsessionemail = navigationView.findViewById(R.id.textView);
//        tvnavsessionemail.setText(sessionemail);


        //custom list

        updatelist();


    }

    //putting data in custom listview
    public void updatelist(){

        //initializing objects
        dataList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.home_listview);

        //adding some values to our list
        dataList.add(new ListDataModal("nikhil_cs", "nikhil.com"));
        dataList.add(new ListDataModal("Joker", "websitename.com"));


        //creating the adapter
        MyListAdapter adapter = new MyListAdapter(this, R.layout.custom_listview, dataList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
    }

    private void navDataSetOnLogin() {

        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        sessionemail = sharedpreferences.getString("sessionemail","null");

        NavigationView navigationView = findViewById(R.id.nav_view);
        //inflate header layout
        View navView =  navigationView.inflateHeaderView(R.layout.nav_header_home);
        //reference to views
        //ImageView imgvw = (ImageView)navView.findViewById(R.id.imageView);
        TextView tv = navView.findViewById(R.id.textView);
        //set views
        //imgvw.setImageResource(R.drawable.your_image);
        tv.setText(sessionemail);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                //TODO setting to be done

                return true;
            case R.id.action_logout:
                logout();
                return true;
            case android.R.id.home:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void logout(){
        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this,"Logging out!",Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        if(allUtiityOneClass.isAppIsInBackground(this))
            logout();
    }

//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        logout();
//    }


   /* @Override
    public void finish() {
        super.finish();
        logout();
    }*/
}

