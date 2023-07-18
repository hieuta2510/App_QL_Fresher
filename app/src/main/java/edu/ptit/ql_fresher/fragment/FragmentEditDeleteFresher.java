package edu.ptit.ql_fresher.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import edu.ptit.ql_fresher.R;

public class FragmentEditDeleteFresher extends Fragment {
    private View mView;
    private final static int galleryPick = 1;
    private String downloadImgUrl, randomKey, saveCurDate, saveCurTime;;
    private EditText etName, etEmail, etLang, etCenter, etDoB, etScore1, etScore2, etScore3;
    private TextView tvScore;
    private Button btUpdate, btDelete, btCancel;
    private ImageView img;
    private int check;
    private Uri imageUri;
    private String key = "";
    private DatabaseReference myRef;
    private StorageReference imgRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_delete_fresher, container, false);
        initView();
        check = 0;
        Bundle bundle = getArguments();
        key = bundle.getString("key");
        imgRef= FirebaseStorage.getInstance("gs://qlyfresher.appspot.com/").getReference().child("fresher img");
        myRef= FirebaseDatabase.getInstance("https://qlyfresher-default-rtdb.firebaseio.com/").getReference().child("fresherlist").child(key);
        displayFresher();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1;
                openGallery();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1)
                {
                    storeImg();
                }
                else {
                    editFresher2();
                }
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getResources().getString(R.string.dialogDelTit));
                builder.setMessage(getResources().getString(R.string.dialogDelMes));
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton(getResources().getString(R.string.btCancel), null);
                builder.setPositiveButton(getResources().getString(R.string.dialogDelBtOk), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteFresher();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        return mView;
    }

    private void deleteFresher() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toastDelSuccess), Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void storeImg(){
        Calendar c=Calendar.getInstance();
        SimpleDateFormat curDate=new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate=curDate.format(c.getTime());
        SimpleDateFormat curTime=new SimpleDateFormat("HH:mm:ss");
        saveCurTime=curTime.format(c.getTime());
        randomKey=saveCurDate+"-"+saveCurTime;
        StorageReference filePath=imgRef.child(
                imageUri.getLastPathSegment()+randomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toastDelSuccess) + ": " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImgUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl=task.getResult().toString();
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.toastSaveURL), Toast.LENGTH_SHORT).show();
                        editFresher();
                    }
                });
            }
        });
    }

    private void editFresher() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String lang = etLang.getText().toString();
        String center = etCenter.getText().toString();
        String dob = etDoB.getText().toString();
        String score = tvScore.getText().toString().substring(7);
        if (name.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();
        }else if (lang.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_lang), Toast.LENGTH_SHORT).show();
        }else if (center.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_center), Toast.LENGTH_SHORT).show();
        }else if (dob.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_dob), Toast.LENGTH_SHORT).show();
        } else {
            if (!etScore1.getText().toString().isEmpty()
                    || !etScore2.getText().toString().isEmpty()
                    || !etScore3.getText().toString().isEmpty()) {
                score = scoreFresher();
            }
            HashMap<String, Object> fresher = new HashMap<>();
            fresher.put("key", key);
            fresher.put("name", name);
            fresher.put("email", email);
            fresher.put("language", lang);
            fresher.put("center", center);
            fresher.put("dateOfBirth", dob);
            fresher.put("score", score);
            fresher.put("image", downloadImgUrl);
            myRef.updateChildren(fresher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                }
            });
        }
    }

    private void editFresher2() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String lang = etLang.getText().toString();
        String center = etCenter.getText().toString();
        String dob = etDoB.getText().toString();
        String score = tvScore.getText().toString().substring(7);
        if (name.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();
        }else if (lang.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_lang), Toast.LENGTH_SHORT).show();
        }else if (center.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_center), Toast.LENGTH_SHORT).show();
        }else if (dob.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_dob), Toast.LENGTH_SHORT).show();
        }else if (score.isEmpty()) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_score), Toast.LENGTH_SHORT).show();
        } else {
            if (!etScore1.getText().toString().isEmpty()
                    || !etScore2.getText().toString().isEmpty()
                    || !etScore3.getText().toString().isEmpty()) {
                score = scoreFresher();
            }
            HashMap<String, Object> fresher = new HashMap<>();
            fresher.put("key", key);
            fresher.put("name", name);
            fresher.put("email", email);
            fresher.put("language", lang);
            fresher.put("center", center);
            fresher.put("dateOfBirth", dob);
            fresher.put("score", score);
            myRef.updateChildren(fresher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                }
            });
        }
    }

    private void displayFresher() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String email=snapshot.child("email").getValue().toString();
                    String language=snapshot.child("language").getValue().toString();
                    String center=snapshot.child("center").getValue().toString();
                    String dob=snapshot.child("dateOfBirth").getValue().toString();
                    String score=snapshot.child("score").getValue().toString();
                    String image=snapshot.child("image").getValue().toString();
                    etName.setText(name);
                    etEmail.setText(email);
                    etLang.setText(language);
                    etCenter.setText(center);
                    etDoB.setText(dob);
                    tvScore.setText("Score: " + score);
                    Picasso.get().load(image).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }

    private String scoreFresher()
    {
        double score = 0.00F;
        double score1 = 0, score2 = 0, score3 = 0;
        int count  = 0;
        if(!etScore1.getText().toString().isEmpty())
        {
            score1 = Double.parseDouble(etScore1.getText().toString());
            count++;

        }
        if(!etScore2.getText().toString().isEmpty())
        {
            score2 = Float.parseFloat(etScore2.getText().toString());
            count++;

        }
        if(!etScore3.getText().toString().isEmpty())
        {
            score3 = Float.parseFloat(etScore3.getText().toString());
            count++;

        }
        score = (score1 + score2 + score3) / count;
        
        return String.format("%.2f", score);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==getActivity().RESULT_OK && data!=null){
            imageUri=data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void initView() {
        etName = mView.findViewById(R.id.etNameEdit);
        etEmail = mView.findViewById(R.id.etEmailEdit);
        etLang = mView.findViewById(R.id.etLangEdit);
        etCenter = mView.findViewById(R.id.etCenterEdit);
        etDoB = mView.findViewById(R.id.etDoBEdit);
        tvScore = mView.findViewById(R.id.tvScoreEdit);
        etScore1 = mView.findViewById(R.id.etScoreEdit1);
        etScore2 = mView.findViewById(R.id.etScoreEdit2);
        etScore3 = mView.findViewById(R.id.etScoreEdit3);
        img=mView.findViewById(R.id.imgEdit);
        btUpdate = mView.findViewById(R.id.btUpdateEdit);
        btDelete = mView.findViewById(R.id.btDeleteEdit);
        btCancel = mView.findViewById(R.id.btCancelEdit);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}