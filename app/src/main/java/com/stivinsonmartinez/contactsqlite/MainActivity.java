package com.stivinsonmartinez.contactsqlite;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText name,phone,email;
    ImageView image;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListview;
    Uri imageUri= Uri.parse("android.resourse://com.stivinsonmartinez.contactsqlite/drawable/no_user_logo.png");
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.et_name);
        phone = (EditText) findViewById(R.id.et_phone);
        email = (EditText) findViewById(R.id.et_email);
        contactListview= (ListView) findViewById(R.id.listView);
        image = (ImageView) findViewById(R.id.iv_imagec);
        dbHandler = new DatabaseHandler(getApplicationContext());


        TabHost tabHost= (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.Contact_Creator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.Contact_List);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(dbHandler.getContactsCount(),
                        String.valueOf(name.getText()),
                        String.valueOf(phone.getText()),
                        String.valueOf(email.getText()),
                        imageUri);
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    Toast.makeText(getApplicationContext(), String.valueOf(name.getText()) +
                            " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(name.getText()) +
                        " already exists. Please use a different name.", Toast.LENGTH_SHORT).show();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(String.valueOf(name.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select contact Image"), 1);
            }
        });

        if(dbHandler.getContactsCount()!=0)
            Contacts.addAll(dbHandler.getAllContacts());

        populateList();
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode==RESULT_OK){
            imageUri=data.getData();
            image.setImageURI(data.getData());
        }
    }

    private void populateList() {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListview.setAdapter(adapter);
    }
    private boolean contactExists(Contact contact) {
        String name = contact.get_name();
        int contactCount = Contacts.size();

        for (int i = 0; i < contactCount; i++) {
            if (name.compareToIgnoreCase(Contacts.get(i).get_name()) == 0)
                return true;
        }
        return false;
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {

            super (MainActivity.this, R.layout.list_view, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.list_view, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView name_list = (TextView) view.findViewById(R.id.tc_name);
            name_list.setText(currentContact.get_name());
            TextView phone_list = (TextView) view.findViewById(R.id.tc_phone);
            phone_list.setText(currentContact.get_phone());
            TextView email_list = (TextView) view.findViewById(R.id.tc_email);
            email_list.setText(currentContact.get_email());
            ImageView image_list = (ImageView) view.findViewById(R.id.iv_image);
            image_list.setImageURI(currentContact.get_imageURI());

            return view;
        }
    }


}
