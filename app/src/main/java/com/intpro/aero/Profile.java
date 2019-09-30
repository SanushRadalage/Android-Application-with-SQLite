package com.intpro.aero;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    public static String USER_ID = "";
    EditText id, name, mail, contact, gpa;
    ImageView coverPhoto;
    ImageView profilePhoto;
    FloatingActionButton quiz_button, mail_button, logout_button, option_button, update_button;

    public static Uri resultUriProfile;
    public static Uri resultUriCover;

    private String imageUrl;

    int requestCode_Profile = 1000;
    int requestCode_Cover = 2000;

    boolean isOpen = false;

    DBHelper dbHelper;

    Animation button_view, button_hide, clock_rotate, re_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DBHelper(this);

        id = findViewById(R.id.profileIndex);
        name = findViewById(R.id.profileName);
        mail = findViewById(R.id.profileMail);
        contact = findViewById(R.id.profileContact);
        gpa = findViewById(R.id.profileGpa);

        quiz_button = findViewById(R.id.quize);
        mail_button = findViewById(R.id.mailActivity);
        logout_button = findViewById(R.id.logout);
        option_button = findViewById(R.id.option);
        update_button = findViewById(R.id.update);

        coverPhoto = findViewById(R.id.coverPic);
        profilePhoto = findViewById(R.id.profilePic);

        button_view = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open);
        button_hide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close);
        clock_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clock_wise_rotate);
        re_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clock_wise_rerotate);

        USER_ID = MainPage.USER_ID;


        option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen)
                {
                    option_button.startAnimation(re_rotate);
                    mail_button.startAnimation(button_hide);
                    quiz_button.startAnimation(button_hide);
                    logout_button.startAnimation(button_hide);
                    update_button.startAnimation(button_hide);
                    mail_button.setClickable(false);
                    logout_button.setClickable(false);
                    quiz_button.setClickable(false);
                    update_button.setClickable(false);
                    isOpen = false;

                }
                else
                {
                    option_button.startAnimation(clock_rotate);
                    mail_button.startAnimation(button_view);
                    quiz_button.startAnimation(button_view);
                    logout_button.startAnimation(button_view);
                    update_button.startAnimation(button_view);
                    mail_button.setClickable(true);
                    logout_button.setClickable(true);
                    quiz_button.setClickable(true);
                    update_button.setClickable(true);
                    isOpen = true;
                }
            }
        });


        if (resultUriProfile != null)
        {
            Glide.with(Profile.this).load(resultUriProfile).into(profilePhoto);
        }
        else
        {

        }

        if (resultUriCover != null)
        {
            Glide.with(Profile.this).load(resultUriCover).into(coverPhoto);
        }

        ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, requestCode_Profile);
            }
        });

        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, requestCode_Cover);
            }
        });

        quiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ind = id.getText().toString();
                String nm = name.getText().toString();
                String ml = mail.getText().toString();
                String cn = contact.getText().toString();
                String gp = gpa.getText().toString();

                dbHelper.updateUser(getApplicationContext(),ind, nm, ml, cn, gp);
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        });



        String[] s = dbHelper.readUser(getApplicationContext(), MainPage.USER_ID);
        id.setText(s[0]);
        name.setText(s[1]);
        mail.setText(s[2]);
        contact.setText(s[3]);
        gpa.setText(s[4]);

        try
        {
            profilePhoto.setImageBitmap(dbHelper.getProfileImage(getApplicationContext(), MainPage.USER_ID));
            coverPhoto.setImageBitmap(dbHelper.getCoverImage(getApplicationContext(), MainPage.USER_ID));

        }catch (Exception i)
        {
            Toast.makeText(this, "Why you are not update a profile picture? ", Toast.LENGTH_SHORT).show();
        }


        mail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Email.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
        if (MainPage.USER_ID == null){
            startActivity(new Intent(Profile.this, MainPage.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUriProfile = data.getData();
            resultUriProfile = imageUriProfile;
            //Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
            Glide.with(Profile.this).load(resultUriProfile).into(profilePhoto);
            imageUrl = getPath(imageUriProfile);
        }
        else if (requestCode == 2000 && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUriCover = data.getData();
            resultUriCover = imageUriCover;
            Glide.with(Profile.this).load(resultUriCover).into(coverPhoto);
            imageUrl = getPath(imageUriCover);
        }
        else
        {
            Toast.makeText(Profile.this, "Oopz! Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

        if (resultUriProfile != null && requestCode == 1000)
        {
            //Toast.makeText(Profile.this, imageUrl, Toast.LENGTH_LONG).show();
            dbHelper.addProfileImages(getApplicationContext(), imageUrl, MainPage.USER_ID);
            Toast.makeText(Profile.this, "Saving profile photo.....", Toast.LENGTH_SHORT).show();
        }

        if(resultUriCover != null && requestCode == 2000)
        {
            //Toast.makeText(Profile.this, imageUrl, Toast.LENGTH_LONG).show();
            dbHelper.addCoverImages(getApplicationContext(), imageUrl, MainPage.USER_ID);
            Toast.makeText(Profile.this, "Saving cover photo.....", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


        public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor != null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

}
