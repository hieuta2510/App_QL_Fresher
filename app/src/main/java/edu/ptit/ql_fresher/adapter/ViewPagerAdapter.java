package edu.ptit.ql_fresher.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.ptit.ql_fresher.fragment.FragmentDashboard;
import edu.ptit.ql_fresher.fragment.FragmentHome;
import edu.ptit.ql_fresher.fragment.FragmentManageCenter;
import edu.ptit.ql_fresher.fragment.FragmentManageFresher;
import edu.ptit.ql_fresher.fragment.FragmentProfile;
import edu.ptit.ql_fresher.model.User;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private User user = new User();
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, User user) {
        super(fm, behavior);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new FragmentManageFresher();
            case 1:return new FragmentManageCenter();
            case 2:return new FragmentHome();
            case 3:return new FragmentDashboard();
            case 4:return new FragmentProfile(user);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}