package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    TextView noaccounttv;
    EditText memailet,mpasswordet;
    Button mloginbtn;
    ProgressDialog pd;
    //
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "login1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        Log.d("tag","loginn2");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Login");
        //
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //
        noaccounttv=findViewById(R.id.no_account);
        memailet=findViewById(R.id.emailid);
        mpasswordet=findViewById(R.id.passwordid);
        mloginbtn=findViewById(R.id.loginbtn);

        // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();



        //
        noaccounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        //
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input data
                String email=memailet.getText().toString().trim();
                String password=mpasswordet.getText().toString().trim();
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
                    loginuser(email,password);
                }


            }
        });

pd=new ProgressDialog(this);
pd.setMessage("Logging  In..");
    }

    private void loginuser(String email, String password) {
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                //get email and uid
                                String email = user.getEmail();
                                String uid = user.getUid();
                                //when user is registered stre user infoin firabase database too
                                HashMap<Object, String> hashmap = new HashMap<>();
                                //put info in hashmap
                                hashmap.put("email", email);
                                hashmap.put("uid", uid);
                                //will add later in edit profile

                                hashmap.put("name", "");
                                hashmap.put("phone", "");
                                hashmap.put("image", "");
                                //
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                ;
                                //path ti store user data named "users"
                                DatabaseReference reference = database.getReference("Users");
                                //put data with hashmap in database
                                reference.child(uid).setValue(hashmap);
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();

                            }
                            else
                            {
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            }

                        }else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                //error
                Toast.makeText(LoginActivity.this, ""+e.getMessage(),
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
