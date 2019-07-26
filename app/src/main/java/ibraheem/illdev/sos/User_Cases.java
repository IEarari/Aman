package ibraheem.illdev.sos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class User_Cases extends AppCompatActivity {
        final String TAG = "User_Cases";
    FirebaseFirestore db;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cases);
        db = FirebaseFirestore.getInstance();
        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listView = findViewById(R.id.list_user_cases);
        db.collection("cases").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<Cases> cases_array = new ArrayList<>();
                            final UserCasesAdapter adapter = new UserCasesAdapter(User_Cases.this, cases_array);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (String.valueOf(document.get("UserID")).equals(UID)) {
                                    cases_array.add(new Cases(String.valueOf(document.get("UserName")), String.valueOf(document.get("Phone")), String.valueOf(document.get("Status")), document.getId(), String.valueOf(document.get("Geo")),String.valueOf(document.get("Dep"))));
                                    listView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        startActivity(new Intent(User_Cases.this , Track.class).putExtra("ID" , cases_array.get(position).getID()));
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
