package com.example.sleepy_connect;

import android.util.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import static org.junit.Assert.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class EntrantTest {
    // Had to move this test to an instrumented test, because the encoder relies on android utility.
    // The java utility could work too, but needs an SDK that is higher than our app supports.

    /*@Test
    public void testSaltHashNonNull() {
        // Checking generation of salt and hash
        String password = "password";

        Entrant e1 = new Entrant("Test1", "Test1", "test1@gmail.com", "2000-01-01", "123", "test1", password);
        Entrant e2 = new Entrant("test2", "test2", "test2@gmail.com", "2000-01-01", "123", "test2", password);

        // Checking if salts and hashes are not null
        assertNotNull(e1.getSalt()); assertNotNull(e1.getHash());
        assertNotNull(e2.getSalt()); assertNotNull(e2.getHash());
    }

    @Test
    public void testSaltHashDifferent(){
        // Checking regeneration of salt and hash
        String password = "password";

        Entrant e1 = new Entrant("Test1", "Test1", "test1@gmail.com", "2000-01-01", "123", "test1", password);
        Entrant e2 = new Entrant("test2", "test2", "test2@gmail.com", "2000-01-01", "123", "test2", password);

        // Checking if the salts are regenerated for every entrant
        assertNotEquals(e1.getSalt(), e2.getSalt());

        // Checking if the hashing function works, should be different hashes
        assertNotEquals(e1.getHash(), e2.getHash());
    }

    @Test
    public void testSaltHashRetrievable(){
        // Checking if we can verify the hash after everything was generated
        String password = "password";

        Entrant e1 = new Entrant("Test1", "Test1", "test1@gmail.com", "2000-01-01", "123", "test1", password);
        Entrant e2 = new Entrant("test2", "test2", "test2@gmail.com", "2000-01-01", "123", "test2", password);

        // Checking if we can verify the hash
        byte[] saltBytes = Base64.decode(e1.getSalt(), Base64.DEFAULT);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashBytes = factory.generateSecret(spec).getEncoded();
            Base64.encodeToString(hashBytes, Base64.DEFAULT);
            assertEquals(e1.getHash(), Base64.encodeToString(hashBytes, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }*/
}

