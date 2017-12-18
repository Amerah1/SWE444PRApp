package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHome2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference data,datacat;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> arrayAdapter;
    private TextView sug;
    private DatabaseReference database;
    private ListView Lcat;
    private ArrayList<String> category;
    int size=0;
    RecyclerView recycle;
    RecyclerView.Adapter adap ;
    ProgressDialog progressDia;
    List<ImageUpload_Category> listCat = new ArrayList<>();

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

       FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new ListCategories());
        ft.commit();
        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        data=FirebaseDatabase.getInstance().getReference().child("SPRDB");
        ValueEventListener EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String num= String.valueOf(dataSnapshot.getChildrenCount());
                    size=Integer.parseInt(num.substring(num.length()-1));
                    sug=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                            findItem(R.id.nav_suggested));
                    if(size!=0)
                        initializeCountDrawer(size);
                }catch (Exception e){
                    Toast.makeText(AdminHome2.this,e.getMessage(),Toast.LENGTH_LONG).show();}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        data.addListenerForSingleValueEvent(EventListener);


    }

    public void notifyProductSuggested(int size){
        finish();
        startActivity(new Intent(getApplicationContext(), AdminHome2.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_profile:
                fragment = new profile();
                break;
            case R.id.nav_product:
                fragment = new AddProduct();
                break;

            case R.id.nav_category:
                fragment = new AddCategory();
                break;

            case R.id.nav_suggested:
                fragment = new SuggestedProducts();
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.nav_ListCategory:
                fragment = new ListCategories();
                break;
                default:
                    fragment = new ListCategories();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void initializeCountDrawer(int size){

        //Gravity property aligns the text
        sug.setGravity(Gravity.CENTER_VERTICAL);
        sug.setTypeface(null, Typeface.BOLD);
        sug.setTextColor(getResources().getColor(R.color.colorAccent));
        sug.setText("**");


    }


}
