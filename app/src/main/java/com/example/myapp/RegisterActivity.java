package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity<TAG> extends AppCompatActivity {
    EditText memailet,mpasswordet;
    Button mregisterbtn;
    TextView mhaveaccounttv;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ACTIONBAR AND ITS TITLE
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //
        memailet=findViewById(R.id.emailid);
        mpasswordet=findViewById(R.id.passwordid);
        mregisterbtn=findViewById(R.id.registerbtn);
        mhaveaccounttv=findViewById(R.id.have_account);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=memailet.getText().toString().trim();
                String password=mpasswordet.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    memailet.setError("Invalid Email");
                    memailet.setFocusable(true);
                }
                else if(password.length()<6)
                {
                    //seterror
                    mpasswordet.setError("Password length atleast 6 characters");
                    mpasswordet.setFocusable(true);

                }
                else
                {
                    registeruser(email,password);
                }

            }
        });

        pd=new ProgressDialog(this);
        pd.setMessage("Registering..");

        //handle login textview click listner
        mhaveaccounttv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });




    }

    private void registeruser(String email, String password) {
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success,
                            FirebaseUser user=mAuth.getCurrentUser();


                            String email=user.getEmail();
                            String uid=user.getUid();
                            //when user is registered stre user infoin firabase database too
                            HashMap<Object,String>hashmap=new HashMap<>();
                            //put info in hashmap
                            hashmap.put("email",email);
                            hashmap.put("uid",uid);
                            //will add later in edit profile

                            hashmap.put("name","");
                            hashmap.put("phone","");
                            hashmap.put("image","");
                            //
                            FirebaseDatabase database=FirebaseDatabase.getInstance();;
                            //path ti store user data named "users"
                            DatabaseReference reference=database.getReference("Users");
                            //put data with hashmap in database
                            reference.child(uid).setValue(hashmap);





                            Toast.makeText(RegisterActivity.this, "Registered....\n"+user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();


                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                    }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}

