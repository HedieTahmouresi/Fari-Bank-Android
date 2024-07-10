package ir.ac.kntu;

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

public class EditContact extends AppCompatActivity {
    private EditText name;
    private EditText lastName;
    private EditText phoneNumber;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("User Number"));
        Contact currentContact = currentUser.findContact(getIntent().getStringExtra("Contact Info"));
        name = (EditText) findViewById(R.id.editName);
        lastName = (EditText) findViewById(R.id.editLastName);
        phoneNumber = (EditText) findViewById(R.id.editPhone);
        done = (Button) findViewById(R.id.done);
        name.setText(currentContact.getName());
        lastName.setText(currentContact.getLastName());
        phoneNumber.setText(currentContact.getSimCard().getPhoneNumber());
        onClick(currentUser, currentContact);
    }

    public boolean checkPhone(String phoneNumber, SimpleUser currentUser, Contact currentContact){
        int flag = MainActivity.getFariBank().getBankData().checkPhoneNumber(phoneNumber, "should exist");
        SimCard simCard = MainActivity.getFariBank().getManagerData().getSimCard(phoneNumber);
        if (simCard == null) {
            simCard = new SimCard(phoneNumber, false);
        }
        if (phoneNumber.isEmpty()){
            Toast.makeText(EditContact.this, "You can't leave the phone number field empty", Toast.LENGTH_LONG).show();
            return false;
        } else if (phoneNumber.equals(currentContact.getSimCard().getPhoneNumber())){
            return true;
        }else if (flag==1){
            Toast.makeText(EditContact.this, "Wrong format", Toast.LENGTH_LONG).show();
            return false;
        } else if (flag==3){
            Toast.makeText(EditContact.this, "This user doesn't have an account in my our bank!", Toast.LENGTH_LONG).show();
            return false;
        } else if (currentUser.contactExistence(new Contact(" ", " ", simCard))){
            Toast.makeText(EditContact.this, "You already have a contact with this number", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void onClick(SimpleUser currentUser, Contact currentContact){
        name = (EditText) findViewById(R.id.editName);
        lastName = (EditText) findViewById(R.id.editLastName);
        phoneNumber = (EditText) findViewById(R.id.editPhone);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkPhone(phoneNumber.getText().toString().trim(), currentUser, currentContact)){
                            currentContact.setName(name.getText().toString());
                            currentContact.setLastName(lastName.getText().toString());
                            SimCard simCard = MainActivity.getFariBank().getManagerData().getSimCard(phoneNumber.getText().toString().trim());
                            if (simCard == null) {
                                simCard = new SimCard(phoneNumber.getText().toString().trim(), false);
                            }
                            currentContact.setSimCard(simCard);
                            finish();
                        }
                    }
                }
        );
    }

}