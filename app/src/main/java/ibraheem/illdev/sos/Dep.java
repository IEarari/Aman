package ibraheem.illdev.sos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Dep extends AppCompatActivity {
    String User_Dep, Dep, caseID;
    FirebaseFirestore db;
    String TAG = "Dep.Java";
    ListView listView;
    TextView Name, Address, Number, caseStatus, CaseID;
    Button received, refused, onway, arrived, done, signout;
    boolean isCaseDone = false;
    int pos;
    final ArrayList<Cases> cases_array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep);
        Name = findViewById(R.id.name);
        Address = findViewById(R.id.address);
        Number = findViewById(R.id.cellphone);
        caseStatus = findViewById(R.id.case_status);
        CaseID = findViewById(R.id.case_id);
        listView = findViewById(R.id.dep_list);
        db = FirebaseFirestore.getInstance();
        User_Dep = "None";
        received = findViewById(R.id.received);
        refused = findViewById(R.id.refused);
        onway = findViewById(R.id.on_way);
        arrived = findViewById(R.id.arrived);
        done = findViewById(R.id.done);
        signout = findViewById(R.id.sign_out_dep);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dep.this , Login.class));
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus("done");
                cases_array.remove(pos);
            }
        });
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus("arrived");
            }
        });
        onway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus("on the way");
            }
        });
        refused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus("refused");
            }
        });
        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus("received");
            }
        });
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

            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                db.collection("cases").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    final CasesAdapter adapter = new CasesAdapter(Dep.this, cases_array);
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if(String.valueOf(document.get("Status")).equals("done")){
                                                            isCaseDone = true;
                                                        }else {
                                                            isCaseDone= false;
                                                        }
                                                        if (String.valueOf(document.get("Dep")).equals(Dep) && !isCaseDone) {
                                                            cases_array.add(new Cases(String.valueOf(document.get("UserName")), String.valueOf(document.get("Phone")), String.valueOf(document.get("Status")), document.getId(), String.valueOf(document.get("Geo")),String.valueOf(document.get("Dep"))));
                                                            listView.setAdapter(adapter);
                                                            adapter.notifyDataSetChanged();
                                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    Name.setText(cases_array.get(position).getName());
                                                                    Address.setText(cases_array.get(position).getAddress());
                                                                    Number.setText(cases_array.get(position).getCell_number());
                                                                    caseStatus.setText(cases_array.get(position).getStatus());
                                                                    CaseID.setText(cases_array.get(position).getID());
                                                                    caseID = cases_array.get(position).getID();
                                                                    pos = position;
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
                        });
                    }
                }
            };
            thread.start();
        }

    }

    private void ChangeStatus(String status) {
        FirebaseFirestore firestore;
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("cases").document(caseID).update("Status",status);
        caseStatus.setText(status);
    }
}
