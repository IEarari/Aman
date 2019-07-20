package ibraheem.illdev.sos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {
    EditText email, password;
    String Email, Password;
    Button Login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = findViewById(R.id.email_input_login);
        password = findViewById(R.id.password_input_login);
        Login = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                final Intent toSos = new Intent(Login.this, Sos.class).putExtra("Email",Email);
                firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(toSos);
                        }else{
                            Log.e("Firebase Error: " , task.getException().getMessage());
                            Toast.makeText(Login.this , task.getException().getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }

    public void toSignUp(View view) {
        Intent to_sign_up = new Intent(this, SignUp.class);
        startActivity(to_sign_up);
    }
}