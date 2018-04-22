package com.diot.safepasswords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    //bydefault password is hidden
    boolean showPass;
    TextView tvtitle, tvwebsite, tvusername, tvpassword, tvnotes;
    OneDataClass oneDataClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        oneDataClass = (OneDataClass) intent.getSerializableExtra("OneDataClass");
        //edit fab
        FloatingActionButton fab = findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, UpdateActivity.class);
                intent.putExtra("OneDataClass",oneDataClass);
                startActivity(intent);
                finish();
            }
        });

    initialize();
    setAllData(oneDataClass.datatitle, oneDataClass.datawebsites,oneDataClass.datausername,oneDataClass
    .datapassword,oneDataClass.datanotes);
    initializeVars();   //to toggle password visibility
    }

    private void setAllData(String datatitle, String datawebsites, String datausername, String datapassword, String datanotes) {
        tvtitle.setText(datatitle);
        tvwebsite.setText(datawebsites);
        tvusername.setText(datausername);
        tvpassword.setText(datapassword);
        tvnotes.setText(datanotes);
    }

    private void initialize() {
        tvtitle = findViewById(R.id.textView_details_title);
        tvwebsite = findViewById(R.id.textView_details_website);
        tvusername = findViewById(R.id.textView_details_username);
        tvpassword = findViewById(R.id.textView_details_password);
        tvnotes = findViewById(R.id.textView_details_notes);
    }

    public  void initializeVars(){
        //bydefault password is hidden
         showPass = true;
    }
    //toggle password view
    public void togglePassword(View view){

        ImageButton eye = findViewById(R.id.imageButton_eye);
        EditText edittext_pass = findViewById(R.id.textView_details_password);
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
