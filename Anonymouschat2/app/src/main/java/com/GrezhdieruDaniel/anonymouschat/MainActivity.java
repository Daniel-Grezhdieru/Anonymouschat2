package com.GrezhdieruDaniel.anonymouschat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int MAX_LENGTH = 120;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    EditText mEditTextMessage;
    Button mSendButton;
    RecyclerView mMessagesRecycler;
    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendButton=findViewById(R.id.send_message_b);
        mEditTextMessage=findViewById(R.id.message_input);
        mMessagesRecycler=findViewById(R.id.message_recycler);

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this,messages);
        mMessagesRecycler.setAdapter(dataAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditTextMessage.getText().toString();
                if (msg.equals("")){
                    Toast.makeText(getApplicationContext(),"Введите сообщение",Toast.LENGTH_LONG).show();
                    return;
                }
                if (msg.equals(" ")){
                    Toast.makeText(getApplicationContext(),"Введите сообщение",Toast.LENGTH_LONG).show();
                    return;
                }
                if(msg.length()>MAX_LENGTH){
                    Toast.makeText(getApplicationContext(),"Сообщение превышает максимальную длину в 120 символов",Toast.LENGTH_LONG).show();
                    return;
                }
                myRef.push().setValue(msg);
                mEditTextMessage.setText("");
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
