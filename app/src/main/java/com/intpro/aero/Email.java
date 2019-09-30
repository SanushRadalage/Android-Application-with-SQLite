package com.intpro.aero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static com.intpro.aero.Profile.resultUriProfile;

public class Email extends AppCompatActivity {

    DBHelper dbHelper;

    TextView id, name, mail, contact, gpa;
    EditText receiverMail, subjct, mssage;
    Button send;
    ImageView profileimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        id = findViewById(R.id.profileIndex);
        name = findViewById(R.id.profileName);
        mail = findViewById(R.id.profileMail);
        contact = findViewById(R.id.profileContact);
        gpa = findViewById(R.id.profileGpa);
        receiverMail = findViewById(R.id.ReceiverMail);
        subjct = findViewById(R.id.subject);
        mssage = findViewById(R.id.message);
        profileimg = findViewById(R.id.profilePic);

        send = findViewById(R.id.sendMail);

        dbHelper = new DBHelper(this);


        //Glide.with(Email.this).load(resultUriProfile).into(profileimg);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        String[] s = dbHelper.readUser(getApplicationContext(), MainPage.USER_ID);
        id.setText(s[0]);
        name.setText(s[1]);
        mail.setText(s[2]);
        contact.setText(s[3]);
        gpa.setText(s[4]);

        profileimg.setImageBitmap(dbHelper.getProfileImage(getApplicationContext(), MainPage.USER_ID));

    }

    private void sendEmail(){
        String recipientList= receiverMail.getText().toString();
        String[] recipients =recipientList.split(",");

        String sub= subjct.getText().toString();
        String msg= mssage.getText().toString();
        String ind= id.getText().toString();
        String nm= name.getText().toString();
        String em= mail.getText().toString();
        String ph= contact.getText().toString();
        String gp= gpa.getText().toString();

        if(recipientList.isEmpty())
        {
            Toast.makeText(Email.this, "Please enter receiver's Email address", Toast.LENGTH_SHORT).show();
        }
        else
        {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,sub);

            String s = msg +"\n\n" + "Index No :"+ind+"\nName : " + nm + "\nEmail : " + em + "\nPhone No : " + ph + "\nGPA : "+ gp;

            intent.putExtra(Intent.EXTRA_TEXT,s);


            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose and Email client"));
        }


    }


}
