package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddContact extends AppCompatActivity {
    private Button button;
    private EditText name;
    private EditText lastName;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        onClick(currentUser);
    }

    public Contact addContact(String name, String lastName, String phoneNumber, SimpleUser user){
        if (MainActivity.getFariBank().getBankData().checkPhoneNumber(phoneNumber, "should exist")==0 && user.findContact(phoneNumber)==null){
            SimCard simCard = MainActivity.getFariBank().getManagerData().getSimCard(phoneNumber);
            if (simCard == null) {
                simCard = new SimCard(phoneNumber, false);
            }
            Contact newContact = new Contact(name, lastName, simCard);
            user.addContact(newContact);
            return newContact;
        } else if (MainActivity.getFariBank().getBankData().checkPhoneNumber(phoneNumber, "should exist")==1){
            Toast.makeText(AddContact.this, "Wrong format", Toast.LENGTH_LONG).show();
            return null;
        } else if (MainActivity.getFariBank().getBankData().checkPhoneNumber(phoneNumber, "should exist")==3){
            Toast.makeText(AddContact.this, "This user doesn't have an account in my our bank!", Toast.LENGTH_LONG).show();
            return null;
        } else if (user.findContact(phoneNumber)!=null){
            Toast.makeText(AddContact.this, "You already have a contact with this number", Toast.LENGTH_LONG).show();
            return null;
        }
        return null;
    }

    public void onClick(SimpleUser currentUser){
        button = (Button) findViewById(R.id.addButton);
        name = (EditText) findViewById(R.id.editTextText);
        lastName = (EditText) findViewById(R.id.editTextText2);
        phoneNumber = (EditText) findViewById(R.id.editTextPhone2);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Contact newContact = addContact(name.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString(), currentUser);
                        if (newContact!=null){
                            finish();
                        }
                    }
                }
        );
    }
}