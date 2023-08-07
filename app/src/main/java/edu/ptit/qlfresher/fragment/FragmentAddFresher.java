package edu.ptit.qlfresher.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import edu.ptit.qlfresher.activity.AddActivity;
import edu.ptit.qlfresher.receiver.MyReceiver;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;
import edu.ptit.qlfresher.model.Fresher;

public class FragmentAddFresher extends Fragment {
    private static final String TAG = "To";
    private EditText etName, etEmail, etLang, etCenter, etDoB;
    private Button btConfirm, btCancel;
    private ImageView img;
    private final static int galleryPick=1;
    private Uri imageUri;
    private String saveCurDate,saveCurTime;
    private String downloadImgUrl,randomKey;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    private Integer NumWork = new Random().nextInt();
    private String keytodo = Integer.toString(NumWork);
    private String name, email, language, center, dob;
    private int mYear, mMonth, mDay;
    private View mView;
    private SQLiteHelper db;
    public FragmentAddFresher() {
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
        mView = inflater.inflate(R.layout.fragment_add_fresher, container, false);
        initView();
        imgRef= FirebaseStorage.getInstance("gs://qlyfresher.appspot.com/").getReference().child("fresher img");
        myRef= FirebaseDatabase.getInstance("https://qlyfresher-default-rtdb.firebaseio.com/").getReference().child("fresherlist");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
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
        email = etEmail.getText().toString();
        language = etLang.getText().toString();
        center = etCenter.getText().toString().toUpperCase();
        if (center.isEmpty()) center = "None";
        dob = etDoB.getText().toString();
        if(imageUri==null){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_img), Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();
        }else if(language.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_lang), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        }else if(dob.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.val_dob), Toast.LENGTH_SHORT).show();
        }else{
            checkExistFresher(email);
        }
    }

    private void checkExistFresher(String email)
    {
        List<String> mList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    mList.add(fresher.getEmail());
                }
                checkExist(mList, email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExist(List<String> mList, String email) {
        boolean check = false;
        for (String i : mList) {
            if ((i.equals(email)))
            {
                check = true;
                break;
            }
        }
        if (check == false){
            storeFresher();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_fresher_exists), Toast.LENGTH_SHORT).show();
        }
    }

    private void storeFresher(){
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
                        "Lỗi: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),
                        "Thêm fresher thành công!", Toast.LENGTH_SHORT).show();
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
                                "Lưu URL thành công", Toast.LENGTH_SHORT).show();
                        saveFresherToDatabase();
                    }
                });
            }
        });
    }

    private void saveFresherToDatabase(){
        HashMap<String,Object> fresher=new HashMap<>();
        fresher.put("key",TAG+randomKey);
        fresher.put("name",name);
        fresher.put("email", email);
        fresher.put("language", language);
        fresher.put("center", center);
        fresher.put("dateOfBirth", dob);
        fresher.put("score", "0");
        fresher.put("image",downloadImgUrl);
        myRef.child(TAG+randomKey).updateChildren(fresher)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(getActivity(),
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotifyAddFresher");
                            intent.putExtra("fresherName",name);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                                    3, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            Center center2 = db.getCenterByAcronym(center);
                            center2.setTotalFresher(center2.getTotalFresher()+1);
                            db.updateCenter(center2);
                            goBackToMainActivity();
                        }else{
                            Toast.makeText(getActivity(),
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                            goBackToMainActivity();
                        }
                    }
                });
    }
    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
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
        etName = mView.findViewById(R.id.etNameAddFR);
        etEmail = mView.findViewById(R.id.etEmailAddFR);
        etLang = mView.findViewById(R.id.etLangAddFR);
        etCenter = mView.findViewById(R.id.etCenterAddFR);
        etDoB = mView.findViewById(R.id.etDoBAddFR);
        btConfirm = mView.findViewById(R.id.btnSaveTaskFR);
        btCancel = mView.findViewById(R.id.btnCancelFR);
        img= mView.findViewById(R.id.imgFR);
        db = new SQLiteHelper(getActivity());
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof AddActivity) {
            ((AddActivity) getActivity()).navigateBackToMainActivity();
        }
    }
}