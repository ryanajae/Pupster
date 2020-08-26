package com.puppyTinder.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.puppyTinder.Login.AuthenticationActivity;
import com.puppyTinder.Objects.UserObject;
import com.puppyTinder.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Activity responsible for handling the edit of user's data
 */
public class EditProfileActivity extends AppCompatActivity {

    private EditText    mPupName,
                        mHumName,
                        mAge,
                        mAbout;

    private SegmentedButtonGroup mRadioGroupInterest, mRadioGroupEnergy;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    UserObject mUser = new UserObject();

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPupName = findViewById(R.id.edit_pupname);
        mHumName = findViewById(R.id.edit_humname);
        mAge = findViewById(R.id.age);
        mAbout = findViewById(R.id.about);

        mRadioGroupInterest = findViewById(R.id.interestReg);
        mRadioGroupEnergy = findViewById(R.id.edit_energy);

        mProfileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        getUserInfo();


        //on profile image click allow user to choose another pic by calling the responding intent
        mProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }


    /**
     * Get user's current info data and populate the corresponding Elements
     */
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser.parseObject(dataSnapshot);

                mPupName.setText(mUser.getPupName());
                mHumName.setText(mUser.getHumName());
                mAge.setText(mUser.getAge());
                mAbout.setText(mUser.getAbout());
                if(!mUser.getProfileImageUrl().equals("default"))
                    Glide.with(getApplicationContext()).load(mUser.getProfileImageUrl()).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
                if(mUser.getInterest().equals("Male"))
                    mRadioGroupInterest.setPosition(0, false);
                else if (mUser.getInterest().equals("Female"))
                    mRadioGroupInterest.setPosition(1, false);
                else mRadioGroupInterest.setPosition(2, false);
                if (mUser.getEnergy().equals("Low"))
                    mRadioGroupEnergy.setPosition(0, false);
                else if (mUser.getEnergy().equals("Medium"))
                    mRadioGroupEnergy.setPosition(1, false);
                else
                    mRadioGroupEnergy.setPosition(2, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Store user's info in the database
     * if the image has been changed then upload it to the storage
     */
    private void saveUserInformation() {

        String interest;

        String pupName = mPupName.getText().toString();
        String humName = mHumName.getText().toString();
        String age = mAge.getText().toString();
        String about = mAbout.getText().toString();
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
        Map userInfo = new HashMap();
        userInfo.put("pupName", pupName);
        userInfo.put("humName", humName);
        userInfo.put("age", age);
        userInfo.put("energy", energy);
        userInfo.put("interest", interest);
        userInfo.put("about", about);
        mUserDatabase.updateChildren(userInfo);

        if(resultUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

//            Uri uploadUri = Uri.fromFile(new File(resultUri.toString()));
//            String uploadString = uploadUri.toString();
//            uploadString = uploadString.substring(uploadString.indexOf("c"));
            UploadTask uploadTask = filePath.putFile(resultUri);

            uploadTask.addOnFailureListener(e -> {
                finish();
                return;
            });
            uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                Map newImage = new HashMap();
                newImage.put("profileImageUrl", uri.toString());
                mUserDatabase.updateChildren(newImage);

                finish();
            }).addOnFailureListener(exception -> {
                finish();
            }));
        }else{
            finish();
        }
    }

    /**
     * Get the uri of the image the user picked if the result is successful
     * @param requestCode - code of the request ( for the image request is 1)
     * @param resultCode - if the result was successful
     * @param data - data of the image fetched
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                Glide.with(getApplication())
                        .load(bitmap) // Uri of the picture
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        saveUserInformation();
        return false;
    }
}
