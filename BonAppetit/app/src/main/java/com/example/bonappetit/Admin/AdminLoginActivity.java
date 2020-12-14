package com.example.bonappetit.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bonappetit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {
    EditText id,password;
    Button sign_in;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        id=findViewById(R.id.id);
        password=findViewById(R.id.password);
        sign_in=findViewById(R.id.sign_in);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sign_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                final String txtid=id.getText().toString();
                final String txtpassword=password.getText().toString();
                reference= FirebaseDatabase.getInstance().getReference().child("Admins").child(txtid);
                if(id.equals("") || password.equals(""))
                {
                    Toast.makeText(AdminLoginActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String f_password=snapshot.child("password").getValue().toString();

                            if (txtpassword.equals(f_password)) {

                                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(AdminLoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "Password and id does not match", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Please check your credentials",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}