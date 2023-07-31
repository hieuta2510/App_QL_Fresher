package edu.ptit.ql_fresher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import edu.ptit.ql_fresher.fragment.FragmentChangePass;
import edu.ptit.ql_fresher.fragment.FragmentDetailsUser;
import edu.ptit.ql_fresher.fragment.FragmentEditDeleteCenter;
import edu.ptit.ql_fresher.fragment.FragmentEditDeleteFresher;
import edu.ptit.ql_fresher.model.User;

public class EditDeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        Intent intent = getIntent();
        int act = intent.getIntExtra("act", 0);
        Bundle bundle = new Bundle();
        if(act == 0) { // fresher
            String key = intent.getStringExtra("key");
            String oldCenterName = intent.getStringExtra("oldCenterName");
            bundle.putString("key", key);
            bundle.putString("oldCenterName", oldCenterName);
            FragmentEditDeleteFresher editDeleteFresher = new FragmentEditDeleteFresher();
            editDeleteFresher.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, editDeleteFresher)
                    .commit();
        } else if(act == 1){ // center
            int id = intent.getIntExtra("id", 0);
            bundle.putInt("id", id);
            FragmentEditDeleteCenter editDeleteCenter = new FragmentEditDeleteCenter();
            editDeleteCenter.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, editDeleteCenter)
                    .commit();
        } else if(act == 2){ //profile
            User user = (User) intent.getSerializableExtra("user");
            bundle.putSerializable("user", user);
            FragmentDetailsUser fragmentDetailsUser = new FragmentDetailsUser();
            fragmentDetailsUser.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, fragmentDetailsUser)
                    .commit();
        } else { //change pass
            User user = (User) intent.getSerializableExtra("user");
            bundle.putSerializable("user", user);
            FragmentChangePass fragmentChangePass = new FragmentChangePass();
            fragmentChangePass.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameEditDelete, fragmentChangePass)
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