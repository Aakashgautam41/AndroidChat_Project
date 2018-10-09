package com.example.aakash.chatapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private final Activity myActivity;
    private final DatabaseReference myDatabaseRef;
    private final String myUsername;
    private final ArrayList<DataSnapshot> mySnapshot;

    //Child event listener
    private final ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mySnapshot.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //Constructor
    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
        myActivity = activity;
        myUsername = name;
        myDatabaseRef = ref.child("chats");
        mySnapshot = new ArrayList<>();

        //add listener
        myDatabaseRef.addChildEventListener(mListener);

    }

    //Static class

    static class ViewHolder{
        TextView senderName;
        TextView chatBody;
        LinearLayout.LayoutParams layoutParams;
    }



    @Override
    public int getCount() {
        return mySnapshot.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = mySnapshot.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){

            LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_single_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.senderName = (TextView) view.findViewById(R.id.sender);
            holder.chatBody = (TextView) view.findViewById(R.id.message);
            holder.layoutParams = (LinearLayout.LayoutParams) holder.senderName.getLayoutParams();

            view.setTag(holder);


        }

        final InstantMessage message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        //Styling
        boolean isMe = message.getSender().equals(myUsername);
        //call a function for styling
        chatRowStyling(isMe, holder);

        String author = message.getSender();
        holder.senderName.setText(author);

        String msg = message.getMessage();
        holder.chatBody.setText(msg);


        return view;
    }

    private void chatRowStyling(boolean isItme, ViewHolder holder){
        if (isItme){
            holder.layoutParams.gravity = Gravity.END;
            holder.senderName.setTextColor(Color.BLUE);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_green);
        } else {
            holder.layoutParams.gravity = Gravity.START;
            holder.senderName.setTextColor(Color.GREEN);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_orange);


        }

        //Forgot this setUp
        holder.senderName.setLayoutParams(holder.layoutParams);
        holder.chatBody.setLayoutParams(holder.layoutParams);
    }

    public void freeUpResources(){
        myDatabaseRef.removeEventListener(mListener);
    }
}
