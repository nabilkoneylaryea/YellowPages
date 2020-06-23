package com.nabilkoneylaryea.yellowpages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewContactActivity extends AppCompatActivity {

    CircleImageView civ_view_pfp;
    TextView tv_view_name;
    TextView tv_view_number;
    Button btn_back, btn_deleteContact;

    String firstName, lastName, number;
    int ID;

    DatabaseHelper db;


    public static final String TAG = "View Contact Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_contact);

        civ_view_pfp = findViewById(R.id.civ_view_pfp);
        tv_view_name = findViewById(R.id.tv_name);
        tv_view_number = findViewById(R.id.tv_number);
        btn_back = findViewById(R.id.btn_back);
        btn_deleteContact = findViewById(R.id.btn_deleteContact);

        db = DatabaseHelper.getInstance(this);

        Intent oldIntent = getIntent();
        ID = oldIntent.getIntExtra("ID", -1);

        viewContact();

        btn_deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteContact(ID, firstName, lastName, number);
                Log.i(TAG, "onClick: Deleting...");
                Toast.makeText(ViewContactActivity.this, "Deleted: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btn_confirm_edit:

                try {

                    Intent newIntent = new Intent(ViewContactActivity.this, EditContactActivity.class);
                    newIntent.putExtra("ID", ID);
                    startActivityForResult(newIntent, 3);

                }
                catch(Exception e) {
                    Log.i(TAG, "onOptionsItemSelected: " + e.toString());
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3 && resultCode == RESULT_OK) {

            viewContact();
            Toast.makeText(ViewContactActivity.this, "Updated!",Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void viewContact() {

        Contact contact = db.getContact(ID);
        firstName = contact.getFirstName();
        lastName = contact.getLastName();
        String name = firstName + " " + lastName;
        tv_view_name.setText(name);

        number = contact.getPhoneNumber();
        tv_view_number.setText(number);

        if (contact.hasImage()) {
            Uri img = contact.getImg();
            civ_view_pfp.setImageURI(img);
        }

    }
}
