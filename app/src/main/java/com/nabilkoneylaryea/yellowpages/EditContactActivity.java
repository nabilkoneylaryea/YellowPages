package com.nabilkoneylaryea.yellowpages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactActivity extends AppCompatActivity {

    private static final String TAG = "EDIT_CONTACT_ACTIVITY";
    EditText et_firstName, et_lastName, et_number;
    CircleImageView civ_pfp;
    Button btn_confirm, btn_cancel;
    DatabaseHelper db;

    int ID;
    String oldFirstName, oldLastName, oldPhoneNumber;
    String newFirstName, newLastName, newPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        et_firstName = findViewById(R.id.et_firstName_edit);
        et_lastName = findViewById(R.id.et_lastName_edit);
        et_number = findViewById(R.id.et_number_edit);
        civ_pfp = findViewById(R.id.civ_edit_pfp);
        btn_confirm = findViewById(R.id.btn_confirm_edit);
        btn_cancel = findViewById(R.id.btn_cancel_edit);

        db = DatabaseHelper.getInstance(this);

        Intent oldIntent = getIntent();
        ID = oldIntent.getIntExtra("ID", -1);
        Contact contact = db.getContact(ID);

        oldFirstName = contact.getFirstName();
        et_firstName.setHint(oldFirstName);

        oldLastName = contact.getLastName();
        et_lastName.setHint(oldLastName);

        oldPhoneNumber = contact.getPhoneNumber();
        et_number.setHint(oldPhoneNumber);

        if(contact.hasImage()) {
            Uri img = contact.getImg();
            civ_pfp.setImageURI(img);
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFirstName = et_firstName.getText().toString();
                newLastName = et_lastName.getText().toString();
                newPhoneNumber = et_number.getText().toString();
                updateContact();
                setResult(RESULT_OK);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateContact () {

        boolean firstNameDifferent = newFirstName.length() > 0 && !(newFirstName.equals(oldFirstName));
        boolean lastNameDifferent = newLastName.length() > 0 && !(newLastName.equals(oldLastName));
        boolean numberDifferent = newPhoneNumber.length() > 0 && !(newPhoneNumber.equals(oldPhoneNumber));

        if (firstNameDifferent) {
            db.updateFirstName(ID, oldFirstName, newFirstName);
            Log.i(TAG, "updateContact: First Name: " + newFirstName);
        }
        else {
            newFirstName = oldFirstName;
        }

        if (lastNameDifferent) {
            db.updateLastName(ID, oldLastName, newLastName);
            Log.i(TAG, "updateContact: Last Name: " + newLastName);
        }
        else {
            newLastName = oldLastName;
        }

        if (numberDifferent) {
           db.updatePhoneNumber(ID, oldPhoneNumber, newPhoneNumber);
            Log.i(TAG, "updateContact: Phone Number: " + newPhoneNumber);
        }
        else {
            newPhoneNumber = oldPhoneNumber;
        }

        et_firstName.setText(newFirstName);
        et_lastName.setText(newLastName);
        et_number.setText(newPhoneNumber);

    }
}
