package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    EditText pname,paddress,pcontact;
    TextView txtusername,txtuseremail,txtuseraddress,txtusercontact;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        pname=findViewById(R.id.name);
        paddress=findViewById(R.id.address);
        txtuseremail=findViewById(R.id.email);
        txtusername=findViewById(R.id.username);
        txtuseraddress=findViewById(R.id.useraddress);
        txtusercontact=findViewById(R.id.usercontact);
        pcontact=findViewById(R.id.Contact);


        update=findViewById(R.id.updateacc);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        final String id=firebaseUser.getUid();
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname=snapshot.child("name").getValue().toString();
                String emailid=snapshot.child("email").getValue().toString();
                String useraddr=snapshot.child("address").getValue().toString();
                String usercont=snapshot.child("contact").getValue().toString();
                txtuseremail.setText(emailid);
                txtusername.setText("Name: "+uname);
                txtuseraddress.setText("Address: "+useraddr);
                txtusercontact.setText("Contact: "+usercont);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //bottom navigation

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById (R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId ()) {
                    case R.id.account:
                        return true;

                    case R.id.cart:
                        Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(cartintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        Intent accountintent=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(accountintent);
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;

                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=pname.getText().toString();
                String address=paddress.getText().toString();
                String usercontact=pcontact.getText().toString();
                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(address) && TextUtils.isEmpty(usercontact))
                {
                    Toast.makeText(getApplicationContext(),"Please provide data to update",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(!TextUtils.isEmpty(name))
                    {
                        reference.child("name").setValue(name);
                        Toast.makeText(getApplicationContext(),"Update sucessful",Toast.LENGTH_SHORT).show();
                    }

                    if(!TextUtils.isEmpty(address))
                    {
                        reference.child("address").setValue(address);
                        Toast.makeText(getApplicationContext(),"Update sucessful",Toast.LENGTH_SHORT).show();

                    }
                    if(!TextUtils.isEmpty(usercontact)){
                        reference.child("contact").setValue(usercontact);
                        Toast.makeText(getApplicationContext(),"Update sucessful",Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public void onBackPressed() {
       Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
       startActivity(intent);
        overridePendingTransition(0,0);
    }

}