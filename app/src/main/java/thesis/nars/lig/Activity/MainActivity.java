package thesis.nars.lig.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import thesis.nars.lig.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String uName;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        if(user == null)
        {
            setContentView(R.layout.activity_main);

        }
        else
        {
            uName = user.getDisplayName();
            userID = user.getUid();
            Intent intent = new Intent(MainActivity.this,ChatActivity.class);
            intent.putExtra("userID",userID);
            intent.putExtra("senderName",uName );
            startActivity(intent);
        }
    }
    public void goToLogin(View v)
    {
        Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(myIntent);
    }
    public void goToSignup(View v)
    {
        Intent myIntent = new Intent(MainActivity.this,SignupActivity.class);
        startActivity(myIntent);
    }
}
