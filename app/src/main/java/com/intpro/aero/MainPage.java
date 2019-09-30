package com.intpro.aero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * In this page mainly focus on user authentication by compare stored values in database
 * also user have optional actions to reset password or create new account
 *
 * Author : Sarani_Hansamali
 */


public class MainPage extends AppCompatActivity {

    public static String USER_ID = "";
    EditText indexNumber, password;
    Button signIn, createAccount;
    TextView resetPassword;

    DBHelper dbHelper;


    //animation pack
    TextView heading, logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        indexNumber = findViewById(R.id.index);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        resetPassword = findViewById(R.id.forgotPasswordOption);
        createAccount = findViewById(R.id.createAccountOption);

        heading = findViewById(R.id.title);
        logoImage = findViewById(R.id.cap);

        Timer timer;

        dbHelper = new DBHelper(this);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.fade);

        logoImage.setAnimation(animation1);
        heading.setAnimation(animation2);


        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String id = indexNumber.getText().toString().trim();
                String pass = password.getText().toString().trim();
                Boolean res = dbHelper.checkUser(id, pass);

                if(id.isEmpty())
                {
                    indexNumber.setError("Index cannot be empty");

                }

                else if(pass.isEmpty())
                {
                    password.setError("Password cannot be empty");
                }


                if (res==true)
                {
                    USER_ID = id;
                    Toast.makeText(MainPage.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainPage.this, Profile.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(MainPage.this,"Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }


            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

}
