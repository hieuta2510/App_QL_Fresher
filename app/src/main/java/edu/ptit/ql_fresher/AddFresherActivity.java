package edu.ptit.ql_fresher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddFresherActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
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
                onBackPressed();
            }
        });
    }

    private void validateData(){
        name= etName.getText().toString();
        email = etEmail.getText().toString();
        language = etLang.getText().toString();
        center = etCenter.getText().toString();
        if(imageUri==null){
            Toast.makeText(this, getResources().getString(R.string.val_img), Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();
        }else if(center.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.val_center), Toast.LENGTH_SHORT).show();
        }else if(language.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.val_lang), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        }else{
            storeWork();
        }
    }
    private void storeWork(){
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
                Toast.makeText(AddFresherActivity.this,
                        "Lỗi: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddFresherActivity.this,
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
                        Toast.makeText(AddFresherActivity.this,
                                "Lưu URL thành công", Toast.LENGTH_SHORT).show();
                        saveWorktoDatabase();
                    }
                });
            }
        });
    }

    private void saveWorktoDatabase(){
        HashMap<String,Object> product=new HashMap<>();
        product.put("key",TAG+randomKey);
        product.put("name",name);
        product.put("email", email);
        product.put("language", language);
        product.put("center", center);
        product.put("dateOfBirth", dob);
        product.put("score", "0");
        product.put("image",downloadImgUrl);
        myRef.child(TAG+randomKey).updateChildren(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(AddFresherActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name",name);
                            intent.putExtra("Email",email);
                            intent.putExtra("Language", language);
                            intent.putExtra("Center", center);
                            intent.putExtra("DateOfBirth", dob);
                            intent.putExtra("Score", email);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddFresherActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            Intent intent1=new Intent(AddFresherActivity.this,
                                    MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(AddFresherActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddFresherActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void initView() {
        etName = findViewById(R.id.etNameAdd);
        etEmail = findViewById(R.id.etEmailAdd);
        etLang = findViewById(R.id.etLangAdd);
        etCenter = findViewById(R.id.etCenterAdd);
        etDoB = findViewById(R.id.etDoBAdd);
        btConfirm = findViewById(R.id.btnSaveTask);
        btCancel = findViewById(R.id.btnCancel);
        img=findViewById(R.id.img);
    }
}