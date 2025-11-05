package com.example.sleepy_connect;

import android.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Entrant {
    /* Creates an entrant object:
    Input: android_id
    Output: Entrant object with: First Name, Last Name, Email, Birthday, Phone, Username initialized to NULL
    */
    public String android_id;
    public String birthday;
    public String email;
    public String phone_number;
    public String username;
//    public String password;
//    public String salt; // Base 64 encoded String
//    public String hash; // Base 64 encoded String
    public String last_name;
    public String first_name;
    public int access; // 1 is entrant, 2 is organizer, 3 is administrator

    public Entrant(String android_id) {
        this.android_id = android_id;
        this.first_name = "NULL";
        this.last_name = "NULL";
        this.email = "NULL";
        this.birthday = "NULL";
        this.phone_number = "NULL";
        this.username = "NULL";
        this.access = 1;

        /* Instead of storing a password, we generate a salt and a hash
        Reference: https://www.baeldung.com/java-password-hashing */

/*        // Salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.salt = Base64.encodeToString(salt, Base64.DEFAULT);

        // Hash
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            this.hash = Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }*/
    }

    public Entrant() {
        // Empty constructor for Firebase to use when retrieving stuff
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public Integer getAccess() { return access; }

    public void setAccess(Integer access) { this.access = access; }

}
