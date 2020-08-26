package com.puppyTinder.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.puppyTinder.R;


/**
 * Fragment for existing user login
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mEmail, mPassword;
    private Button mLogin;
    private TextView mForgotButton;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_login, container, false);
        else
            container.removeView(view);


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeObjects();
    }

    /**
     * Sends user reset password to entered email
     */
    private void forgotPassword(){
        if (mEmail.getText().toString().trim().length() > 0)
            FirebaseAuth.getInstance().sendPasswordResetEmail(mEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(view.findViewById(R.id.layout), "Reset email sent", Snackbar.LENGTH_LONG).show();
                            }else
                                Snackbar.make(view.findViewById(R.id.layout), "Email not found", Snackbar.LENGTH_LONG).show();
                        }
                    });
    }

    private void login(){
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if(!task.isSuccessful()){
                Snackbar.make(view.findViewById(R.id.layout), "sign in error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgotButton:
                forgotPassword();
                break;
            case R.id.login:
                login();
                break;
        }
    }



    /**
     * Initializes design elements + their clickListeners
     */
    private void initializeObjects(){
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
        mForgotButton = view.findViewById(R.id.forgotButton);
        mLogin = view.findViewById(R.id.login);
        mForgotButton.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }
}