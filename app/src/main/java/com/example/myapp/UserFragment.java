package com.example.myapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.myapp.adapters.AdapterUser;
import com.example.myapp.models.ModelUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    RecyclerView recyclerView;

AdapterUser adapterUser;
List<ModelUsers>usersList;
    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_user, container, false);
       //

       //
       recyclerView=v.findViewById(R.id.users_recycleview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //
        usersList=new ArrayList<>();
        //
        getAllUsers();


       return v;
    }

    private void getAllUsers() {
        //get current users
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        //
        //get path of database named "users"
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){

               ModelUsers modelUser=ds.getValue(ModelUsers.class);
                    //get all users except currently signed in user
                    if(!modelUser.getUid().equals(fuser.getUid()))
                    {
                        usersList.add(modelUser);
                    }
                    adapterUser=new AdapterUser(getActivity(),usersList);
                    //
                    recyclerView.setAdapter(adapterUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //inflate options menu

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);//to show menu
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //search view
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //if search query is not empty
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchUsers(s);
                }
                else
                {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //if search query is not empty
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchUsers(s);
                }
                else
                {
                    getAllUsers();
                }
                return false;
            }
        });

         super.onCreateOptionsMenu(menu,inflater);
    }


    private void searchUsers(final String query) {
        //get current users
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        //
        //get path of database named "users"
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    ModelUsers modelUser=ds.getValue(ModelUsers.class);
                    //get all searchusers except currently signed in user
                    if(!modelUser.getUid().equals(fuser.getUid()))
                    {
                        if(modelUser.getName().toLowerCase().contains(query.toLowerCase())
                                ||modelUser.getEmail().toLowerCase().contains(query.toLowerCase() ))
                        {
                            usersList.add(modelUser);
                        }
                    }
                    adapterUser=new AdapterUser(getActivity(),usersList);
                    //refresh
                    adapterUser.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //handle menu item clicks




}
