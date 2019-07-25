package ibraheem.illdev.sos;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Dep extends AppCompatActivity {
    String User_Dep, Dep;
    FirebaseFirestore db;
    String TAG = "Dep.Java";
    ListView listView;
    TextView Name, Address, Number, caseStatus, CaseID;
    int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep);
        /*ArrayList<array> array = new ArrayList<>();
        while (j < 5) {
            array.add(new array(j));
            if (j == 4) {
                Log.e("=>", String.valueOf(j));
            }

        }*/
        listView = findViewById(R.id.dep_list);
        db = FirebaseFirestore.getInstance();
        User_Dep = "None";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_Dep = extras.getString("DEP");
            if (User_Dep.equals("Fire")) {
                Dep = "102";
            } else if (User_Dep.equals("Police")) {
                Dep = "100";
            } else if (User_Dep.equals("Ambulance")) {
                Dep = "101";
            } else {
                Toast.makeText(Dep.this, "Please Contact Administration , Account Error Occurred", Toast.LENGTH_LONG).show();
            }
            db.collection("cases").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int i = 0;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (String.valueOf(document.get("Dep")).equals(Dep)) {
                                        i++;
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        ArrayList<Cases> cases = new ArrayList<>();
                                        CasesAdapter adapter = new CasesAdapter(Dep.this, cases);
                                        cases.add(new Cases(String.valueOf(document.get("UserName")), String.valueOf(document.get("Phone")), String.valueOf(document.get("Status")), document.getId(), String.valueOf(document.get("Geo"))));
                                        adapter.notifyDataSetChanged();
                                        listView.setAdapter(adapter);
                                        Log.e("Size =>" , String.valueOf(cases.size()));
                                        Log.e("=>" ,String.valueOf(document.get("UserName"))+" "+ String.valueOf(document.get("Phone"))+" "+ String.valueOf(document.get("Status"))+" "+ document.getId()+" "+ String.valueOf(document.get("Geo")) );
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

    }
}
