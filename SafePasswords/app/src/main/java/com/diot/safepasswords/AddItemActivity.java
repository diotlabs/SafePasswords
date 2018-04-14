package com.diot.safepasswords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etusername, etwebsite, etpassword, etnotes;
    Button bsavedata;

    public static final String MyPREFERENCES = "Session" ;
    public static String sessionemail = "";
    final String REGISTER_URL = "http://scholarshealthservices.com/safepasswordapplication/submitdata.php";
    String json;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_save_data:
                AESEncryption aesEncryption = new AESEncryption(sessionemail);
                String username = etusername.getText().toString();
                String website = etwebsite.getText().toString();
                String password = etpassword.getText().toString();
                String notes = etnotes.getText().toString();
                if(username.length()!=0 && website.length() != 0 && password.length() != 0 && notes.length() != 0){
                    try {
                        submitalldata(aesEncryption.doEncryptionAES(sessionemail),aesEncryption.doEncryptionAES(username),aesEncryption.doEncryptionAES(website),aesEncryption.doEncryptionAES(password),aesEncryption.doEncryptionAES(notes));
                        //submitalldata(username,website,password,notes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(username.length()!=0 && website.length() != 0 && password.length() != 0){
                    try {
                        submitalldata(aesEncryption.doEncryptionAES(sessionemail),aesEncryption.doEncryptionAES(username),aesEncryption.doEncryptionAES(website),aesEncryption.doEncryptionAES(password),aesEncryption.doEncryptionAES("NA"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    enum StatusReg
    {
        TRUE,FALSE,NOTCONNECTED
    }
    StatusReg status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        sessionemail = sharedpreferences.getString("sessionemail","null");

        initialize();
        bsavedata.setOnClickListener(this);
    }

    private void initialize() {
        etusername = findViewById(R.id.editText_username);
        etpassword = findViewById(R.id.editText_password);
        etwebsite = findViewById(R.id.editText_website);
        etnotes = findViewById(R.id.editText_notes);
        bsavedata = findViewById(R.id.button_save_data);
    }

    private void submitalldata(final String email, final String username, final String website, final String password, final String notes) {
        class SubmitData extends AsyncTask<String, Void, String> {
            ProgressDialog loading = null;
            ConnectClass loginuser = new ConnectClass();
            //            EditText EditText = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                EditText = (EditText) findViewById(R.id.);
                loading = ProgressDialog.show(AddItemActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
//                if(loading != null && loading.isShowing()){ loading.dismiss();}
                json = new String(response);
                response.trim();
                Log.e("response", response);

//                if (response.contains("true") && response.contains("emailexists")) {
//                    status = AddItemActivity.StatusReg.ALREADY;
//                } else
                if(response.contains("false")) {
                    status = AddItemActivity.StatusReg.FALSE;
                }
                else if(response.contains("true")){
                    status = AddItemActivity.StatusReg.TRUE;
                }
                else{
                    status = AddItemActivity.StatusReg.NOTCONNECTED;
                }

//                if(status == AddItemActivity.StatusReg.ALREADY){
//                    Toast.makeText(AddItemActivity.this,"Email already registered!",Toast.LENGTH_SHORT).show();
////                    etregemail.requestFocus();
//                }
//                else

                    if (status == AddItemActivity.StatusReg.TRUE) {
//                    Toast.makeText(ForgottenPassword.this,json,Toast.LENGTH_SHORT).show();
////                    EditText.setText(json);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenPassword.this);
//                    builder.setTitle(json);
////                    builder.setMessage(json);
//                    builder.create().show();
//                    Intent intent = new Intent(LoginActivity.this,ResetPassword.class);
//                    intent.putExtra("email",etloginemail.getText().toString());
//                    startActivity(intent);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String email = jsonObject.getString("email");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AddItemActivity.this,"Data added :)",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddItemActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if(status == AddItemActivity.StatusReg.FALSE){
                    Toast.makeText(AddItemActivity.this,"Please try again :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddItemActivity.this,"Please Enable INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                JSONObject jsonObject = jsonBuilderIsHere(email,username,website,password,notes);
                Log.e("doinback","before");
                String result = loginuser.sendPostRequest(REGISTER_URL, jsonObject);
                Log.e("doinback","after");
                return result;
            }

        }

        SubmitData ru = new SubmitData();
        ru.execute(username,website,password,notes);
    }



    private JSONObject jsonBuilderIsHere(String email, String username, String website, String password, String notes) {
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        try {

            jsonObject.accumulate("email", email);
            jsonObject.accumulate("username", username);
            jsonObject.accumulate("website", website);
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("notes", notes);
            return jsonObject;
        } catch (JSONException e) {
            Log.e("Nish", "Can't format JSON!");
        }
        return null;
    }


}
