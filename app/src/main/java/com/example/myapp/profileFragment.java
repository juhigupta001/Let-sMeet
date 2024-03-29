package com.example.myapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {
FirebaseAuth firebaseAuth;
FirebaseUser user;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
//
    ImageView avatar;
    TextView nametv,emailtv,phonetv;

    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_profile,container,false);


        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        //
        avatar=view.findViewById(R.id.avatartv);
        nametv=view.findViewById(R.id.nametv);
        phonetv=view.findViewById(R.id.phonetv);
        emailtv=view.findViewById(R.id.emailtv);
        //
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    //get data
                    String name=""+ds.child("name").getValue();
                    String email=""+ds.child("email").getValue();
                    String phone=""+ds.child("phone").getValue();
                    String image=""+ds.child("image").getValue();
                    //set data
                    emailtv.setText(email);
                    nametv.setText(name);
                    phonetv.setText(phone);
                    try
                    {
                        Picasso.get().load(image).into(avatar);
                    }catch (Exception e)
                    {

                        Picasso.get().load(R.drawable.ic_camera_black).into(avatar);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("tag2","inside");
        return view;

    }

}
