package edu.ptit.ql_fresher.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ptit.ql_fresher.AddActivity;
import edu.ptit.ql_fresher.EditDeleteActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.database.SQLiteHelper;
import edu.ptit.ql_fresher.model.User;

public class FragmentDetailsUser extends Fragment {
    private EditText etName, etDoB;
    private TextView etEmail;
    private Button btUpdate, btCancel;
    public FragmentDetailsUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        User user = (User) bundle.getSerializable("user");
        init(view);
        setTextUser(user);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper db = new SQLiteHelper(getActivity());
                User user1 = db.getUserByEmail(etEmail.getText().toString());
                user1.setFullname(etName.getText().toString());
                user1.setDob(etDoB.getText().toString());
                db.updateUser(user1);
                Toast.makeText(getActivity(), getResources().getString(R.string.toastChangeUser), Toast.LENGTH_SHORT).show();
                goBackToMainActivity();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainActivity();
            }
        });
        
        etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toastChangeEmail), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(View view)
    {
        etName = view.findViewById(R.id.etNameDetailsUser);
        etEmail = view.findViewById(R.id.etEmailDetailsUser);
        etDoB = view.findViewById(R.id.etDoBDetailsUser);
        btUpdate = view.findViewById(R.id.btUpdateDetailsUser);
        btCancel = view.findViewById(R.id.btCancelDetailsUser);
    }

    private void setTextUser(User user)
    {
        etName.setText(user.getFullname());
        etEmail.setText(user.getEmail());
        etDoB.setText(user.getDob());
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof EditDeleteActivity) {
            ((EditDeleteActivity) getActivity()).navigateBackToFragmentManageCenter();
        }
    }
}