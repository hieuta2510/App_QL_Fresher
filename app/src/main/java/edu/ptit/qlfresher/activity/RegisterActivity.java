package edu.ptit.qlfresher.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etFullName, etDoB;
    private Button btRegister, btCancel;
    protected FirebaseAuth mFirebaseAuth;
    private int mYear, mMonth, mDay;

    public RegisterActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        mFirebaseAuth = FirebaseAuth.getInstance();
        onClick();

    }

    private void onClick() {
        etDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR) / 1000 * 1000;
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePassword(etPassword.getText().toString()) && validateUsername(etEmail.getText().toString())) {
                    SQLiteHelper db = new SQLiteHelper(RegisterActivity.this);
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String fullname = etFullName.getText().toString();
                    String dob = etDoB.getText().toString();
                    db.addUser(new User(email, password, fullname, dob));
                    mFirebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    final Intent data = new Intent();
                                    // Truyền data vào intent
                                    data.putExtra("email", email);
                                    data.putExtra("pass", password);
                                    // Đặt resultCode là Activity.RESULT_OK
                                    setResult(Activity.RESULT_OK, data);
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.toastRegisterSuccess), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.toastRegisterFail), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Password must be more than 6 letters, less than 15 letters and cannot be blank or invalid email", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected boolean validatePassword(String password)
    {
        if(password.isEmpty() || password.length() < 6 || password.length() > 15)
        {
            return false;
        }
        return true;
    }

    protected boolean validateUsername(String email)
    {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }



    private void initView() {
        etEmail = findViewById(R.id.etEmailReg);
        etPassword = findViewById(R.id.etPassReg);
        etFullName = findViewById(R.id.etFullName);
        etDoB = findViewById(R.id.etDoB);
        btRegister = findViewById(R.id.btRegister);
        btCancel = findViewById(R.id.btCancelReg);
    }
}