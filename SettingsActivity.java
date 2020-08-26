package com.puppyTinder.Activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.fluidslider.FluidSlider;
import com.puppyTinder.Objects.UserObject;
import com.puppyTinder.Login.AuthenticationActivity;
import com.puppyTinder.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity that control the search settings of the user:
 *  -Search interest
 *  -Search Distance
 *
 *  Also has option to log out the user
 */
public class SettingsActivity extends AppCompatActivity {

    private SegmentedButtonGroup mRadioGroup;
    FluidSlider mSlider;
    private Button mLogOut;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    UserObject mUser = new UserObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRadioGroup = findViewById(R.id.radioRealButtonGroup);
        mSlider = findViewById(R.id.fluidSlider);
        mLogOut = findViewById(R.id.logOut);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        getUserInfo();

        mLogOut.setOnClickListener(v -> logOut());

    }


    /**
     * Fetch user search settings and populates elements
     */
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUser.parseObject(dataSnapshot);

                mSlider.setPosition(mUser.getSearchDistance() / 100);

                switch(mUser.getInterest()){
                    case "Male":
                        mRadioGroup.setPosition(0, false);
                        break;
                    case "Female":
                        mRadioGroup.setPosition(1, false);
                        break;
                    default:
                        mRadioGroup.setPosition(2, false);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Saves user search settings to the database
     */
    private void saveUserInformation() {
        String interest = "Both";

        switch(mRadioGroup.getPosition()){
            case 0:
                interest = "Male";
                break;
            case 1:
                interest = "Female";
                break;
            case 2:
                interest = "Both";
                break;
        }

        Map userInfo = new HashMap();
        userInfo.put("interest", interest);
        userInfo.put("search_distance", Math.round(mSlider.getPosition() * 100));
        mUserDatabase.updateChildren(userInfo);

    }

    /**
     * Logs out user and takes it to the AuthenticationActivity
     */
    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingsActivity.this, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveUserInformation();
        finish();
        return false;
    }

}
