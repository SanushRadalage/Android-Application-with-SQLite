package com.intpro.aero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity{

    /**
    * This is sign-up activity for users
    *
    * t1 to t6 is order of the column names in table users
    *
    */

    DBHelper dbHelper;

    Button signup;
    EditText id, name, mail, contact, gpa, password, repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        signup = findViewById(R.id.signUp);

        id = findViewById(R.id.t1);
        name = findViewById(R.id.t2);
        mail = findViewById(R.id.t3);
        contact = findViewById(R.id.t4);
        gpa = findViewById(R.id.t5);
        password = findViewById(R.id.t6);
        repass = findViewById(R.id.t7);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Index = id.getText().toString().trim();
                String Name = name.getText().toString().trim();
                String Email = mail.getText().toString().trim();
                String Phone = contact.getText().toString().trim();
                String Gpa =  gpa.getText().toString().trim();
                String Password =password.getText().toString().trim();
                String Repassword = repass.getText().toString().trim();


                if(Index.isEmpty())
                {
                    id.setError("Index cannot be empty");

                }
                else if(Name.isEmpty())
                {
                    name.setError("Name cannot be empty");
                }
                else if(Email.isEmpty())
                {
                    mail.setError("Email cannot be empty");
                }
                else if(Phone.isEmpty())
                {
                    contact.setError("Contact cannot be empty");
                }
                else if(Gpa.isEmpty())
                {
                    gpa.setError("Email cannot be empty");
                }
                else if(Password.isEmpty() || Repassword.isEmpty())
                {
                    password.setError("Password cannot be empty");
                    repass.setError("Password cannot be empty");
                }


                if(!Index.isEmpty() && !Name.isEmpty() && !Email.isEmpty() && !Phone.isEmpty() && !Gpa.isEmpty() && !Password.isEmpty())
                {

                    if(Password.equals(Repassword))
                    {
                        if(Password.length() >= 5)
                        {
                            long val = dbHelper.addUser(Index,Name,Email,Phone, Gpa,Password);

                            if(val>0)
                            {
                                Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, MainPage.class);
                                startActivity(intent);
                                id.setText(null);
                                name.setText(null);
                                mail.setText(null);
                                contact.setText(null);
                                gpa.setText(null);
                                password.setText(null);
                                repass.setText(null);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "Password must to be 5 characters or more!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });
    }
}
