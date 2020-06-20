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

    String firstName, lastName, number, imgUriString;
    boolean hasImage;

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

        firstName = oldIntent.getStringExtra("FIRST_NAME");
        lastName = oldIntent.getStringExtra("LAST_NAME");
        String name =  firstName + " " + lastName ;
        number = oldIntent.getStringExtra("PHONE_NUMBER");

        hasImage = oldIntent.getBooleanExtra("HAS_IMAGE", false);
        if (hasImage) {
            imgUriString = oldIntent.getStringExtra("IMAGE");
            Uri img = Uri.parse(imgUriString);
            civ_view_pfp.setImageURI(img);
        }

        tv_view_name.setText(name);
        tv_view_number.setText(number);

        btn_deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.getItemID(number);
                int id = -1;
                if (cursor.moveToFirst()) {
                    do {
                        id = cursor.getInt(0);
                    } while (cursor.moveToNext());
                } else {
                    Log.i(TAG, "onClick: Apparently contact doesn't exist");
                }
                db.deleteContact(id, firstName, lastName, number);
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

                    Log.i(TAG, "onClick: " + firstName);
                    newIntent.putExtra("FIRST_NAME", firstName);

                    Log.i(TAG, "onClick: " + lastName);
                    newIntent.putExtra("LAST_NAME", lastName);

                    Log.i(TAG, "onClick: " + number);
                    newIntent.putExtra("PHONE_NUMBER", number);

                    newIntent.putExtra("HAS_IMAGE", hasImage);
                    if (hasImage) {

                        Log.i(TAG, "onClick: " + imgUriString);
                        newIntent.putExtra("IMAGE", imgUriString);

                    }
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
        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            firstName = data.getStringExtra("NEW_FIRST_NAME");
            lastName = data.getStringExtra("NEW_LAST_NAME");
            String name = firstName + " " + lastName;
            number = data.getStringExtra("NEW_PHONE_NUMBER");

            tv_view_name.setText(name);
            tv_view_number.setText(number);
            Toast.makeText(ViewContactActivity.this, "Updated!",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
