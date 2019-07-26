package ibraheem.illdev.sos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    EditText email, password;
    String Email, Password, AccType, UID;
    Button Login;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    final String TAG = "Login Page";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        progressBar = findViewById(R.id.progress_circular);
        db = FirebaseFirestore.getInstance();
        email = findViewById(R.id.email_input_login);
        password = findViewById(R.id.password_input_login);
        Login = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                final Intent toSos = new Intent(Login.this, Sos.class).putExtra("Email", Email);
                firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    UID = FirebaseAuth.getInstance().getUid();
                                    db.collection("users").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getId().equals(UID)) {
                                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                                AccType = String.valueOf(document.get("AccType"));
                                                                Log.e("AccType", document.get("AccType") + " Email: " + Email);
                                                                if (AccType.equals("Normal")) {
                                                                    Intent toSos = new Intent(Login.this, Sos.class).putExtra("Email", Email);
                                                                    startActivity(toSos);
                                                                    finish();
                                                                } else if (AccType.equals("Police")) {
                                                                    startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Police"));
                                                                    finish();
                                                                } else if (AccType.equals("Ambulance")) {
                                                                    startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Ambulance"));
                                                                    finish();
                                                                } else if (AccType.equals("Fire")) {
                                                                    startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Fire"));
                                                                    finish();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                    Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Log.e("Firebase Error: ", task.getException().getMessage());
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getEmail() != null && currentUser.isEmailVerified()) {
                progressBar.setVisibility(View.VISIBLE);
                Email = currentUser.getEmail();
                UID = FirebaseAuth.getInstance().getUid();
                db.collection("users").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getId().equals(UID)) {
                                            AccType = String.valueOf(document.get("AccType"));
                                            Log.e("AccType", document.get("AccType") + " Email: " + Email);
                                            if (AccType.equals("Normal")) {
                                                Intent toSos = new Intent(Login.this, Sos.class).putExtra("Email", Email);
                                                startActivity(toSos);
                                                finish();
                                            } else if (AccType.equals("Police")) {
                                                startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Police"));
                                                finish();
                                            } else if (AccType.equals("Ambulance")) {
                                                startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Ambulance"));
                                                finish();
                                            } else if (AccType.equals("Fire")) {
                                                startActivity(new Intent(Login.this, Dep.class).putExtra("DEP", "Fire"));
                                                finish();
                                            }
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                //

            }
        }
    }

    public void toSignUp(View view) {
        Intent to_sign_up = new Intent(this, SignUp.class);
        startActivity(to_sign_up);
        finish();
    }
}