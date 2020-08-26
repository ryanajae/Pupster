package com.puppyTinder.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.puppyTinder.R;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Fragment responsible for registering a new user
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText mBirthday, mPupName, mName,
            mEmail,
            mPassword;
    CheckBox mCheckbox;;
    SegmentedButtonGroup mRadioGroup, mRadioGroupInterest, mRadioGroupEnergy;
    private String birthdate;
    Button mRegister;

    private int bdayMonth, bdayYear, bdayDay;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_registration, container, false);
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
     * Initializes the design Elements and calls clickListeners for them
     */
    private void initializeObjects(){

        mRadioGroup = view.findViewById(R.id.radioRealButtonGroup);
        mRadioGroupInterest = view.findViewById(R.id.interestReg);
        mRadioGroupEnergy = view.findViewById(R.id.play_style);
        mBirthday = view.findViewById(R.id.pup_birthay);
        mPupName = view.findViewById(R.id.pup_name);
//        mNextButton = view.findViewById(R.id.regNext1);
        mEmail = view.findViewById(R.id.email);
        mName = view.findViewById(R.id.name);
        mPassword = view.findViewById(R.id.password);
        mCheckbox = view.findViewById(R.id.checkboxReg);
        mRegister = view.findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        TextView interString = (TextView) view.findViewById(R.id.intertString);
        interString.setTypeface(mBirthday.getTypeface());

        Log.w("Font is", mBirthday.getTypeface().toString());
        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                Log.w(TAG, "onDateSet: mm/dd/yy: " + month + '/' + day + '/' + year);
                birthdate = month + "/"+ day + "/" + year;
                mBirthday.setText(birthdate);
                datePicker.updateDate(year, month, day);
            }
        };

//        mRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Boolean shouldReturn = false;
//                if (mBirthday.getText().length() == 0 || birthdate == null) {
//                    mBirthday.setError("Please fill out birthday");
//                    shouldReturn = true;
//                }
//                if (mPupName.getText().length() == 0) {
//                    mPupName.setError("Please fill out name");
//                    shouldReturn = true;
//                }
//                if (mEmail.getText().length() == 0) {
//                    mEmail.setError("Please fill out email");
//                    return;
//                }
//                if (mName.getText().length() == 0) {
//                    mName.setError("Please fill out name");
//                    return;
//                }
//                if (mPassword.getText().length() == 0) {
//                    mPassword.setError("Please fill out password");
//                    return;
//                }
//                if (mPassword.getText().length() < 6) {
//                    mPassword.setError("Password must have at least 6 characters");
//                    return;
//                }
//                if (!mCheckbox.isChecked()) {
//                    mCheckbox.setError("Please check box after reading statement");
//                    return;
//                }
//                if (shouldReturn) return;
//                RegisterFragment1 nextFrag= new RegisterFragment1();
////                Gson gson = new Gson();
////                String birthdateJSON = gson.toJson(birthdate);
//                final String accountType;
//                switch (mRadioGroup.getPosition()) {
//                    case 1:
//                        accountType = "Female";
//                        break;
//                    default:
//                        accountType = "Male";
//                }
//                final String interest;
//                switch (mRadioGroupInterest.getPosition()) {
//                    case 0:
//                        interest = "Male";
//                        break;
//                    case 1:
//                        interest = "Female";
//                        break;
//                    default:
//                        interest = "Both";
//                }
//                final String energy;
//                switch (mRadioGroupEnergy.getPosition()) {
//                    case 0:
//                        energy = "Low";
//                        break;
//                    case 2:
//                        energy = "High";
//                        break;
//                    default:
//                        energy = "Medium";
//                }
//                Bundle args = new Bundle();
//                args.putSerializable("Birthday", birthdate);
//                args.putString("userGender", accountType);
//                args.putString("Interest", interest);
//                args.putString("Energy", energy);
//                nextFrag.setArguments(args);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
    }
    private void register() {
        Boolean shouldReturn = false;
        if (mBirthday.getText().length() == 0 || birthdate == null) {
            mBirthday.setError("Please fill out birthday");
            shouldReturn = true;
        }
        if (mPupName.getText().length() == 0) {
            mPupName.setError("Please fill out name");
            shouldReturn = true;
        }
        if (mEmail.getText().length() == 0) {
            mEmail.setError("Please fill out email");
            shouldReturn = true;
        }
        if (mName.getText().length() == 0) {
            mName.setError("Please fill out name");
            shouldReturn = true;
        }
        if (mPassword.getText().length() == 0) {
            mPassword.setError("Please fill out password");
            shouldReturn = true;
        }
        if (mPassword.getText().length() < 6) {
            mPassword.setError("Password must have at least 6 characters");
            shouldReturn = true;
        }
        if (!mCheckbox.isChecked()) {
            mCheckbox.setError("Please check box after reading statement");
            shouldReturn = true;
        }
        if (shouldReturn) return;
//        RegisterFragment1 nextFrag= new RegisterFragment1();
//                Gson gson = new Gson();
//                String birthdateJSON = gson.toJson(birthdate);
        final String accountType;
        switch (mRadioGroup.getPosition()) {
            case 1:
                accountType = "Female";
                break;
            default:
                accountType = "Male";
        }
        final String interest;
        switch (mRadioGroupInterest.getPosition()) {
            case 0:
                interest = "Male";
                break;
            case 1:
                interest = "Female";
                break;
            default:
                interest = "Both";
        }
        final String energy;
        switch (mRadioGroupEnergy.getPosition()) {
            case 0:
                energy = "Low";
                break;
            case 2:
                energy = "High";
                break;
            default:
                energy = "Medium";
        }
        final String humName = mName.getText().toString();
        final String pupName = mPupName.getText().toString();
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Snackbar.make(view.findViewById(R.id.layout), "Registration Error", Snackbar.LENGTH_SHORT).show();
                } else {
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map userInfo = new HashMap();
                    userInfo.put("humName", humName);
                    userInfo.put("pupName", pupName);
                    userInfo.put("sex", accountType);
                    userInfo.put("search_distance", 100);
                    userInfo.put("profileImageUrl", "default");
                    userInfo.put("birthdate", birthdate);
                    userInfo.put("interest", interest);
                    userInfo.put("energy", energy);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).updateChildren(userInfo);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view.findViewById(R.id.layout), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                register();;
        }
    }
}