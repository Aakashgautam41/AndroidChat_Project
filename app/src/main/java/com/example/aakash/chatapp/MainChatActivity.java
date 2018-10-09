package com.example.aakash.chatapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainChatActivity extends AppCompatActivity {
    private String myUserName;
    private ListView chatListView;
    private EditText chatText;
    private DatabaseReference myDatabaseRef;

    ChatListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        //Calling function to setup username
        setUpDisplayName();

        //Getting instance of database
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();

        //Getting ui elements references
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatText = (EditText) findViewById(R.id.message_input);
        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);

        //Push chat to firebase
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushChatToFirebase();
            }
        });

    }

    //Sending chat to firebase
    private void pushChatToFirebase(){
        String chatInput = chatText.getText().toString();
        if (!chatInput.equals("")){
            InstantMessage chat = new InstantMessage(myUserName, chatInput);
            myDatabaseRef.child("chats").push().setValue(chat);
            chatText.setText("");
        }
    }

    //Set userName for user
    private void setUpDisplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREF, MODE_PRIVATE);
        myUserName = prefs.getString(RegisterActivity.DISPLAY_NAME, null);

        if(myUserName == null){
            myUserName = "user";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChatListAdapter myAdapter = new ChatListAdapter(MainChatActivity.this, myDatabaseRef, myUserName);
        chatListView.setAdapter(myAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.freeUpResources();
    }
}
