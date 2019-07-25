package ibraheem.illdev.sos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

public class Sos extends AppCompatActivity {
    Button policebtn, firebtn, ambulance , sign_out;
    String Email, Num, Name, UID , Phone;
    String Address = "";
    FirebaseFirestore db;
    final String TAG = String.valueOf(Context.current());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos);
        db = FirebaseFirestore.getInstance();
        final String TAG = "FireStore";
        Num = "0";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Email = extras.getString("Email");
        }
        UID = FirebaseAuth.getInstance().getUid();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(UID)) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Name = String.valueOf(document.get("Name"));
                                    Phone = String.valueOf(document.get("Phone"));
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        policebtn = findViewById(R.id.policebtn);
        firebtn = findViewById(R.id.firebtn);
        ambulance = findViewById(R.id.ambbtn);
        sign_out = findViewById(R.id.sign_out);
        policebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
                Num = "100";
            }
        });
        firebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = "102";
                alert();
            }
        });
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = "101";
                alert();
            }
        });
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent (Sos.this , Login.class));
            }
        });
    }

    private void alert() {
        new AlertDialog.Builder(this)
                .setMessage("Do You Prefer Call or Make Online Case ?")
                .setNegativeButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + Num)));
                    }
                })
                .setPositiveButton("Make Online Case", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sos.this);
                        builder.setTitle("Please Enter Your Current Location");

                        // Set up the input
                        final EditText input = new EditText(Sos.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    Address = input.getText().toString();

                                    Map<String, Object> Case = new HashMap<>();
                                    Case.put("Email", Email);
                                    Case.put("Geo", Address);
                                    Case.put("Dep", Num);
                                    Case.put("UserName", Name);
                                    Case.put("Status", "Received");
                                    Case.put("UserID" ,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Case.put("Phone" , Phone);
                                    // Add a new document with a generated ID
                                    db.collection("cases")
                                            .add(Case)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    startActivity(new Intent(Sos.this, Track.class).putExtra("ID", documentReference.getId()));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });


                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }).create().show();
    }
}
