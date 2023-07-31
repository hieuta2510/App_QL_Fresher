package edu.ptit.ql_fresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import edu.ptit.ql_fresher.EditDeleteActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.database.SQLiteHelper;
import edu.ptit.ql_fresher.model.User;

public class FragmentProfile extends Fragment implements View.OnClickListener{
    private User user = new User();
    private TextView name, email, dob;
    private TextView userInfo, changePass, phone, facebook, logout;
    protected FirebaseAuth mFirebaseAuth;
    private int check = 0;

    public FragmentProfile(User us)
    {
        user = us;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        userInfo.setOnClickListener(this);
        changePass.setOnClickListener(this);
        phone.setOnClickListener(this);
        facebook.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void init(View view)
    {
        name = view.findViewById(R.id.nameProfile);
        email = view.findViewById(R.id.emailProfile);
        dob = view.findViewById(R.id.dobProfile);
        userInfo = view.findViewById(R.id.personInfoProfile);
        changePass = view.findViewById(R.id.changePassProfile);
        phone = view.findViewById(R.id.phoneProfile);
        facebook = view.findViewById(R.id.facebookProfile);
        logout = view.findViewById(R.id.logoutProfile);
    }

    private void setTextUser()
    {
        name.setText(user.getFullname());
        email.setText("Email: "+ user.getEmail());
        dob.setText("Dob: " + user.getDob());
    }

    @Override
    public void onClick(View v) {
        if (v == userInfo) {
            check = 1;
            Intent intent = new Intent(getActivity(), EditDeleteActivity.class);
            intent.putExtra("act", 2);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if(v == changePass){
            check = 1;
            Intent intent = new Intent(getActivity(), EditDeleteActivity.class);
            intent.putExtra("act", 3);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if(v == phone){

        } else if(v == facebook){

        } else if(v == logout){
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuth.signOut();
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(check == 1) {
            SQLiteHelper db = new SQLiteHelper(getActivity());
            user = db.getUserByEmail(user.getEmail());
            setTextUser();
        } else {
            setTextUser();
        }
    }
}