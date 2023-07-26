package edu.ptit.ql_fresher.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.ptit.ql_fresher.EditDeleteActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.database.SQLiteHelper;
import edu.ptit.ql_fresher.model.Center;

public class FragmentEditDeleteCenter extends Fragment {
    private View mView;
    private Center center;
    private TextView tvDetailsCenter;
    private EditText etAcronym, etName, etAddress;
    private String acronym, name, address;
    private int id = 1, totalFr = 0;
    private Button btUpdate, btDelete, btCancel;
    private SQLiteHelper db;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_delete_center, container, false);
        initView();
        db = new SQLiteHelper(getActivity());
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        center = db.getCenterById(Integer.toString(id));
        etAcronym.setText(center.getAcronym());
        etName.setText(center.getName());
        etAddress.setText(center.getAddress());
        tvDetailsCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDetailsCenter fragmentDetailsCenter = new FragmentDetailsCenter();
                Bundle bundle = new Bundle();
                bundle.putSerializable("center", center);
                fragmentDetailsCenter.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameEditDelete, fragmentDetailsCenter)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getResources().getString(R.string.dialogDelCenTit));
                builder.setMessage(getResources().getString(R.string.dialogDelCenMes));
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton(getResources().getString(R.string.btCancel), null);
                builder.setPositiveButton(getResources().getString(R.string.dialogDelBtOk), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteCenter(Integer.toString(id));
                        goBackToMainActivity();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
        acronym = etAcronym.getText().toString();
        name= etName.getText().toString();
        address = etAddress.getText().toString();
        if(acronym.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_acronym), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_address), Toast.LENGTH_SHORT).show();
        }else{
            Center center2 = new Center(center.getId(),acronym, name, address, center.getTotalFresher());
            updateCenter(center2);
        }
    }

    private void updateCenter(Center center) {
        db = new SQLiteHelper(getActivity());
        db.updateCenter(center);
        Toast.makeText(getActivity(), getResources().getString(R.string.toastSaveCenter), Toast.LENGTH_SHORT).show();
        goBackToMainActivity();
    }

    private void initView() {
        tvDetailsCenter = mView.findViewById(R.id.tvDetailsCenter);
        etAcronym = mView.findViewById(R.id.etEditAcronymCenter);
        etName = mView.findViewById(R.id.etEditNameCenter);
        etAddress = mView.findViewById(R.id.etEditAddressCenter);
        btUpdate = mView.findViewById(R.id.btUpdateCenterEdit);
        btDelete = mView.findViewById(R.id.btDeleteCenterEdit);
        btCancel = mView.findViewById(R.id.btCancelCenterEdit);
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof EditDeleteActivity) {
            ((EditDeleteActivity) getActivity()).navigateBackToFragmentManageCenter();
        }
    }
}