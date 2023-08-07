package edu.ptit.qlfresher.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.fragment.FragmentAddCenter;
import edu.ptit.qlfresher.fragment.FragmentAddFresher;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        int act = intent.getIntExtra("act", 0);
        if(act == 0)
        {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameAdd, new FragmentAddFresher())
                .commit();
        }
        else
        {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameAdd, new FragmentAddCenter())
                .commit();
        }
    }

    public void navigateBackToMainActivity() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}