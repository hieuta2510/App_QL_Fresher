package edu.ptit.qlfresher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ptit.qlfresher.activity.EditDeleteActivity;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.User;

public class FragmentChangePass extends Fragment {
    private EditText etOldPass, etNewPass, etConfirmPass;
    private Button btUpdate, btCancel;
    private User user = new User();
    private String oldPass = "", newPass,  confirmPass;

    public FragmentChangePass() {
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
        return inflater.inflate(R.layout.fragment_change_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        init(view);
        user = (User) bundle.getSerializable("user");
        oldPass = user.getPassword();
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass = etNewPass.getText().toString();
                confirmPass = etConfirmPass.getText().toString();
                if(validatePass())
                {
                    SQLiteHelper db = new SQLiteHelper(getActivity());
                    User user1 = db.getUserByEmail(user.getEmail());
                    user1.setPassword(newPass);
                    db.updateUser(user1);
                    Toast.makeText(getActivity(), getResources().getString(R.string.toastChangeUser), Toast.LENGTH_SHORT).show();
                    FirebaseUser userFire = FirebaseAuth.getInstance().getCurrentUser();
                    userFire.updatePassword(newPass).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    } else {
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e);
                                }
                            });
                    goBackToMainActivity();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainActivity();
            }
        });
    }
    private void init(View view)
    {
        etOldPass = view.findViewById(R.id.etOldPass);
        etNewPass = view.findViewById(R.id.etNewPass);
        etConfirmPass = view.findViewById(R.id.etConfirmPass);
        btUpdate = view.findViewById(R.id.btUpdatePass);
        btCancel = view.findViewById(R.id.btCancelPass);
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof EditDeleteActivity) {
            ((EditDeleteActivity) getActivity()).navigateBackToFragmentManageCenter();
        }
    }

    private boolean validatePass()
    {
        if(!etOldPass.getText().toString().equals(oldPass))
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.toastIncorrectOldPass), Toast.LENGTH_SHORT).show();
            return false;
        } else
        {
            if(!newPass.equals(confirmPass))
            {
                Toast.makeText(getActivity(), getResources().getString(R.string.toastIncorrectConfirmPass), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}