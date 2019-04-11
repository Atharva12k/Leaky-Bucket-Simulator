package com.example.atharva.leakybucket;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edt1,edt2,edt3,edt4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt1=findViewById(R.id.nop);
                edt2=findViewById(R.id.sop);
                edt3=findViewById(R.id.rop);
                edt4=findViewById(R.id.rop);
                int nop=Integer.parseInt(edt1.getText().toString());
                int sop=Integer.parseInt(edt2.getText().toString());
                int rop=Integer.parseInt(edt3.getText().toString());
                int sob=Integer.parseInt(edt4.getText().toString());
                Log.d("Check values ",nop+" "+sop+" "+rop);



                Intent i=new Intent(MainActivity.this,gifPlayer.class);
                i.putExtra("nop",nop);
                i.putExtra("sop",sop);
                i.putExtra("rop",rop);
                i.putExtra("sob",sob);
                startActivity(i);
                finish();





            }
        });




    }
}
