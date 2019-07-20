package ibraheem.illdev.sos;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Track extends AppCompatActivity {
    TextView Status;
    Toolbar toolbar;
    String id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "Track . JAVA";
    DocumentReference Case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Status = findViewById(R.id.status_tv);
        toolbar = findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("ID");
            toolbar.setTitle("Your Case ID: " + id);
        }
        Case = db.collection("cases").document(id);
        Case.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    Status.setText(String.valueOf(snapshot.getData().get("Status")));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }
}
