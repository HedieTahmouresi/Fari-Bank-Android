package ir.ac.kntu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactDetails extends AppCompatActivity {
    private TextView letter;
    private TextView fullName;
    private TextView number;
    private FloatingActionButton delete;
    private FloatingActionButton edit;
    private FloatingActionButton transfer;
    private FloatingActionButton chargeSim;

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
        initialize();
    }

    public void initialize() {
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("User Number"));
        Contact currentContact = currentUser.findContact(getIntent().getStringExtra("Contact Info"));
        fullName = (TextView) findViewById(R.id.contactName);
        letter = (TextView) findViewById(R.id.contactLetter);
        number = (TextView) findViewById(R.id.numberContact);
        fullName.setText(currentContact.getName().concat(" ").concat(currentContact.getLastName()));
        letter.setText(currentContact.getName().toUpperCase().substring(0, 1));
        number.setText(currentContact.getSimCard().getPhoneNumber());
        onClickDelete(currentUser, currentContact);
        onClickEdit(currentUser, currentContact);
        onClickTransfer(currentUser, currentContact);
    }

    public void onClickDelete(SimpleUser currentUser, Contact currentContact) {
        delete = (FloatingActionButton) findViewById(R.id.deleteContact);
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(ContactDetails.this);
                        warningBuilder.setMessage("Are you sure?").setCancelable(false).setPositiveButton("yes", (dialogInterface, i) -> {
                            currentUser.removeContact(currentContact);
                            finish();
                        }).setNegativeButton("no", (dialogInterface, i) -> dialogInterface.cancel());
                        AlertDialog warning = warningBuilder.create();
                        warning.setTitle("Warning");
                        warning.show();

                    }
                }
        );
    }

    public void onClickEdit(SimpleUser currentUser, Contact currentContact) {
        edit = (FloatingActionButton) findViewById(R.id.editContact);
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ContactDetails.this, EditContact.class);
                        intent.putExtra("Contact Info", currentContact.getSimCard().getPhoneNumber());
                        intent.putExtra("User Number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickTransfer(SimpleUser currentUser, Contact currentContact) {
        transfer = (FloatingActionButton) findViewById(R.id.transferToContact);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetails.this, Transfer.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                intent.putExtra("way", "by Contact List");
                intent.putExtra("Info", currentContact.getSimCard().getPhoneNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }
}