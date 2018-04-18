package com.haybankz.medmanager.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.haybankz.medmanager.adapter.MedicationRecyclerAdapter;
import com.haybankz.medmanager.adapter.PicassoCircleTransformation;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.SimpleFragmentPagerAdapter;
import com.haybankz.medmanager.data.user.UserContract;
import com.haybankz.medmanager.listener.ClickListener;
import com.haybankz.medmanager.listener.RecyclerTouchListener;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.User;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.FileUtils;
import com.haybankz.medmanager.util.ImageUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;
import com.haybankz.medmanager.util.PreferenceUtils;
import com.haybankz.medmanager.util.UserDbUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    LinearLayout mNoResultLayout;
    TextView mNoResultFoundTextView;
    RecyclerView mSearchRecyclerView;

    MedicationRecyclerAdapter mMedicationRecyclerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    MenuItem searchViewItem;
    SearchView searchView;

    TextView mNameTextView;
    TextView mEmailTextView;
    ImageView mProfilePicImageView;

    GoogleSignInClient mGoogleSignInClient;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNoResultLayout = findViewById(R.id.layout_no_result_found);
        mNoResultFoundTextView = findViewById(R.id.tv_no_result_found);

        View header = navigationView.getHeaderView(0);
        mNameTextView = header.findViewById(R.id.tv_name);
        mEmailTextView = header.findViewById(R.id.tv_email);
        mProfilePicImageView = header.findViewById(R.id.img_profile_pics);


        if(getIntent() != null){
            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {

                int i = getIntent().getIntExtra(Constant.KEY_ACCT_ID, 0);
                String photoUrl = getIntent().getStringExtra(Constant.KEY_ACCT_PHOTO_URL);

                if(i > 0){
                    user = UserDbUtils.getUserById(this, i);
                    if(user != null){
                        mNameTextView.setText(user.getDisplayName());
                        mEmailTextView.setText(user.getEmail());

                        if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                            Picasso.with(this)
                                    .load(user.getPhotoUrl())
                                    .transform(new PicassoCircleTransformation())
                                    .fit()
                                    .placeholder(R.drawable.ic_person_white)
                                    .error(R.drawable.ic_person_dark)
//                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .noFade()
                                    .into(mProfilePicImageView);
                        }else{
                            mProfilePicImageView.setImageResource(R.drawable.ic_person_white);
                        }
                    }
                }

                if(photoUrl != null && !photoUrl.equals("") && i > 0){
                    Picasso.with(this)
                        .load(photoUrl)
                        .into(ImageUtils.getTarget(this, i));

                    File file = FileUtils.saveImage(this, ImageUtils.getBitmapFromPath(this, photoUrl));
                    ContentValues values = new ContentValues();
                    values.put(UserContract.UserEntry.COLUMN_USER_PHOTO_URL, "file:" + file.getPath());

                    UserDbUtils.updateUser(this, i, values);
                }

            }else{
                int i = getIntent().getIntExtra(Constant.KEY_ACCT_ID, 0);

                if(i > 0){
                    user = UserDbUtils.getUserById(this, i);
                    if(user != null){
                        mNameTextView.setText(user.getDisplayName());
                        mEmailTextView.setText(user.getEmail());

                        Picasso.with(this)
                                .load(user.getPhotoUrl())
                                .transform(new PicassoCircleTransformation())
                                .placeholder(R.drawable.ic_person_white)
                                .error(R.drawable.ic_person_dark)
//                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .noFade()
                                .into(mProfilePicImageView);
                    }
                }
            }

        }

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);

                startActivity(intent);
            }
        });




        viewPager = findViewById(R.id.view_pager);

        SimpleFragmentPagerAdapter simpleFragmentPagerAdapter =
                new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());


        viewPager.setAdapter(simpleFragmentPagerAdapter);
        viewPager.setCurrentItem(0);

        tabLayout = findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);


        mSearchRecyclerView = findViewById(R.id.recycler_search);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mSearchRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mMedicationRecyclerAdapter = new MedicationRecyclerAdapter(MainActivity.this, new ArrayList<Medication>());

        mSearchRecyclerView.setAdapter(mMedicationRecyclerAdapter);
        mSearchRecyclerView.addItemDecoration(new DividerItemDecoration(mSearchRecyclerView.getContext(), StaggeredGridLayoutManager.VERTICAL));

        mSearchRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mSearchRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(MainActivity.this, mMedicationRecyclerAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        searchViewItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchViewItem.getActionView();
        final TextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchTextView.setBackgroundColor(getResources().getColor(R.color.white));
        searchTextView.setTextColor(getResources().getColor(R.color.black));
        searchTextView.setHintTextColor(getResources().getColor(R.color.darker_gray));



        String searchFor = searchTextView.getText().toString();


        if(!searchFor.isEmpty()){
            searchView.setIconified(false);

        }



        searchView.setQueryHint("Search for medication");

        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){

                    mNoResultLayout.setVisibility(View.GONE);
                    mSearchRecyclerView.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);

                }else{

                    ArrayList<Medication> medications = MedicationDbUtils.getMedicationsByName(MainActivity.this, newText);
                    if(medications != null) {

                        tabLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.GONE);
                        mNoResultLayout.setVisibility(View.GONE);


                        mMedicationRecyclerAdapter.addAll(medications);
                        mMedicationRecyclerAdapter.notifyDataSetChanged();
                        mSearchRecyclerView.setVisibility(View.VISIBLE);

                    }else{

                        mMedicationRecyclerAdapter.clear();

                        tabLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.GONE);
                        mSearchRecyclerView.setVisibility(View.GONE);
                        mNoResultFoundTextView.setText("No record found for '" + newText + "'");
                        mNoResultLayout.setVisibility(View.VISIBLE);



                    }



                }

                return false;
            }
        });


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_med) {

            Intent addMedicationIntent = new Intent(this, AddMedicationActivity.class);
            startActivity(addMedicationIntent);

            // Handle the camera action
        } else if (id == R.id.nav_search) {

            searchViewItem.expandActionView();

        } else if (id == R.id.nav_log_out) {

            mGoogleSignInClient.signOut();
            PreferenceUtils.setLoggedOut(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        } else if (id == R.id.nav_update_user) {

            Intent intent = new Intent(this, UpdateUserActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = (int) PreferenceUtils.getLoggedInUser(this);

        user = UserDbUtils.getUserById(this, id);
        if(user != null){
            mNameTextView.setText(user.getDisplayName());
            mEmailTextView.setText(user.getEmail());

            if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                Picasso.with(this)
                        .load(user.getPhotoUrl())
                        .transform(new PicassoCircleTransformation())
                        .fit()
                        .placeholder(R.drawable.ic_person_white)
                        .error(R.drawable.ic_person_dark)
//                                    .networkPolicy(NetworkPolicy.OFFLINE)
                        .noFade()
                        .into(mProfilePicImageView);
            }else{
                mProfilePicImageView.setImageResource(R.drawable.ic_person_white);
            }
        }
    }
}
