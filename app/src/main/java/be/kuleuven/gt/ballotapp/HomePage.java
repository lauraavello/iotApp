package be.kuleuven.gt.ballotapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;


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
    ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        profileIcon = findViewById(R.id.profile_icon); //
        addMenu = findViewById(R.id.add_menu);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("userName")) {
            userName = intent.getStringExtra("userName");
        }


        if (profileIcon != null) {
            profileIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        } else {
            Log.e("HomePage", "profile_icon is NULL in nav_header.xml!");
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_nav, R.string.close_nav) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bundle = new Bundle();
        bundle.putString("userName", userName);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_home, new HomeFragment()).commit();
        }

        // Set up bottom navigation
        bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment frag;
            if (item.getItemId() == R.id.home_menu) {
                frag = new HomeFragment();
            } else if (item.getItemId() == R.id.box_menu) {
                frag = new BoxFragment();
            } else if (item.getItemId() == R.id.stats_menu) {
                frag = new VotingStatsFragment();
            } else if (item.getItemId() == R.id.comments_menu) {
                frag = new CommentsFragment();
            } else {
                return true;
            }
            frag.setArguments(bundle);
            replaceFragment(frag);
            return true;
        });

        // Set up add menu button
        addMenu.setOnClickListener(v -> {
            Fragment f = new AddFragment();
            f.setArguments(getUserBundle());
            replaceFragment(f);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;
        int itemId = item.getItemId();
        if (itemId == R.id.admin_settings) {
            f = new AdminSettingsFragment();
        } else if (itemId == R.id.nav_logOutSett) {
            startActivity(new Intent(this, LoginPage.class));
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        } else if (itemId == R.id.password_settings) {
            f = new ChangePasswordFragment();
        } else if (itemId == R.id.profile_pic_settings) {
            f = new ChangePhotoFragment();
        } else {
            return true;
        }
        f.setArguments(getUserBundle());
        replaceFragment(f);
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

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
