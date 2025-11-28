package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sleepy_connect.admin.AdminActivity;

//file needs to be cleaned up
//we should move edit and read-only changes into methods since they have multiple uses

/**
 * fragment to display and change the user profile
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String correct_password = "sleepyadmin";
    private String saved_user;
    private String saved_first;
    private String saved_last;
    private String saved_birthday;
    private String saved_phone;
    private String saved_email;
    private KeyListener user_key;
    private KeyListener first_key;
    private KeyListener last_key;
    private KeyListener birthday_key;
    private KeyListener phone_key;
    private KeyListener email_key;
    private KeyListener password_key;
    private Entrant user;
    private EntrantDAL entrantDal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // get user from viewmodel
        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = vmUser.getUser().getValue();
        assert user != null;

        //set strings as empty first so they still have a value to display even if the user information cannot be pulled
        //cleans up code by avoiding 6 if-else statements
        saved_user = "";
        saved_first = "";
        saved_last = "";
        saved_birthday = "";
        saved_phone = "";
        saved_email = "";
        //fills the variables with user information from database
        if (user.getUsername() != null) saved_user = user.getUsername();
        if (user.getFirst_name() != null) saved_first = user.getFirst_name();
        if (user.getLast_name() != null) saved_last = user.getLast_name();
        if (user.getBirthday() != null) saved_birthday = user.getBirthday();
        if (user.getEmail() != null) saved_email = user.getEmail();
        if (user.getPhone_number() != null) saved_phone = user.getPhone_number();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find id for each piece of information
        EditText profile_user = view.findViewById(R.id.profile_username);
        EditText profile_first = view.findViewById(R.id.profile_first_name);
        EditText profile_last = view.findViewById(R.id.profile_last_name);
        EditText profile_birthday = view.findViewById(R.id.profile_birthday);
        EditText profile_phone = view.findViewById(R.id.profile_phone);
        EditText profile_email = view.findViewById(R.id.profile_email);
        EditText admin_password = view.findViewById(R.id.admin_password_input);

        //ids for the seven buttons
        Button edit_button = view.findViewById(R.id.edit_profile_button);
        Button delete_button = view.findViewById(R.id.delete_profile_button);
        Button confirm_button = view.findViewById(R.id.confirm_profile_button);
        Button cancel_button = view.findViewById(R.id.cancel_profile_button);
        Button notif_setting_button = view.findViewById(R.id.notification_settings_button);
        Button admin_view_button = view.findViewById(R.id.admin_view_button);


        //sets the textviews with the current information for the user
        profile_user.setText(saved_user);
        profile_first.setText(saved_first);
        profile_last.setText(saved_last);
        profile_birthday.setText(saved_birthday);
        profile_email.setText(saved_email);
        profile_phone.setText(saved_phone);

        //make the edittext read-only while in the profile preview
        disableText(profile_user);
        disableText(profile_first);
        disableText(profile_last);
        disableText(profile_birthday);
        disableText(profile_phone);
        disableText(profile_email);

        //get keyboard input from user
        user_key = profile_user.getKeyListener();
        first_key = profile_user.getKeyListener();
        last_key = profile_user.getKeyListener();
        birthday_key = profile_user.getKeyListener();
        phone_key = profile_user.getKeyListener();
        email_key = profile_user.getKeyListener();

        //sets key listener for each input to null because it is read-only
        profile_user.setKeyListener(null);
        profile_first.setKeyListener(null);
        profile_last.setKeyListener(null);
        profile_birthday.setKeyListener(null);
        profile_phone.setKeyListener(null);
        profile_email.setKeyListener(null);

        //keyboard listener for admin view password stays on when not editing profile
        password_key = admin_password.getKeyListener();
        admin_password.setKeyListener(password_key);

        admin_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_input = admin_password.getText().toString().trim();
                //correct password inputted
                if (user_input.equals(correct_password)) {
                    Intent intent = new Intent(requireActivity(), AdminActivity.class);
                    NavigationActivity host = (NavigationActivity) requireActivity();
                    intent.putExtra("uid", host.userID);
                    startActivity(intent);
                } else {
                    // wrong password inputted
                    Toast.makeText(requireActivity(), "Incorrect password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            /**
             * switches screen aspects to make them editable
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                //turns all user information to usable strings
                saved_user = profile_user.getText().toString();
                saved_first = profile_first.getText().toString();
                saved_last = profile_last.getText().toString();
                saved_birthday = profile_birthday.getText().toString();
                saved_phone = profile_phone.getText().toString();
                saved_email = profile_email.getText().toString();

                //turns the edittext to editable
                enableText(profile_user);
                enableText(profile_first);
                enableText(profile_last);
                enableText(profile_birthday);
                enableText(profile_phone);
                enableText(profile_email);

                //disable switching to admin view
                disableText(admin_password);
                admin_view_button.setVisibility(view.INVISIBLE);
                admin_password.setKeyListener(null);

                //sets key listener to specific input for future exception handling
                profile_user.setKeyListener(user_key);
                profile_first.setKeyListener(first_key);
                profile_last.setKeyListener(last_key);
                profile_birthday.setKeyListener(birthday_key);
                profile_phone.setKeyListener(phone_key);
                profile_email.setKeyListener(email_key);

                //change the button options to reflect the edit profile state
                edit_button.setVisibility(view.GONE);
                delete_button.setVisibility(view.GONE);
                confirm_button.setVisibility(view.VISIBLE);
                cancel_button.setVisibility(view.VISIBLE);
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            /**
             * switches screen aspects to make them uneditable
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                //turns the profile back to read only
                disableText(profile_user);
                disableText(profile_first);
                disableText(profile_last);
                disableText(profile_birthday);
                disableText(profile_phone);
                disableText(profile_email);

                profile_user.setKeyListener(null);
                profile_first.setKeyListener(null);
                profile_last.setKeyListener(null);
                profile_birthday.setKeyListener(null);
                profile_phone.setKeyListener(null);
                profile_email.setKeyListener(null);

                //admin profile can now be switched to again
                enableText(admin_password);
                admin_password.setKeyListener(password_key);
                admin_view_button.setVisibility(view.VISIBLE);

                //set the confirmed information to the screen
                saved_user = profile_user.getText().toString();
                saved_first = profile_first.getText().toString();
                saved_last = profile_last.getText().toString();
                saved_birthday = profile_birthday.getText().toString();
                saved_phone = profile_phone.getText().toString();
                saved_email = profile_email.getText().toString();

                //put the new information into the database
                user.setUsername(saved_user);
                user.setFirst_name(saved_first);
                user.setLast_name(saved_last);
                user.setBirthday(saved_birthday);
                user.setPhone_number(saved_phone);
                user.setEmail(saved_email);

                entrantDal = new EntrantDAL();
                entrantDal.updateEntrant(user);

                //change the buttons back to the read only state
                edit_button.setVisibility(view.VISIBLE);
                delete_button.setVisibility(view.VISIBLE);
                confirm_button.setVisibility(view.GONE);
                cancel_button.setVisibility(view.GONE);
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            /**
             * switches screen aspects to make them uneditable
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //changes profile back to read only
                profile_user.setText(saved_user);
                profile_first.setText(saved_first);
                profile_last.setText(saved_last);
                profile_birthday.setText(saved_birthday);
                profile_phone.setText(saved_phone);
                profile_email.setText(saved_email);

                disableText(profile_user);
                disableText(profile_first);
                disableText(profile_last);
                disableText(profile_birthday);
                disableText(profile_phone);
                disableText(profile_email);

                profile_user.setKeyListener(null);
                profile_first.setKeyListener(null);
                profile_last.setKeyListener(null);
                profile_birthday.setKeyListener(null);
                profile_phone.setKeyListener(null);
                profile_email.setKeyListener(null);

                //admin profile can now be switched to again
                enableText(admin_password);
                admin_password.setKeyListener(password_key);
                admin_view_button.setVisibility(view.VISIBLE);

                edit_button.setVisibility(view.VISIBLE);
                delete_button.setVisibility(view.VISIBLE);
                confirm_button.setVisibility(view.GONE);
                cancel_button.setVisibility(view.GONE);
            }
        });

        // set click listener for delete profile button
        delete_button.setOnClickListener(v -> {

            // update database
            EntrantDAL entrantDAL = new EntrantDAL();
            entrantDAL.deleteEntrant(user.getAndroid_id(), user.created_event_list)
                    .addOnCompleteListener(task -> requireActivity().finish());
        });
    }

    /**
     * makes editTexts read-only
     * @param t editText to be changed
     */
    private void disableText(EditText t){
        t.setFocusable(false);
        t.setFocusableInTouchMode(false);
        t.setCursorVisible(false);
        t.setClickable(false);
    }

    /**
     * makes editTexts editable
     * @param t editText to be changed
     */
    private void enableText(EditText t){
        t.setFocusable(true);
        t.setFocusableInTouchMode(true);
        t.setCursorVisible(true);
        t.setClickable(true);
    }
}