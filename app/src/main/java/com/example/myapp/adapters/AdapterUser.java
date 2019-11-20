package com.example.myapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.ChatActivity;
import com.example.myapp.models.ModelUsers;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.myholder> {

Context context;
List<ModelUsers>userList;

    public AdapterUser(Context context, List<ModelUsers> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);
        return new myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int i) {
        final String hisuid=userList.get(i).getUid();
        String userImage = userList.get(i).getImage();
        String username = userList.get(i).getName();
        final String useremail = userList.get(i).getEmail();
        //
        holder.mnametv.setText(username);
        holder.memailtv.setText(useremail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.mavatariv);

        }catch (Exception e){

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(context, ChatActivity.class);
                i.putExtra("hisuid",hisuid);
                context.startActivity(i);

             //   Toast.makeText(context,""+useremail,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class
    class myholder extends RecyclerView.ViewHolder
    {
    ImageView mavatariv;
    TextView mnametv,memailtv;


        public myholder(@NonNull View itemView) {
            super(itemView);

            mavatariv=itemView.findViewById(R.id.avatariv);
            mnametv=itemView.findViewById(R.id.nametv);
            memailtv=itemView.findViewById(R.id.emailtv);
        }


    }
}
