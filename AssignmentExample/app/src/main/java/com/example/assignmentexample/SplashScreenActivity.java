package com.example.assignmentexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.assignmentexample.Common.Common;
import com.example.assignmentexample.Model.UserInfoModel;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashScreenActivity extends AppCompatActivity {

    private static int LOGIN_REQUEST_CODE = 7171;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    FirebaseDatabase database;
    DatabaseReference userInfoRef;

    @Override
    protected void onStart() {
        super.onStart();
        delaySplashScreen();
    }

    @Override
    protected void onStop() {
        if (firebaseAuth != null && listener != null) {
            firebaseAuth.removeAuthStateListener(listener);
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        userInfoRef = database.getReference(Common.USER_INFO_REFERENCE);

        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        listener = myFirebaseAuth -> {
            FirebaseUser user = myFirebaseAuth.getCurrentUser();
            if (user != null) {
                checkUserFromFirebase();
            }
            else {
                showLoginLayout();
            }
        };
    }

    private void checkUserFromFirebase() {
        userInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //Toast.makeText(SplashScreenActivity.this, "User already register", Toast.LENGTH_SHORT).show();
                            UserInfoModel userInfoModel = snapshot.getValue(UserInfoModel.class);
                            goToMainActivity(userInfoModel);
                        }
                        else {
                            showRegisterLayout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SplashScreenActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity(UserInfoModel userInfoModel) {
        Common.currentUser = userInfoModel; // init value
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }

    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);

        TextInputEditText edt_first_name = itemView.findViewById(R.id.edt_first_name);
        TextInputEditText edt_last_name = itemView.findViewById(R.id.edt_last_name);
        TextInputEditText edt_phone_number = itemView.findViewById(R.id.edt_phone_number);

        Button btn_continue = itemView.findViewById(R.id.btn_register);

        //Set data
        if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null &&
        !TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
            edt_phone_number.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }

        //Set View
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btn_continue.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edt_first_name.getText().toString())) {
                Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (TextUtils.isEmpty(edt_last_name.getText().toString())) {
                Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (TextUtils.isEmpty(edt_phone_number.getText().toString())) {
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                UserInfoModel model = new UserInfoModel();
                model.setFirstName(edt_first_name.getText().toString());
                model.setLastName(edt_last_name.getText().toString());
                model.setPhoneNumber(edt_phone_number.getText().toString());

                userInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(model)
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    goToMainActivity(model);
                });
            }
        });
    }

    private void showLoginLayout() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.layout_sign_in)
                .setPhoneButtonId(R.id.btn_phone_sign_in)
                .setGoogleButtonId(R.id.btn_google_sign_in)
                .build();

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .build(), LOGIN_REQUEST_CODE);
    }

    private void delaySplashScreen() {
        progress_bar.setVisibility(View.VISIBLE);

        Completable.timer(3, TimeUnit.SECONDS,
                AndroidSchedulers.mainThread())
                .subscribe(() ->

                        // After show splash screen, ask login if not login
                        firebaseAuth.addAuthStateListener(listener)
                );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else {
                Toast.makeText(this, "[ERROR]: " + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
