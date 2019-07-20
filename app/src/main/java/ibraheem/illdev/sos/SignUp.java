package ibraheem.illdev.sos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    EditText email, password;
    String Email, Password;
    Button Signup;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        Signup = findViewById(R.id.signup_btn);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    tost("User Craeated Successfully");
                                    startActivity(new Intent(SignUp.this, Login.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    tost("Failed");
                                }
                            }
                        });
            }
        });
    }

    public void toSignIn(View view) {
        Intent to_sign_in = new Intent(this, Login.class);
        startActivity(to_sign_in);
    }

    private void tost(String msg) {
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_LONG).show();
    }
}
