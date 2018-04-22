package com.diot.safepasswords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.lang.UProperty;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ettitle, etwebsite, etusername, etpassword, etnotes;
    Button bupdatedata;
    OneDataClass oneDataClass;
    String sessionemail;
    boolean showPass;

    String json;
    AESEncryption aesEncryption;
    final String REGISTER_URL = "http://scholarshealthservices.com/safepasswordapplication/updateonedata.php";
    enum StatusReg{
        TRUE,FALSE,NOTCONNECTED,NODATA
    }
    StatusReg status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent = getIntent();
        oneDataClass = (OneDataClass) intent.getSerializableExtra("OneDataClass");

        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        sessionemail = sharedpreferences.getString("sessionemail","null");
        aesEncryption = new AESEncryption(sessionemail);
        initializeVars();
        initialize();
        initializeAllValues();
    }

    private void initializeAllValues() {
        ettitle.setText(oneDataClass.datatitle);
        etwebsite.setText(oneDataClass.datawebsites);
        etusername.setText(oneDataClass.datausername);
        etpassword.setText(oneDataClass.datapassword);
        etnotes.setText(oneDataClass.datanotes);
        bupdatedata.setOnClickListener(this);
    }

    private void initialize() {
        ettitle = findViewById(R.id.editText_update_title);
        etwebsite = findViewById(R.id.editText_update_website);
        etusername = findViewById(R.id.editText_update_username);
        etpassword = findViewById(R.id.editText_update_password);
        etnotes = findViewById(R.id.editText_update_notes);
        bupdatedata = findViewById(R.id.button_update_data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_update_data:
                AESEncryption aesEncryption = new AESEncryption(sessionemail);
                if(ettitle.getText().length()==0) {
                    ettitle.requestFocus();
                    Toast.makeText(this,"Title cannot be empty!",Toast.LENGTH_SHORT).show();
                    break;
                }
                    if(etwebsite.getText().length()==0)
                        etwebsite.setText("NA");
                    if(etusername.getText().length()==0)
                        etusername.setText("NA");
                    if(etpassword.getText().length()==0)
                        etpassword.setText("NA");
                    if(etnotes.getText().length()==0)
                        etnotes.setText("NA");
                    try {
                        setOneData(oneDataClass.dataid,aesEncryption.doEncryptionAES(sessionemail),aesEncryption.doEncryptionAES(ettitle.getText().toString()),aesEncryption.doEncryptionAES(etwebsite.getText().toString()),aesEncryption.doEncryptionAES(etusername.getText().toString()),aesEncryption.doEncryptionAES(etpassword.getText().toString()),aesEncryption.doEncryptionAES(etnotes.getText().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
        }
    }


    private void setOneData(final int id,final String key,final String title, final String website, final String username, final String password,final String notes) {
        class SetOneData extends AsyncTask<String, Void, String> {
            ProgressDialog loading = null;
            ConnectClass loginuser = new ConnectClass();
            //            EditText EditText = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                EditText = (EditText) findViewById(R.id.);
                loading = ProgressDialog.show(UpdateActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
//                TextView tvnodata = findViewById(R.id.textView_nodatashow);
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
                    email = jsonObject.getString("email").toString();
                    Log.e("stat",stat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("stat",stat);
//                dataList = new ArrayList<>();
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
//                tvnodata.setVisibility(View.GONE);
                if(status == StatusReg.NODATA) {
//                    tvnodata.setVisibility(View.VISIBLE);
                    Toast.makeText(UpdateActivity.this,"Fatal Error!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateActivity.this,HomeActivity.class);
                    startActivity(intent);
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
                    Intent intent = new Intent(UpdateActivity.this,DetailsActivity.class);
                    intent.putExtra("OneDataClass",oneDataClass);
                    startActivity(intent);

                    finish();
                } else if(status == StatusReg.FALSE){
                    Toast.makeText(UpdateActivity.this,"Please try again :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UpdateActivity.this,"Please Enable INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                JSONObject jsonObject = jsonBuilderIsHere(id, key, title, website, username, password, notes);
                Log.e("doinback","before");
                String result = loginuser.sendPostRequest(REGISTER_URL, jsonObject);
                Log.e("doinback","after");
                return result;
            }

        }

        SetOneData ru = new SetOneData();
        ru.execute(key);
    }

    private void getAllDataFromJSON(JSONObject response) {
        try {
            JSONArray jArray = response.getJSONArray("onedata");
            Log.e("data length!", "" + jArray.length());
            for (int i = 0; i < jArray.length(); i++) {
                //Log.e("Title","Test");
                JSONObject json_data = jArray.getJSONObject(i);
//                OneDataClass oneDataClass = new OneDataClass();
//                Log.e("Title","Test2  "+json_data.getString("title"));
                oneDataClass.dataid = Integer.parseInt(json_data.getString("id"));
                oneDataClass.datatitle = aesEncryption.decryptText(json_data.getString("title"));
                oneDataClass.datausername = aesEncryption.decryptText(json_data.getString("username"));
                oneDataClass.datapassword = aesEncryption.decryptText(json_data.getString("password"));
                oneDataClass.datawebsites = aesEncryption.decryptText(json_data.getString("website"));
                oneDataClass.datanotes = aesEncryption.decryptText(json_data.getString("notes"));
//                ListDataModal listDataModal = new ListDataModal(oneDataClass.dataid,oneDataClass.datatitle,oneDataClass.datawebsites);
                Log.e("Title", "Test3");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JSONObject jsonBuilderIsHere(int id, String key, String title, String website, String username, String password, String notes){
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("emailkey", key);
            jsonObject.accumulate("title", title);
            jsonObject.accumulate("website", website);
            jsonObject.accumulate("username", username);
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("notes", notes);
            // jsonObject.accumulate("password", password);
            return jsonObject;
        } catch (JSONException e) {
            Log.e("Nish", "Can't format JSON!");
        }
        return null;
    }


    public  void initializeVars(){
        //bydefault password is hidden
        showPass = true;
    }
    //toggle password view
    public void togglePassword(View view){

        ImageButton eye = findViewById(R.id.imageButton_eye);
        EditText edittext_pass = findViewById(R.id.editText_update_password);
        if(!showPass)
        {
            showPass = true;
            eye.setBackgroundResource(R.drawable.ic_remove_red_eye_grey_24dp);
            edittext_pass.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else {
            showPass = false;
            eye.setBackgroundResource(R.drawable.ic_remove_red_eye_pink_24dp);
            edittext_pass.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_NORMAL);
        }
    }


}

