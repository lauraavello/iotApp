package be.kuleuven.gt.ballotapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import be.kuleuven.gt.ballotapp.databinding.ActivityHomepageBinding;
import com.google.android.material.bottomappbar.BottomAppBar;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton addMenu;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    ActivityHomepageBinding binding;
    String userName;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        addMenu = findViewById(R.id.add_menu);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("userName")) {
            userName = intent.getStringExtra("userName");
        }



        // Set up the toolbar
        setSupportActionBar(toolbar);

        // Set up ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bundle = new Bundle();
        bundle.putString("userName" , userName);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_home, new HomeFragment()).commit();
        }

        // Set up the bottom navigation view
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment frag = new Fragment();
            if (item.getItemId() == R.id.home_menu) {
                frag = new HomeFragment();
            } else if (item.getItemId() == R.id.box_menu) {
                frag = new BoxFragment();
            } else if (item.getItemId() == R.id.stats_menu) {
                frag = new VotingStatsFragment();
            } else if(item.getItemId() == R.id.comments_menu){
                frag = new CommentsFragment();
            }else {
                return true;
            }
            frag.setArguments(bundle);
            replaceFragment(frag);
            return true;
        });

        // Set up the add menu button
        addMenu.setOnClickListener(v -> {
            Fragment f = new AddFragment();
            f.setArguments(getUserBundle());
            replaceFragment(f);
        });

    }

    // Navigation (to be done)

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f = new Fragment();
        int itemId = item.getItemId();
        if (itemId == R.id.nav_accountSett) {
            f = new AccountSettingsFragment();
        } else if (itemId == R.id.nav_logOutSett) {
            startActivity(new Intent(this, LoginPage.class));
        } else if (itemId == R.id.nav_changePassword) {
            f = new changePasswordFragment();
        }  else if (itemId == R.id.nav_photo){
            f = new changePhotoFragment();
        } else {
            return true;
        }
        f.setArguments(getUserBundle());
        replaceFragment(f);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private Bundle getUserBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        return bundle;
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.commit();
    }
}