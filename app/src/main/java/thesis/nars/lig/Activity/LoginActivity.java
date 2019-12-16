package thesis.nars.lig.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import thesis.nars.lig.Model.GroupDataModel;
import thesis.nars.lig.R;

/**
 * Created by nars on 12/14/2019.
 */

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

    EditText e1,e2;
    TextView er1,er2;
    Button loginBtn;
    Map<String, String> map;
    String uName;
    private SharedPreferences sharedPreferences;
    private DatabaseReference dataNode;
    private String username;
    private String password;    

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            e1 = (EditText)findViewById(R.id.etUsername);
            e2 = (EditText)findViewById(R.id.etPassword);
            er1 = (TextView) findViewById(R.id.txtError1);
            er2 = (TextView) findViewById(R.id.txtError2);
            auth = FirebaseAuth.getInstance();
            loginBtn = (Button)findViewById(R.id.btnLogin);
        TextView txtViewSignup = (TextView) findViewById(R.id.txtViewSignup);
        SpannableString content = new SpannableString("Sign up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtViewSignup.setText(content);

        txtViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(myIntent);
            }
        });

        }

        public void login(View view) {
            EditText usrname = (EditText) findViewById(R.id.etUsername);
            EditText pwd = (EditText) findViewById(R.id.etPassword);

            username = e1.getText().toString();
            password = e2.getText().toString();

            if (usrname.getText().toString().equals("")  || pwd.getText().toString().equals("")) {
                er1.setVisibility(View.VISIBLE);
                er2.setVisibility(View.VISIBLE);
                return;
            }
            if (username.length()>16) {
                er1.setVisibility(View.VISIBLE);
                er2.setVisibility(View.VISIBLE);
                return;
            }
            if (password.length()>16){
                er1.setVisibility(View.VISIBLE);
                er2.setVisibility(View.VISIBLE);
                return;
            }
            if (username.length()<8){
                er1.setVisibility(View.VISIBLE);
                er2.setVisibility(View.VISIBLE);
                return;
            }
            if (password.length()<8){
                er1.setVisibility(View.VISIBLE);
                er2.setVisibility(View.VISIBLE);
                return;
            }
//            else {

                auth.signInWithEmailAndPassword(username + "@gmail.com",e2.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {

                                    FirebaseUser user = auth.getCurrentUser();


                                        final String userID = user.getUid();
                                        DatabaseReference userReference = databaseReference.child(userID);
                                        userReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                map = (Map<String, String>) dataSnapshot.getValue();

                                                uName = map.get("username").toString();

                                                sharedPreferences = getSharedPreferences("WerNaU", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("CURRENTNAME", uName);
                                                editor.commit();

                                                dataNode = FirebaseDatabase.getInstance().getReference("privateDataNode");
                                                String conKey = dataNode.push().getKey();
                                                GroupDataModel model = new GroupDataModel();
                                                model.setConKey(conKey);
                                                model.setName("chatApp");
                                                dataNode.child(userID).push().setValue(model);

                                                Intent intent = new Intent(LoginActivity.this,ChatActivity.class);
                                                intent.putExtra("userID",userID);
                                                intent.putExtra("senderName",uName );
                                                startActivity(intent);



                                              //  FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                               // String fcm_token = Constants.getToken(getApplicationContext());


//                                                CreateUser mUser = new CreateUser();
//                                                if (firebaseUser != null) {
//                                                    mUser.setFcm_token(fcm_token);
//                                                }
//
//                                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                                editor.putString("name", uName);
//                                                editor.commit();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
//                                        Intent intent = new Intent(LoginActivity.this,ChatRoomActivity.class);
//                                        startActivity(intent);
//                                        finish();

                                }
                                else
                                {
                                    er1.setVisibility(View.VISIBLE);
                                    er2.setVisibility(View.VISIBLE);
                                }
                            }

                        });
//            }
        }
        }
