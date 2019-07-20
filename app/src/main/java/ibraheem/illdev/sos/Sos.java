package ibraheem.illdev.sos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Sos extends AppCompatActivity {
    Button policebtn , firebtn , ambulance;
    String Email , Num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos);
        Num ="0";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Email = extras.getString("Email");
        }

        policebtn = findViewById(R.id.policebtn);
        firebtn =findViewById(R.id.firebtn);
        ambulance = findViewById(R.id.ambbtn);
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
    }
    private void alert(){
        new AlertDialog.Builder(this)
                .setMessage("Do You Prefer Call or Make Online Case ?")
                .setNegativeButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+Num)));
                    }
                })
                .setPositiveButton("Make Online Case", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(Sos.this , Send.class).putExtra("Email" , Email).putExtra("em" , Num));
                    }
                }).create().show();
    }
}
