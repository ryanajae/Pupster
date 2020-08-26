package com.puppyTinder.Objects;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.Months;
import org.joda.time.Years;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * Object of each card
 */
public class UserObject implements Serializable {
    private String  userId = "--",
                    pupName = "--",
                    humName = "--",
                    profileImageUrl = "default",
                    age = "--",
                    birthdate = "--",
                    about = "--",
                    userSex = "--",
                    interest = "--",
                    energy = "--";

    float searchDistance = 100;

    public UserObject(){}


    public void parseObject(DataSnapshot dataSnapshot){
        if(!dataSnapshot.exists()){return;}
        userId = dataSnapshot.getKey();

        if(dataSnapshot.child("pupName").getValue()!=null)
            pupName = dataSnapshot.child("pupName").getValue().toString();
        if(dataSnapshot.child("humName").getValue()!=null)
            humName = dataSnapshot.child("humName").getValue().toString();
        if(dataSnapshot.child("sex").getValue()!=null)
            userSex = dataSnapshot.child("sex").getValue().toString();
        if(dataSnapshot.child("birthdate").getValue()!=null)
            birthdate = dataSnapshot.child("birthdate").getValue().toString();
        if(dataSnapshot.child("about").getValue()!=null)
            about = dataSnapshot.child("about").getValue().toString();
        if (dataSnapshot.child("profileImageUrl").getValue()!=null)
            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
        if (dataSnapshot.child("energy").getValue()!=null)
            energy = dataSnapshot.child("energy").getValue().toString();
        if(dataSnapshot.child("interest").getValue()!=null)
            interest = dataSnapshot.child("interest").getValue().toString();
        if(dataSnapshot.child("search_distance").getValue()!=null)
            searchDistance = Float.parseFloat(dataSnapshot.child("search_distance").getValue().toString());

    }

    public String getUserId(){
        return userId;
    }
    public String getPupName(){
        return pupName;
    }
    public String getHumName() { return humName; }
    public String getAge(){
        String[] split = birthdate.split("/");
        int year = Integer.parseInt(split[2]);
        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);

//        org.joda.time.LocalDate birthDate,
//        org.joda.time.LocalDate currentDate) {
//            // validate inputs ...
//            Years age = Years.yearsBetween(birthDate, currentDate);
//            return age.getYears();
//            LocalDate bday = LocalDate.of(year, month, day);
//        Period p1 = Period.between(bday, LocalDate.now());
        org.joda.time.LocalDate birthdate = new org.joda.time.LocalDate(year, month, day);
        org.joda.time.LocalDate now = new org.joda.time.LocalDate();
        int numYears = Years.yearsBetween(birthdate, now).getYears();
        if (numYears < 2) {
            if (numYears == 1)
                age = Integer.toString(numYears) + " year, " + Integer.toString(Months.monthsBetween(birthdate, now).getMonths() - (12 * numYears)) + " months old";
            else
                age = Integer.toString(Months.monthsBetween(birthdate, now).getMonths() - (12 * numYears)) + " months old";
        }
        else
           age = Integer.toString(numYears) + " years old";
        return age;
    }
    public String getAbout(){
        return about;
    }
    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public String getEnergy() { return energy; }
    public float getSearchDistance() {
        return searchDistance;
    }

    public String getInterest() {
        return interest;
    }

    public String getUserSex() {
        return userSex;
    }
}
