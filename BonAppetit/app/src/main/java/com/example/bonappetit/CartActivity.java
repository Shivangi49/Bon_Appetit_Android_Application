package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bonappetit.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CartViewHolder holder;
    Toolbar toolbar;
    Button order;
    Integer total,quantity;
    ArrayList<String> fooditems;
    String fid,id;
    private int overalltotal=0;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        order=findViewById(R.id.order);
        recyclerView = findViewById(R.id.recycler_food);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fooditems=new ArrayList<>();
        //bottom navigation

        bottomNavigationView = findViewById (R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId ()) {
                    case R.id.cart:
                        return true;

                    case R.id.home:
                        Intent cartintent=new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(cartintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.account:
                        Intent accountintent=new Intent(getApplicationContext(),AccountActivity.class);
                        startActivity(accountintent);
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;

                }
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),PaymentActivity.class);
                intent.putExtra("total price",String.valueOf(overalltotal));
                intent.putStringArrayListExtra("food items",fooditems);
                intent.putExtra("quantity",String.valueOf(quantity));
                startActivity(intent);
                finish();

            }
        });

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        id=firebaseUser.getUid();
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child(id), Cart.class)
                .build();
        holder = new CartViewHolder(options);
        recyclerView.setAdapter(holder);

        }



    @Override
    public void onStart() {
        super.onStart();
        holder.startListening();
    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    //view holder for cart


    public class CartViewHolder extends FirebaseRecyclerAdapter<Cart, CartViewHolder.cartViewHolder> {
        public CartViewHolder(@NonNull FirebaseRecyclerOptions<Cart> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final cartViewHolder cartViewHolder, final int i, @NonNull final Cart cart) {

            cartViewHolder.foodName.setText(cart.getName());
            cartViewHolder.foodPrice.setText(cart.getPrice());
            cartViewHolder.fquantity.setNumber(cart.getQuantity());
            final Integer price=Integer.parseInt(cart.getPrice());
            quantity=Integer.parseInt(cart.getQuantity());
             total=price*quantity;
             String foodname=cart.getName();
             fid=cart.getFoodid();
             fooditems.add(foodname);
            overalltotal=overalltotal+total;
            cartViewHolder.foodtotal.setText(total.toString());
            FirebaseAuth auth=FirebaseAuth.getInstance();
            FirebaseUser firebaseUser=auth.getCurrentUser();
            final String id=firebaseUser.getUid();
            final String food_id=cart.getFoodid();
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("cart").child(id).child(food_id);

            cartViewHolder.fquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    if(Integer.parseInt(cartViewHolder.fquantity.getNumber())==0){
                        databaseReference.removeValue();
                    }
                    else{
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("foodid",food_id);
                        hashMap.put("name",cart.getName());
                        hashMap.put("price",cart.getPrice());
                        hashMap.put("quantity", cartViewHolder.fquantity.getNumber());
                        databaseReference.setValue(hashMap);
                    }
                }

            });



        }
        @NonNull
        @Override
        public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new cartViewHolder(view);
        }

        class cartViewHolder extends RecyclerView.ViewHolder {
            public TextView foodName, foodPrice,foodtotal;
            public ElegantNumberButton fquantity;

            public cartViewHolder(@NonNull View itemView) {
                super(itemView);
                foodName = (TextView) itemView.findViewById(R.id.food_name);
                foodPrice = (TextView) itemView.findViewById(R.id.food_price);
                foodtotal=(TextView)itemView.findViewById(R.id.total);
                fquantity=itemView.findViewById(R.id.elquantity);
            }


        }


    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}