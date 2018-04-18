package com.diot.safepasswords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class DetailsActivity extends AppCompatActivity {

    //bydefault password is hidden
    boolean showPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //edit fab
        FloatingActionButton fab = findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, UpdateActivity.class);
                startActivity(intent);
            }
        });


    initializeVars();   //to toggle password visibility
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
