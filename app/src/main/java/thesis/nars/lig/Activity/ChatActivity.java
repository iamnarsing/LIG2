package thesis.nars.lig.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import thesis.nars.lig.Adapter.MessageListAdapter;
import thesis.nars.lig.Model.MessageModel;
import thesis.nars.lig.R;

/**
 * Created by nars on 12/16/2019.
 */

public class ChatActivity extends AppCompatActivity{

    ListView messageListView;
    Button sendBtn;
    ArrayList<MessageModel> messageList;
    MessageListAdapter adapter;
    private String current_user_name;
    FirebaseAuth auth;
    FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private String userID;
    private FirebaseDatabase rootNode;
    private DatabaseReference conversationNode;
    private DatabaseReference convoNode;
    private EditText messageEdt;
    private String userName;
    private String senderName;
    private String messageText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        userID = getIntent().getStringExtra("userID");
        userName = getIntent().getStringExtra("senderName");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        convoNode = FirebaseDatabase.getInstance().getReference("conversationData").child("chatroom");
        rootNode = FirebaseDatabase.getInstance();
        messageEdt = (EditText) findViewById(R.id.message_Edt);
        messageListView = (ListView) findViewById(R.id.messageList);

        sendBtn = (Button) findViewById(R.id.send_message_Btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageEdt.getText().toString().trim().isEmpty()){
                    MessageModel messageModel = new MessageModel();
                    messageModel.setSenderID(userID);
                    messageModel.setSenderName(userName);
//                    messageModel.setIsActive(isActive);
                   // messageModel.setMessageType("Text");
                    messageModel.setMessageTxt(messageEdt.getText().toString());
                    //messageModel.setUrl("");
                    sendMessage(messageModel);
                    messageEdt.setText("");
                }
            }
        });
        messageList = new ArrayList<MessageModel>();
        adapter = new MessageListAdapter(this,101,messageList,userID,senderName);
        messageListView.setAdapter(adapter);
        GetMessagesData();
    }

    private void sendMessage(MessageModel messageModel) {
        convoNode.push().setValue(messageModel);
    }

    private void GetMessagesData() {
        rootNode = FirebaseDatabase.getInstance();
        convoNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                messageList.add(messageModel);
                adapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
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
        });
    }

    private void scrollMyListViewToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messageListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public void signOut(View view) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            auth.signOut();
            finish();
            Intent intent = new Intent(ChatActivity.this, SignupActivity.class);
            startActivity(intent);

        }
    }
}
