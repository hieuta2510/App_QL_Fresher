package edu.ptit.ql_fresher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import edu.ptit.ql_fresher.AddActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.database.SQLiteHelper;
import edu.ptit.ql_fresher.model.Center;

public class FragmentAddCenter extends Fragment {
    private EditText etAcronym, etName, etAddress;
    private Button btConfirm, btCancel;
    private String acronym, name, address;
    private View mView;
    private SQLiteHelper db;
    public FragmentAddCenter() {
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
        mView = inflater.inflate(R.layout.fragment_add_center, container, false);
        initView();
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainActivity();
            }
        });
        return mView;
    }

    private void validateData(){
        name= etName.getText().toString();
        acronym = etAcronym.getText().toString();
        address = etAddress.getText().toString();
         if(acronym.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_acronym), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_address), Toast.LENGTH_SHORT).show();
        }else{
             Center center = new Center(acronym, name, address, 0);
            storeCenter(center);
        }
    }

    private void storeCenter(Center center) {
        db = new SQLiteHelper(getActivity());
        db.addCenter(center);
        Toast.makeText(getActivity(), getResources().getString(R.string.toastSaveCenter), Toast.LENGTH_SHORT).show();
        goBackToMainActivity();
    }

    private void initView() {
        etAcronym = mView.findViewById(R.id.etAcronymCenter);
        etName = mView.findViewById(R.id.etNameCenter);
        etAddress = mView.findViewById(R.id.etAddressCenter);
        btConfirm = mView.findViewById(R.id.btConfimAddCenter);
        btCancel = mView.findViewById(R.id.btnCancelAddCenter);
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof AddActivity) {
            ((AddActivity) getActivity()).navigateBackToMainActivity();
        }
    }
}