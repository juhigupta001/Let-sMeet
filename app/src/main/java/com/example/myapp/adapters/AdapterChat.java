package com.example.myapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends  RecyclerView.Adapter<AdapterChat.myholder> {
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<ModelChat>chatList;
    String imageurl;
    FirebaseUser fuser;


    public AdapterChat(Context context, List<ModelChat> chatList, String imageurl) {
        this.context = context;
        this.chatList = chatList;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //inglate chats

if(i==MSG_TYPE_LEFT){
    View v= LayoutInflater.from(context).inflate(R.layout.new_chat_right,parent,false);
    return new myholder(v);

}
else
{
    View v= LayoutInflater.from(context).inflate(R.layout.new_chat_left,parent,false);
    return new myholder(v);
}



    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int i) {
        String message=chatList.get(i).getMessage();
        String timestamp=chatList.get(i).getTimestamp();
        //convert time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
      //  cal.setTimeInMillis(Long.parseLong(timestamp));
       // String datetime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
        //set data
        holder.messagetv.setText(message);
       // holder.timetv.setText(datetime);
        try{
            Picasso.get().load(imageurl).into(holder.profilriv);

        }catch (Exception e){

        }
        //set seen /delivered statusof mesage
        if(i==chatList.size()-1) {
                    if(chatList.get(i).isIsseen())
            holder.isseentv.setText("Seen");

        else
            {
                holder.isseentv.setText("Delivered");
            }
        }
        else
        {
            holder.isseentv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fuser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return  MSG_TYPE_LEFT;


    }

//view holder class


    class myholder extends RecyclerView.ViewHolder
    {

        ImageView profilriv;
        TextView messagetv,timetv,isseentv;

        public myholder(@NonNull View itemView) {
            super(itemView);
            profilriv=itemView.findViewById(R.id.profileiv);
            messagetv=itemView.findViewById(R.id.messagetv);
            timetv=itemView.findViewById(R.id.timetv);
            isseentv=itemView.findViewById(R.id.isseentv);



        }
    }

}
