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
    Input: First Name, Last Name, Email, Birthday, Phone, Username, Password
    Output: Entrant object with: First Name, Last Name, Email, Birthday, Phone, Username, Salt, Hash
    */
    public String birthday;
    public String email;
    public String phone_number;
    public String username;
//    public String password;
    public String salt; // Base 64 encoded String
    public String hash; // Base 64 encoded String
    public String last_name;
    public String first_name;

    public Entrant(String first_name, String last_name, String email, String birthday, String phone, String username, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birthday = birthday;
        this.phone_number = phone;
        this.username = username;

        /* Instead of storing a password, we generate a salt and a hash
        Reference: https://www.baeldung.com/java-password-hashing */

        // Salt
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
        }
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

    public String getHash() { return hash; }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    public void setHash(String hash) { this.hash = hash; }


}
