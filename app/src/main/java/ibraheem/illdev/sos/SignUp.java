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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText email, password, phone, name;
    String Email, Password, Phone, Name;
    Button SignUp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        phone = findViewById(R.id.et_phone);
        name = findViewById(R.id.et_name);
        SignUp = findViewById(R.id.signup_btn);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                Phone = phone.getText().toString();
                if (phone.length() == 10) {
                    Name = name.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        tost("User Created Successfully Please confirm your email");
                                        if (firebaseAuth.getCurrentUser() != null) {
                                            firebaseAuth.getCurrentUser().sendEmailVerification();
                                            CollectionReference usersRef = db.collection("users");
                                            String id = firebaseAuth.getCurrentUser().getUid();
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("Email", Email);
                                            user.put("Phone", Phone);
                                            user.put("Name", Name);
                                            user.put("AccType","Normal");
                                            usersRef.document(id).set(user);
                                        }

                                        startActivity(new Intent(SignUp.this, Login.class));
                                    } else {
                                        tost("Failed");
                                    }
                                }
                            });
                }else{
                    tost("Please Check Your Number");
                }
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
