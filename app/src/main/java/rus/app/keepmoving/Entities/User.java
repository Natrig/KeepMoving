package rus.app.keepmoving.Entities;


import android.view.View;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class User {
    FirebaseUser authUser;
    String name;
    String surname;
    String password;
    String phone;
    String email;
    String description;
    String photoPath;
    String confirmation;
    Date birth;

    public User(View view) {

//        EditText email = findViewById(R.id.emailInput);
//        EditText password = findViewById(R.id.passwordInput);
    }

    public boolean save() {
        boolean save = false;


        return save;
    }

    public FirebaseUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(FirebaseUser authUser) {
        this.authUser = authUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
