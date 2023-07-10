package edu.ptit.ql_fresher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import edu.ptit.ql_fresher.adapter.ViewPagerAdapter;
import edu.ptit.ql_fresher.fragment.FragmentHome;
import edu.ptit.ql_fresher.fragment.FragmentManageFresher;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.bottom_nav);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.nav_manageFresher).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.nav_manageCenter).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.nav_dashboard).setChecked(true);
                        break;
                    case 4:
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_manageFresher:viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_manageCenter:viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_home:viewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_dashboard:viewPager.setCurrentItem(3);
                        break;
                    case R.id.nav_profile:viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }
}