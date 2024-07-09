package ir.ac.kntu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactDetails extends AppCompatActivity {
    private TextView letter;
    private TextView fullName;
    private TextView number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("User Number"));
        Contact currentContact = currentUser.findContact(getIntent().getStringExtra("Contact Info"));
        fullName = (TextView) findViewById(R.id.contactName);
        letter = (TextView) findViewById(R.id.contactLetter);
        number = (TextView) findViewById(R.id.numberContact);
        fullName.setText(currentContact.getName().concat(" ").concat(currentContact.getLastName()));
        letter.setText(currentContact.getName().toUpperCase().substring(0,1));
        number.setText(currentContact.getSimCard().getPhoneNumber());
    }
}