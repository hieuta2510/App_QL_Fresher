package edu.ptit.qlfresher.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ptit.qlfresher.activity.EditDeleteActivity;
import edu.ptit.qlfresher.model.Fresher;
import edu.ptit.qlfresher.receiver.MyReceiver;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;

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
    private SQLiteHelper db;
    private int mYear, mMonth, mDay;
    private String oldCenterName = "";

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
        oldCenterName = bundle.getString("oldCenterName");
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

        etDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR) / 1000 * 1000;
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String day = String.valueOf(dayOfMonth), mon = String.valueOf(month);
                                if(dayOfMonth < 10) day = "0" + dayOfMonth;
                                if(month < 9) mon = "0" + month;
                                String ns = day + "/" + mon + "/" + year;
                                etDoB.setText(ns);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
                    validateData(false);
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
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(getActivity(),
                                MyReceiver.class);
                        intent.putExtra("myAction", "mDoNotifyDeleteFresher");
                        intent.putExtra("fresherName",etName.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                                5, intent, 0);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
                goBackToMainActivity();
            }
        });
        return mView;
    }

    private void deleteFresher() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateCenterTotalFresherDel();
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toastDelSuccess), Toast.LENGTH_SHORT).show();
                goBackToMainActivity();
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
                        getResources().getString(R.string.toastFail) + ": " + message, Toast.LENGTH_SHORT).show();
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
                        validateData(true);
                    }
                });
            }
        });
    }

    private void validateData(boolean editedImage){
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String lang = etLang.getText().toString();
        String center = etCenter.getText().toString().trim().toUpperCase();
        String dob = etDoB.getText().toString();
        String score = tvScore.getText().toString().substring(7);
        if (name.isEmpty() || !validateName(name)) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_name), Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty() || !validateEmail(email)) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();
        }else if (lang.isEmpty() || !validateLang(lang)) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_lang), Toast.LENGTH_SHORT).show();
        }else if (center.isEmpty() || !validateCenter(center)) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_center), Toast.LENGTH_SHORT).show();
        }else if (dob.isEmpty() || !validateDoB(dob, "dd/MM/yyyy")) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_dob), Toast.LENGTH_SHORT).show();
        } else{
            if (!etScore1.getText().toString().isEmpty()
                    || !etScore2.getText().toString().isEmpty()
                    || !etScore3.getText().toString().isEmpty()) {
                score = scoreFresher(etScore1.getText().toString(), etScore2.getText().toString(), etScore3.getText().toString());
            }
            Fresher fresher = new Fresher();
            if (editedImage == true)
            {
                fresher = new Fresher(key, name, email, lang, center, dob, downloadImgUrl, score);
                editFresherClickImage(fresher);
            }
            else
            {
                fresher = new Fresher(key, name, email, lang, center, dob,"", score);
                editFresherNoImage(fresher);
            }
        }
    }

    private void editFresherClickImage(Fresher editedFresher) {
        HashMap<String, Object> fresher = new HashMap<>();
            fresher.put("key", key);
            fresher.put("name", editedFresher.getName());
            fresher.put("email", editedFresher.getEmail());
            fresher.put("language", editedFresher.getLanguage().toUpperCase());
            fresher.put("center", editedFresher.getCenter());
            fresher.put("dateOfBirth", editedFresher.getDateOfBirth());
            fresher.put("score", editedFresher.getScore());
            fresher.put("image", downloadImgUrl);

            myRef.updateChildren(fresher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(getActivity(),
                                MyReceiver.class);
                        intent.putExtra("myAction", "mDoNotifyUpdateFresher");
                        intent.putExtra("fresherName",etName.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                                4, intent, 0);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        updateCenterTotalFresherEdit(editedFresher.getCenter());
                        goBackToMainActivity();
                    }
                }
            });
    }

    private void editFresherNoImage(Fresher editedFresher) {
            HashMap<String, Object> fresher = new HashMap<>();
            fresher.put("key", key);
            fresher.put("name", editedFresher.getName());
            fresher.put("email", editedFresher.getEmail());
            fresher.put("language", editedFresher.getLanguage().toUpperCase());
            fresher.put("center", editedFresher.getCenter());
            fresher.put("dateOfBirth", editedFresher.getDateOfBirth());
            fresher.put("score", editedFresher.getScore());
            myRef.updateChildren(fresher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(getActivity(),
                                MyReceiver.class);
                        intent.putExtra("myAction", "mDoNotifyUpdateFresher");
                        intent.putExtra("fresherName",etName.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                                4, intent, 0);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        updateCenterTotalFresherEdit(editedFresher.getCenter());
                        goBackToMainActivity();
                    }
                }
            });
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

    private void updateCenterTotalFresherEdit(String centerName)
    {
        if (!centerName.equals(oldCenterName)){
            // thay doi so luong fresher center moi
            Center center = db.getCenterByAcronym(centerName);
            center.setTotalFresher(center.getTotalFresher()+1);
            db.updateCenter(center);

            //thay doi so luong fresher center cu
            center = db.getCenterByAcronym(oldCenterName);
            center.setTotalFresher(center.getTotalFresher()-1);
            db.updateCenter(center);
        }
    }

    private void updateCenterTotalFresherDel()
    {
            db = new SQLiteHelper(getActivity());
            Center center = db.getCenterByAcronym(oldCenterName);
            center.setTotalFresher(center.getTotalFresher()-1);
            db.updateCenter(center);
    }

    protected static boolean validateDoB(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        try {
            LocalDate parsedDate = LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    protected static boolean validateName(String name) {
        String regex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?0-9]"; // Biểu thức chính quy để kiểm tra ký tự đặc biệt hoặc số
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return !matcher.find();
    }

    protected boolean validateEmail(String email)
    {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    protected boolean validateCenter(String centerName)
    {
        List<Center> list = db.getAllCenter();
        for (Center i : list){
            if(i.getAcronym().equals(centerName)){
                return true;
            }
        }
        return false;
    }

    protected boolean validateLang(String language)
    {
        String[] list = getActivity().getResources().getStringArray(R.array.spProgramLang);
        for(int i = 0;i < list.length;i++)
        {
            if(language.trim().equalsIgnoreCase(list[i]))
            {
                return true;
            }
        }
        return false;
    }

    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }

    protected String scoreFresher(String score1, String score2, String score3)
    {
        double score = 0.00F;
        double sc1 = 0, sc2 = 0, sc3 = 0;
        int count  = 0;
        if(!score1.isEmpty())
        {
            sc1 = Double.parseDouble(score1);
            count++;
        }
        if(!score2.isEmpty())
        {
            sc2 = Double.parseDouble(score2);
            count++;
        }
        if(!score3.isEmpty())
        {
            sc3 = Double.parseDouble(score3);
            count++;
        }

        score = (sc1 + sc2 + sc3) / count;
        
        return String.format("%.2f", score).replace(",", ".");
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
        db = new SQLiteHelper(getActivity());
    }

    private void goBackToMainActivity() {
        if (getActivity() instanceof EditDeleteActivity) {
            ((EditDeleteActivity) getActivity()).navigateBackToFragmentManageCenter();
        }
    }
}