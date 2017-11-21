package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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

public class AdminHome2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference data;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> arrayAdapter;
    int size=0;



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

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //////////////
       /* Firebase.setAndroidContext(this);
        DatabaseReference fbDb =  FirebaseDatabase.getInstance().getReference();



        fbDb.child("product-rating-app/UserAdmin")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        size =(int) dataSnapshot.getChildrenCount();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
        suggest=new ArrayList<String>();
        data=FirebaseDatabase.getInstance().getReference().child("PRDB").child("PInfo");
        ValueEventListener EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String val = dataSnapshot.getValue(String.class);
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                        ImageUploadInfo product = ds.getValue(ImageUploadInfo.class);
                        suggest.add(product.getImageName());

                        arrayAdapter=new ArrayAdapter<String>(AdminHome2.this , android.R.layout.simple_list_item_1,suggest);
                        //size++;
                    }
                }catch (Exception e){
                    Toast.makeText(AdminHome2.this,e.getMessage(),Toast.LENGTH_LONG).show();}
                //suggest.add(val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        data.addListenerForSingleValueEvent(EventListener);
        size=suggest.size();



        Menu menuNav = navigationView.getMenu();
        MenuItem element = menuNav.findItem(R.id.nav_suggested);
        String before = element.getTitle().toString();

        String counter = Integer.toString(size);
        String s = before + "   "+counter+" ";
        SpannableString sColored = new SpannableString( s );

        sColored.setSpan(new BackgroundColorSpan( Color.RED ), s.length()-(counter.length()+2), s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-(counter.length()+2), s.length(), 0);


        element.setTitle(sColored);






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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_admin_home2_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, MainActivity.class));
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
}
