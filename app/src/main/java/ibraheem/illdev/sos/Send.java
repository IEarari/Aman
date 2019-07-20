package ibraheem.illdev.sos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Send extends AppCompatActivity {
    EditText name , address , cellphone;
    String Name , Address , Cellphone , Email;
    Button Send;
    FirebaseFirestore db;
    String whomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            whomID = extras.getString("em");
            Email = extras.getString("Email");
            Log.e("Send.JAVA" , whomID+" Email :" + Email);

        }
        final String TAG = "FireStore";
        db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.et_name);
        InputFilter[] TextFilters = new InputFilter[1];
        TextFilters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };
        name.setFilters(TextFilters);
        address = findViewById(R.id.et_address);
        cellphone = findViewById(R.id.number);
        Send = findViewById(R.id.send_btn);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString();
                Cellphone = cellphone.getText().toString();
                Address = address.getText().toString();
                // Create a new case with a first and last name
                Map<String, Object> Case = new HashMap<>();
                Case.put("Email",Email );
                Case.put("Geo", Address);
                Case.put("ToWhom", whomID);
                Case.put("UserName", Name);
                Case.put("Status", "Received");

                // Add a new document with a generated ID
                db.collection("cases")
                        .add(Case)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                startActivity(new Intent(Send.this , Track.class).putExtra("ID" , documentReference.getId() ));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
    }
}
