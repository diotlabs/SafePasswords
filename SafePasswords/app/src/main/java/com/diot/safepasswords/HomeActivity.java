package com.diot.safepasswords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    List<OneDataClass> alldataList;
    ListView listView;

    String json;
    AESEncryption aesEncryption;
    final String REGISTER_URL = "http://scholarshealthservices.com/safepasswordapplication/getalldata.php";
    enum StatusReg{
        TRUE,FALSE,NOTCONNECTED,NODATA
    }
    StatusReg status;

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
        aesEncryption = new AESEncryption(sessionemail);
//        Toast.makeText(this,""+sessionemail,Toast.LENGTH_SHORT).show();
//        tvnavsessionemail = navigationView.findViewById(R.id.textView);
//        tvnavsessionemail.setText(sessionemail);


        //custom list

        try {
            getData(aesEncryption.doEncryptionAES(sessionemail));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        updatelist();

    }

    public void goToAddNewItem(View view)
    {
        Intent intent = new Intent(HomeActivity.this, AddItemActivity.class);
        startActivity(intent);
    }

    //putting data in custom listview
    public void updatelist(){

        //initializing objects
//        dataList = new ArrayList<>();
        listView = findViewById(R.id.home_listview);

        //creating the adapter
        MyListAdapter adapter = new MyListAdapter(this, R.layout.custom_listview, dataList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);
                OneDataClass oneDataClass = alldataList.get(position);
                intent.putExtra("OneDataClass",oneDataClass);
                startActivity(intent);
            }
        });
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


    private void getData(final String key) {
        class GetAllData extends AsyncTask<String, Void, String> {
            ProgressDialog loading = null;
            ConnectClass loginuser = new ConnectClass();
            //            EditText EditText = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                EditText = (EditText) findViewById(R.id.);
                loading = ProgressDialog.show(HomeActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                TextView tvnodata = findViewById(R.id.textView_nodatashow);
//                if(loading != null && loading.isShowing()){ loading.dismiss();}
//                json = new String(response);
//                response.trim();
                Log.e("response", response);
                String email, stat = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
//                    getAllDataFromJSON(jsonObject);
                    stat = jsonObject.getString("stat").toString();
//                    email = jsonObject.getString("email").toString();
                    Log.e("stat",stat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("stat",stat);
                dataList = new ArrayList<>();

                if(stat.contains("nodata")) {
                    status = StatusReg.NODATA;
                }
                else if(stat.contains("false")) {
                    status = StatusReg.FALSE;
                }
                else if(stat.contains("true")){
                    status = StatusReg.TRUE;
                }
                else{
                    status = StatusReg.NOTCONNECTED;
                }
                tvnodata.setVisibility(View.GONE);
                if(status == StatusReg.NODATA) {
                    tvnodata.setVisibility(View.VISIBLE);
                }
                else if (status == StatusReg.TRUE) {
//                    Toast.makeText(ForgottenPassword.this,json,Toast.LENGTH_SHORT).show();
//                    EditText.setText(json);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenPassword.this);
//                    builder.setTitle(json);
//                    builder.setMessage(json);
//                    builder.create().show();
//                    Intent intent = new Intent(LoginActivity.this,ResetPassword.class);
//                    intent.putExtra("email",etloginemail.getText().toString());
//                    startActivity(intent);

//                    Toast.makeText(HomeActivity.this,"Registered "+email+" :).",Toast.LENGTH_SHORT).show();
                    getAllDataFromJSON(jsonObject);
                    updatelist();
//                    Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                } else if(status == HomeActivity.StatusReg.FALSE){
                    Toast.makeText(HomeActivity.this,"Please try again :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(HomeActivity.this,"Please Enable INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                JSONObject jsonObject = jsonBuilderIsHere(key);
                Log.e("doinback","before");
                String result = loginuser.sendPostRequest(REGISTER_URL, jsonObject);
                Log.e("doinback","after");
                return result;
            }

        }

        GetAllData ru = new GetAllData();
        ru.execute(key);
    }

    private void getAllDataFromJSON(JSONObject response) {
        alldataList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            JSONArray jArray = response.getJSONArray("alldata");
            Log.e("data length!",""+jArray.length());
            for(int i=0;i<jArray.length();i++) {
                //Log.e("Title","Test");
                JSONObject json_data = jArray.getJSONObject(i);
                OneDataClass oneDataClass = new OneDataClass();
//                Log.e("Title","Test2  "+json_data.getString("title"));
                oneDataClass.dataid = Integer.parseInt(json_data.getString("id"));
                oneDataClass.datatitle = aesEncryption.decryptText(json_data.getString("title"));
                oneDataClass.datausername = aesEncryption.decryptText(json_data.getString("username"));
                oneDataClass.datapassword = aesEncryption.decryptText(json_data.getString("password"));
                oneDataClass.datawebsites = aesEncryption.decryptText(json_data.getString("website"));
                oneDataClass.datanotes = aesEncryption.decryptText(json_data.getString("notes"));
                ListDataModal listDataModal = new ListDataModal(oneDataClass.dataid,oneDataClass.datatitle,oneDataClass.datawebsites);
                Log.e("Title","Test3");
                alldataList.add(oneDataClass);
                dataList.add(listDataModal);
            }
            SharedPreferences mPrefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(alldataList);
            prefsEditor.putString("alldatalist", json);
            prefsEditor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JSONObject jsonBuilderIsHere(String key) {
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("emailkey", key);
           // jsonObject.accumulate("password", password);
            return jsonObject;
        } catch (JSONException e) {
            Log.e("Nish", "Can't format JSON!");
        }
        return null;
    }


    protected void parseJSON(String json){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
//                password = jsonObject.getString("password");
//            event2 = jsonObject.getString("event2");
//            event3 = jsonObject.getString("event3");
//            event4 = jsonObject.getString("event4");
//                 = jo.getString("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getData(aesEncryption.doEncryptionAES(sessionemail));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

   /*
        //To get data back as Object from SharedPrefrences
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        MyObject obj = gson.fromJson(json, MyObject.class);

    */
}

