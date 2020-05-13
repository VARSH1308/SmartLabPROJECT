package com.example.smartlab;

class User {
    public String email , gender , dob;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String gender, String dob) {
        this.email = email;
        this.gender = gender;
        this.dob = dob;
    }
}
