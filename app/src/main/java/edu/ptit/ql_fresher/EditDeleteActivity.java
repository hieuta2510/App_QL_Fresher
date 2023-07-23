package edu.ptit.ql_fresher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import edu.ptit.ql_fresher.fragment.FragmentEditDeleteCenter;
import edu.ptit.ql_fresher.fragment.FragmentEditDeleteFresher;

public class EditDeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        Intent intent = getIntent();
        int act = intent.getIntExtra("act", 0);
        Bundle bundle = new Bundle();
        if(act == 0) {
            String key = intent.getStringExtra("key");
            bundle.putString("key", key);
            FragmentEditDeleteFresher editDeleteFresher = new FragmentEditDeleteFresher();
            editDeleteFresher.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, editDeleteFresher)
                    .commit();
        } else {
            int id = intent.getIntExtra("id", 0);
            bundle.putInt("id", id);
            FragmentEditDeleteCenter editDeleteCenter = new FragmentEditDeleteCenter();
            editDeleteCenter.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, editDeleteCenter)
                    .commit();
        }
    }

    public void navigateBackToFragmentManageCenter() {
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