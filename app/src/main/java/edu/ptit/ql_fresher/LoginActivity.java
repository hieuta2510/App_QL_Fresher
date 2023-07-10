package edu.ptit.ql_fresher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btLogin;
    private EditText etEmail, etPass;
    private TextView tvForgetPass;
    protected FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mFirebaseAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signInWithEmailAndPassword(
                                etEmail.getText().toString(), etPass.getText().toString()).
                        addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.toastSuccess), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                intent.putExtra("email", etEmail.getText().toString());
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.toastError), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEmail.getText().toString().isEmpty())
                {
                    mFirebaseAuth.sendPasswordResetEmail(etEmail.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.toastForgotPass), Toast.LENGTH_LONG).show();
                                    } else {

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this,  getResources().getString(R.string.toastFillEmailForgot), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        btLogin =findViewById(R.id.btLoginLog);
        etEmail =findViewById(R.id.etEmailReg);
        etPass =findViewById(R.id.etPassReg);
        tvForgetPass =findViewById(R.id.forgotPassword);
    }
}