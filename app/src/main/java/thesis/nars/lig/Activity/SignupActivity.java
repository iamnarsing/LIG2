package thesis.nars.lig.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thesis.nars.lig.Model.CreateUser;
import thesis.nars.lig.R;

/**
 * Created by nars on 12/16/2019.
 */

public class SignupActivity extends AppCompatActivity{

    private static final String TAG = "NAAARS";
    FirebaseAuth auth;

    EditText e1,e2;
    TextView er1,er2;
    Button loginBtn;
    private String username,password;
    private String userID;

    DatabaseReference reference;
    private FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        e1 = (EditText)findViewById(R.id.etUsername);
        e2 = (EditText)findViewById(R.id.etPassword);
        er1 = (TextView) findViewById(R.id.txtError1);
        er2 = (TextView) findViewById(R.id.txtError2);

        loginBtn = (Button)findViewById(R.id.btnLogin);

        TextView txtViewSignup = (TextView) findViewById(R.id.txtViewLogin);
        SpannableString content = new SpannableString("Login");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtViewSignup.setText(content);

        txtViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(myIntent);
            }
        });

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void signup(View view) {

        EditText usrname = (EditText) findViewById(R.id.etUsername);
        EditText pwd = (EditText) findViewById(R.id.etPassword);



        username = e1.getText().toString();

        if (usrname.getText().toString().equals("")  || pwd.getText().toString().equals("")) {
            er1.setVisibility(View.VISIBLE);
            er2.setVisibility(View.VISIBLE);
        }
        else {
            auth.createUserWithEmailAndPassword(username + "@gmail.com" ,e2.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                CreateUser mUser = new CreateUser();
                                CreateUser createUser = new CreateUser(e1.getText().toString(),e2.getText().toString());
                                user = auth.getCurrentUser();
                                userID = user.getUid();

                                reference.child(userID).setValue(createUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });

                            } else {
                                er1.setVisibility(View.VISIBLE);
                                er2.setVisibility(View.VISIBLE);
                            }

                        }
                    });

        }
    }
}
