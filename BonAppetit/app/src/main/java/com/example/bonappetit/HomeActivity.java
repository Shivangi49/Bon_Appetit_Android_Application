package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bonappetit.model.Cart;
import com.example.bonappetit.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    MenuViewHolder holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.navMenu);
        drawerLayout =findViewById(R.id.drawer);


        //bottom navigation


        bottomNavigationView = findViewById (R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId ()) {
                    case R.id.home:
                        return true;



                    case R.id.cart:
                        Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
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

        //navigation drawer
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.about:
                        Intent intent=new Intent(getApplicationContext(),AboutUsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.feedback:
                        Intent feedintent=new Intent(getApplicationContext(),FeedbackActivity.class);
                        startActivity(feedintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.signOut:
                        FirebaseAuth.getInstance().signOut();
                        Intent setupIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                }
                return true;
            }
        });


        //load menu
        recyclerView=findViewById(R.id.recycler_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<Category> options=new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("category"), Category.class)
                .build();
        holder=new MenuViewHolder(options);
        recyclerView.setAdapter(holder);

    }

    @Override
    public void onStart() {
        super.onStart();
        holder.startListening();
    }



    //View Holder for menu
    public class MenuViewHolder extends FirebaseRecyclerAdapter<Category,MenuViewHolder.myViewHolder> {

        public MenuViewHolder(@NonNull FirebaseRecyclerOptions<Category> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int i, @NonNull final Category category) {

            myViewHolder.txtMenuName.setText(category.getName());
            Glide.with(myViewHolder.imageView.getContext()).load(category.getImage()).into(myViewHolder.imageView);
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id=getRef(i).getKey();
                    Intent intent=new Intent(getApplicationContext(),FoodList.class);
                    String name=category.getName();
                    intent.putExtra("category id",id);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
            });

        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
            return  new myViewHolder(view);
        }

        class myViewHolder extends RecyclerView.ViewHolder  {

            public TextView txtMenuName;
            public ImageView imageView;

            public  myViewHolder(@NonNull View itemView) {
                super(itemView);
                txtMenuName = (TextView) itemView.findViewById(R.id.menu_name);
                imageView = (ImageView) itemView.findViewById(R.id.menu_image);

            }
    }
    }



}

