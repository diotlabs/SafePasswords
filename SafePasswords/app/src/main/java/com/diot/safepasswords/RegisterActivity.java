package com.diot.safepasswords;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etregemail, etregpassword, etregpasswordconfirm;
    Button bregisteruser;
    String json;
    final String REGISTER_URL = "http://scholarshealthservices.com/safepasswordapplication/registeruser.php";
    enum StatusReg{
        TRUE,FALSE,NOTCONNECTED,ALREADY
    }
    StatusReg status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initiallize();
        bregisteruser.setOnClickListener(this);
    }

    private void initiallize() {
        etregemail = findViewById(R.id.register_email);
        etregpassword = findViewById(R.id.register_password);
        etregpasswordconfirm = findViewById(R.id.confirm_register_password);
        bregisteruser = findViewById(R.id.button_do_register);
    }

    //Go to login page
    public void goToLogin(View view)
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_do_register:
                String email = etregemail.getText().toString().trim();
                String pass = etregpassword.getText().toString().trim();
                String passconf = etregpasswordconfirm.getText().toString().trim();
                if(!(email.contains("@") && email.contains(".com"))){
                    Toast.makeText(this,"Not a valid email!",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6){
                    Toast.makeText(this,"Too short password.\nMin 6 characters!",Toast.LENGTH_LONG).show();
                }
                else if(pass.equals(passconf)){
                    registerUser(email,MD5.getMD5(pass+email).trim());
                }
                else{
                    Toast.makeText(this,"Confirm password did'nt matched!",Toast.LENGTH_SHORT).show();
                    etregpassword.setText("");
                    etregpasswordconfirm.setText("");
                    etregpassword.requestFocus();
                }
                break;
        }
    }

    private void registerUser(final String email, final String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading = null;
            ConnectClass loginuser = new ConnectClass();
            //            EditText EditText = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                EditText = (EditText) findViewById(R.id.);
                loading = ProgressDialog.show(RegisterActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
//                if(loading != null && loading.isShowing()){ loading.dismiss();}
                json = new String(response);
                response.trim();
                Log.e("response", response);

                if (response.contains("true") && response.contains("emailexists")) {
                    status = StatusReg.ALREADY;
                } else if(response.contains("false")) {
                    status = StatusReg.FALSE;
                }
                else if(response.contains("true")){
                    status = StatusReg.TRUE;
                }
                else{
                    status = StatusReg.NOTCONNECTED;
                }
                if(status == StatusReg.ALREADY){
                    Toast.makeText(RegisterActivity.this,"Email already registered!",Toast.LENGTH_SHORT).show();
                    etregemail.requestFocus();
                }
                else if (status == StatusReg.TRUE) {
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
                    Toast.makeText(RegisterActivity.this,"Registered "+email+" :).",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if(status == StatusReg.FALSE){
                    Toast.makeText(RegisterActivity.this,"Please try again :(",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Please Enable INTERNET!", Toast.LENGTH_SHORT).show();
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
