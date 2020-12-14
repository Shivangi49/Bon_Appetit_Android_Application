package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bonappetit.model.Cart;
import com.example.bonappetit.model.Food;
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

import java.util.HashMap;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    FoodViewHolder holder;
    DatabaseReference reference;
    Toolbar toolbar;
    private String category_id;
    FirebaseRecyclerOptions<Food> recyclerOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        reference=FirebaseDatabase.getInstance().getReference().child("foods");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getExtras().get("name").toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //bottom navigation

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById (R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId ()) {
                    case R.id.account:
                        Intent accountintent=new Intent(getApplicationContext(),AccountActivity.class);
                        startActivity(accountintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
                        Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(cartintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;
                    default:
                        return false;

                }
            }
        });

        //food items
        recyclerView=findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent()!=null){
        category_id=getIntent().getExtras().get("category id").toString();}


        if(!category_id.isEmpty() && category_id!=null){

            FirebaseRecyclerOptions<Food> options=new FirebaseRecyclerOptions.Builder<Food>()
                    .setQuery(reference.orderByChild("category_id").equalTo(category_id), Food.class)
                    .build();
            holder=new FoodViewHolder(options);
            recyclerView.setAdapter(holder);

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        holder.startListening();
    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }


    //view holder for food

    public class FoodViewHolder extends FirebaseRecyclerAdapter<Food,FoodViewHolder.myViewHolder> {

        public FoodViewHolder(@NonNull FirebaseRecyclerOptions<Food> options) {

            super(options);
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
            return  new myViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i, @NonNull final Food food) {

            myViewHolder.txtFoodName.setText(food.getName());
            myViewHolder.txtFoodPrice.setText("Price: ₹"+food.getPrice());
            Glide.with(myViewHolder.imageView.getContext()).load(food.getImage()).into(myViewHolder.imageView);
            final String name=food.getName();
            final String price=food.getPrice();
            FirebaseAuth auth=FirebaseAuth.getInstance();
            FirebaseUser firebaseUser=auth.getCurrentUser();
            final String id=firebaseUser.getUid();
            final String food_id=getRef(i).getKey();
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("cart").child(id).child(food_id);


            myViewHolder.addquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    if(Integer.parseInt(myViewHolder.addquantity.getNumber())==0){
                        databaseReference.removeValue();
                    }
                    else{

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("foodid",food_id);
                        hashMap.put("name",name);
                        hashMap.put("price",price);
                        hashMap.put("quantity", myViewHolder.addquantity.getNumber());
                        databaseReference.setValue(hashMap);
                    }
                }

            });




        }

        class myViewHolder extends RecyclerView.ViewHolder  {
            public TextView txtFoodName,txtFoodPrice;
            public ElegantNumberButton addquantity;
            public ImageView imageView;

            public  myViewHolder(@NonNull View itemView){
                super(itemView);
                txtFoodName=(TextView)itemView.findViewById(R.id.food_name);
                imageView=(ImageView)itemView.findViewById(R.id.food_image);
                txtFoodPrice=(TextView)itemView.findViewById(R.id.food_price);
                addquantity=itemView.findViewById(R.id.addquantity);
            }

        }
    }


}