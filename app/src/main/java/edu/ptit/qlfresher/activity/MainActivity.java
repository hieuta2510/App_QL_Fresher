package edu.ptit.qlfresher.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.ViewPagerAdapter;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.User;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    private String key = "";
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        key = intent.getStringExtra("email");
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.bottom_nav);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getUser());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
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

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Hiển thị lại BottomNavigationView
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.VISIBLE);

            super.onBackPressed();
        }
    }

    private User getUser()
    {
        db = new SQLiteHelper(this);
        User user = db.getUserByEmail(key);
        return user;
    }

}