package com.diot.safepasswords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etloginemail, etloginpassword;
    Button bdologin, bgotoregister;
    String json;
    final String REGISTER_URL = "http://scholarshealthservices.com/safepasswordapplication/authlogin.php";
    public static final String MyPREFERENCES = "Session" ;
    public static String sessionemail = "";
    SharedPreferences sharedpreferences;
    enum Status
    {
        TRUE,FALSE,NOTCONNECTED
    }
    Status status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiallize();
        bdologin.setOnClickListener(this);
//        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        sessionemail = sharedpreferences.getString("sessionemail","null");
//        if(!sessionemail.equals("null")){
//            doLogin();
//        }
//        Toast.makeText(this,""+sessionemail,Toast.LENGTH_SHORT).show();
    }

    private void intiallize() {
        etloginemail = findViewById(R.id.login_email);
        etloginpassword = findViewById(R.id.login_password);
        bdologin = findViewById(R.id.button_do_login);
        bgotoregister = findViewById(R.id.button_goto_register);
    }

    //Go to home & validate login
    public void doLogin()
    {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("sessionemail",sessionemail);
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    //Go to register
    public void goToRegister(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginUser(final String email, final String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading = null;
            LoginClass loginuser = new LoginClass();
//            TextView textView = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                textView = (TextView) findViewById(R.id.);
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
//                if(loading != null && loading.isShowing()){ loading.dismiss();}
                json = new String(response);
                response.trim();
                Log.e("response", response);

                if (response.contains("true")) {
                    status = LoginActivity.Status.TRUE;
                } else if(response.contains("false")) {
                    status = LoginActivity.Status.FALSE;
                }
                else{
                    status = LoginActivity.Status.NOTCONNECTED;
                }
                if (status == LoginActivity.Status.TRUE) {
//                    Toast.makeText(ForgottenPassword.this,json,Toast.LENGTH_SHORT).show();
////                    textView.setText(json);
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
                    sessionemail = email;
                   Toast.makeText(LoginActivity.this,"Hello "+email+" :).",Toast.LENGTH_SHORT).show();
                    doLogin();
                    finish();
                } else if(status == LoginActivity.Status.FALSE){
                    Toast.makeText(LoginActivity.this,"Incorrect Credentials :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Enable INTERNET!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                JSONObject jsonObject = jsonBuilderIsHere(email,password);
                Log.e("doinback","before");
                String result = loginuser.sendPostRequest(REGISTER_URL, jsonObject);
                Log.e("doinback","after");
                return result;
            }

        }

        RegisterUser ru = new RegisterUser();
        ru.execute(email);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_do_login:
                String email = etloginemail.getText().toString().trim();
                String password = etloginpassword.getText().toString().trim();
                if (email.contains("@") && email.contains(".com") && password.length() >= 5){
                    loginUser(email, MD5.getMD5(password+email));
                    Log.e("Status",""+status);
                }
                else{
                    Log.e("Here","else login");
                    Toast.makeText(this,"Wrong data format entered! :(",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private JSONObject jsonBuilderIsHere(String email, String password) {
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("email", email);
            jsonObject.accumulate("password", password);
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


}
