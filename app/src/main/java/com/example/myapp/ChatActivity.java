package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.LinkAddress;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.adapters.AdapterChat;
import com.example.myapp.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
Toolbar toolbar;
RecyclerView recyclerView;
ImageView profileiv;
TextView nametv,userstatustv;
EditText messageet;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userdbrefernce;

    //for chehcking if use has sent mesage or not
    ValueEventListener seenListner;
    DatabaseReference userReferenceForSeen;
    List<ModelChat>chatList;
    AdapterChat adapterChat;




    String hisuid;
    String myuid;
    String hisImage;
ImageButton sendbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        //
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chat");
        recyclerView=findViewById(R.id.chat_recyclerview);
        profileiv=findViewById(R.id.profiletv);
        nametv=findViewById(R.id.nametv);
        userstatustv=findViewById(R.id.userstatus);
        messageet=findViewById(R.id.messages);
        sendbtn=findViewById(R.id.sendbtn);
        //layout for recycler view
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recyclerview prpperties

        recyclerView.setLayoutManager(linearLayoutManager);
        //receive the uid passed through intent from useradapteres
        Intent i=getIntent();
        hisuid=i.getStringExtra("hisuid");

        //
        userdbrefernce=firebaseDatabase.getReference("Users");
        //search user to get user info
        Query userquery=userdbrefernce.orderByChild("uid").equalTo(hisuid);
        userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String name=""+ds.child("name").getValue();
                    String hisimage=""+ds.child("image").getValue();
                    //set data
                    nametv.setText(name);
                    try{
                        Picasso.get().load(hisimage).placeholder(R.drawable.ic_default_img_white).into(profileiv);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_default_img_white).into(profileiv);
                    }

                }
                            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    //
        sendbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String message=messageet.getText().toString().trim();
                //check empty or not

                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(ChatActivity.this,"Cannot send empty mssage",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendmessage(message);
                }

            }
        });
        readMessages();
        seenMessages();


    }

    private void seenMessages() {
        userReferenceForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenListner=userReferenceForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myuid)&& chat.getSender().equals(hisuid))
                    {
                        HashMap<String,Object>hasSeenMap=new HashMap<>();
                        hasSeenMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasSeenMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList=new ArrayList<>();
        final DatabaseReference dref=FirebaseDatabase.getInstance().getReference("Chats");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if((chat.getReceiver().equals(myuid) && chat.getSender().equals(hisuid))||
                    (chat.getReceiver().equals(hisuid) && chat.getSender().equals(myuid))){
                        chatList.add(chat);

                    }

                    adapterChat=new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    //set adapter to recycleview
                    recyclerView.setAdapter(adapterChat);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendmessage(String message) {
        String timestamp=String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashmap=new HashMap<>();
        hashmap.put("sender",myuid);
        hashmap.put("receiver",hisuid);
        hashmap.put("message",message);
        hashmap.put("isseen",false);
        hashmap.put("timestamp",timestamp);

        databaseReference.child("Chats").push().setValue(hashmap);
        //reset ed after sending
        messageet.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //handle menu item clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkuserstatus()
    {
        //get current usre
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            //user is signed in
            //set emial of logged in usru
            myuid=user.getUid();

        }
        else
        {
            startActivity(new Intent(ChatActivity.this,MainActivity.class));
        }
    }


    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userReferenceForSeen.removeEventListener(seenListner);
    }
}
