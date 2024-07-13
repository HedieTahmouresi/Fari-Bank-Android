package ir.ac.kntu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transfer extends AppCompatActivity {
    private LinearLayout layout;
    private EditText id;
    private EditText value;
    private EditText pass;
    private Spinner ways;
    private Button done;
    private Spinner options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transfer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String accId = getIntent().getStringExtra("Account Id Out");
        checkWay(accId);
    }

    public void checkWay(String id) {
        CentralBank centralBank = MainActivity.getCentralBank();
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        String way = getIntent().getStringExtra("way");
        String info = getIntent().getStringExtra("Info");

        switch (way) {
            case "by CreditCard ID" -> transferByCreditCard(currentUser, centralBank, id);
            case "by Account ID" -> transferByAccount(currentUser, centralBank, id);
            case "by Contact List" -> transferByContact(currentUser, info);
            case "by Recent List" -> transferByRecent(currentUser);
            default -> {
                return;
            }
        }
    }

    public void transferByContact(SimpleUser currentUser, String info) {
        layout = (LinearLayout) findViewById(R.id.TransferByContact);
        value = (EditText) findViewById(R.id.valueInputContact);
        ways = (Spinner) findViewById(R.id.spinnerWaysContact);
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this, R.array.transfer_options, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(mAdapter);
        done = (Button) findViewById(R.id.contactTransferButton);
        layout.setVisibility(View.VISIBLE);
        options = (Spinner) findViewById(R.id.spinnerContacts);
        ContactSpinnerAdapter adapter = new ContactSpinnerAdapter(Transfer.this, currentUser.getContacts());
        options.setAdapter(adapter);
        if (!" ".equals(info)){
            Contact contact = currentUser.findContact(info);
            int index = Arrays.asList(currentUser.getContacts()).indexOf(contact);
            options.setSelection(index);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = (Contact) options.getSelectedItem();
                String way = (String) ways.getSelectedItem();
                if (value.getText().toString().isEmpty()) {
                    Toast.makeText(Transfer.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                } else if (!"Fari Transfer".equals(way)) {
                    Toast.makeText(Transfer.this, "You can't choose " + way, Toast.LENGTH_SHORT).show();
                } else {
                    SimpleUser receiver = MainActivity.getCurrentUser(contact.getSimCard().getPhoneNumber());
                    String[] info = new String[2];
                    info[0] = value.getText().toString();
                    info[1] = "Fari Transfer";
                    MainActivity.getCentralBank().completeTransferInfoContact(currentUser, receiver, info, Transfer.this);
                }
            }
        });

    }

    public void transferByRecent(SimpleUser currentUser) {
        layout = (LinearLayout) findViewById(R.id.TransferByRecent);
        value = (EditText) findViewById(R.id.valueInputRecent);
        ways = (Spinner) findViewById(R.id.spinnerWaysRecent);
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this, R.array.transfer_options, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(mAdapter);
        done = (Button) findViewById(R.id.recentTransferButton);
        layout.setVisibility(View.VISIBLE);
        options = (Spinner) findViewById(R.id.spinnerRecents);
        RecentSpinnerAdapter adapter = new RecentSpinnerAdapter(Transfer.this, currentUser.getAccount().getRecentList());
        options.setAdapter(adapter);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recent recent = (Recent) options.getSelectedItem();
                String way = (String) ways.getSelectedItem();
                if (value.getText().toString().isEmpty()) {
                    Toast.makeText(Transfer.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                } else {
                    completeTransferRecent(ways, currentUser, recent, value.getText().toString());
                }
            }
        });

    }

    public void transferByCreditCard(SimpleUser currentUser, CentralBank centralBank, String accId) {
        layout = (LinearLayout) findViewById(R.id.TransferByCreditCard);
        id = (EditText) findViewById(R.id.creditIDInput);
        id.setText(accId);
        value = (EditText) findViewById(R.id.valueInputCredit);
        pass = (EditText) findViewById(R.id.creditPassInput);
        ways = (Spinner) findViewById(R.id.spinnerWaysCredit);
        done = (Button) findViewById(R.id.creditTransferButton);
        layout.setVisibility(View.VISIBLE);
        if (currentUser.getAccount().getCreditCard().hasSetPassword()) {
            pass.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transfer_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(adapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().isEmpty() || value.getText().toString().isEmpty() || (currentUser.getAccount().getCreditCard().hasSetPassword() && pass.getText().toString().isEmpty())) {
                    Toast.makeText(Transfer.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                } else if (checkCreditID(id.getText().toString())) {
                    if (currentUser.getAccount().getCreditCard().hasSetPassword()) {
                        if (checkPass(currentUser, pass.getText().toString())) {
                            completeTransferCard(ways, currentUser, id.getText().toString(), value.getText().toString());
                        }
                    } else {
                        completeTransferCard(ways, currentUser, id.getText().toString(), value.getText().toString());
                    }

                }
            }
        });

    }

    public void completeTransferRecent(Spinner ways, SimpleUser currentUser, Recent recent, String value) {
        String selectedItem = (String) ways.getSelectedItem();
        SimpleUser receiver = recent.getPerson();
        if (MainActivity.getCentralBank().checkWays(receiver.getAccount().getCreditCard().getCreditCardId(), value, selectedItem, true)) {
            String[] info = new String[2];
            info[0] = value;
            info[1] = selectedItem;
            MainActivity.getCentralBank().completeTransferInfoRecent(currentUser, receiver, info, Transfer.this);

        } else {
            Toast.makeText(Transfer.this, "You can't choose " + selectedItem, Toast.LENGTH_SHORT).show();
        }
    }

    public void completeTransferCard(Spinner ways, SimpleUser currentUser, String id, String value) {
        String selectedItem = (String) ways.getSelectedItem();
        if (MainActivity.getCentralBank().checkWays(id, value, selectedItem, true)) {
            String[] info = new String[2];
            info[0] = value;
            info[1] = selectedItem;
            SimpleUser receiver = MainActivity.getCentralBank().existsCreditCardId(id).getOwner();
            if (receiver != null) {
                MainActivity.getCentralBank().completeTransferInfoCredit(currentUser, receiver, info, Transfer.this);
            } else {
                Toast.makeText(Transfer.this, "There is no user with this credit card id", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Transfer.this, "You can't choose " + selectedItem, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkCreditID(String creditID) {
        String regexID = "[0-9]{16}";
        Pattern patternID = Pattern.compile(regexID);
        Matcher matcherID = patternID.matcher(creditID);
        if (!matcherID.matches()) {
            Toast.makeText(Transfer.this, "Wrong format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkPass(SimpleUser currentUser, String pass) {
        if (pass.equals(Integer.toString(currentUser.getAccount().getCreditCard().getPassword()))) {
            return true;
        }
        Toast.makeText(Transfer.this, "Wrong password", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void transferByAccount(SimpleUser currentUser, CentralBank centralBank, String accId) {
        layout = (LinearLayout) findViewById(R.id.TransferByAccountID);
        id = (EditText) findViewById(R.id.accountIDInput);
        id.setText(accId);
        value = (EditText) findViewById(R.id.valueInputAccount);
        ways = (Spinner) findViewById(R.id.spinnerWaysAccount);
        done = (Button) findViewById(R.id.accountTransferButton);
        layout.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transfer_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(adapter);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().isEmpty() || value.getText().toString().isEmpty()) {
                    Toast.makeText(Transfer.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                } else if (checkAccountID(id.getText().toString())) {
                    completeTransferAccount(ways, currentUser, id.getText().toString(), value.getText().toString());
                }
            }
        });

    }


    public void completeTransferAccount(Spinner ways, SimpleUser currentUser, String id, String value) {
        String selectedItem = (String) ways.getSelectedItem();
        SimpleUser receiver = MainActivity.getCentralBank().existsAccountId(id).getOwner();
        if (receiver == null) {
            Toast.makeText(Transfer.this, "There is no user with this credit card id", Toast.LENGTH_SHORT).show();
        } else if (MainActivity.getCentralBank().checkWays(receiver.getAccount().getCreditCard().getCreditCardId(), value, selectedItem, false)) {
            String[] info = new String[2];
            info[0] = value;
            info[1] = selectedItem;
            MainActivity.getCentralBank().completeTransferInfoAccount(currentUser, receiver, info, Transfer.this);

        } else {
            Toast.makeText(Transfer.this, "You can't choose " + selectedItem, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkAccountID(String creditID) {
        String regexID = "[0-9]{13}";
        Pattern patternID = Pattern.compile(regexID);
        Matcher matcherID = patternID.matcher(creditID);
        if (!matcherID.matches()) {
            Toast.makeText(Transfer.this, "Wrong format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}