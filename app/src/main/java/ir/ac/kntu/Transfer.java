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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transfer extends AppCompatActivity {
    private LinearLayout layout;
    private EditText id;
    private EditText value;
    private EditText pass;
    private Spinner ways;
    private Button done;


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
        checkWay();
    }

    public void checkWay() {
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        String way = getIntent().getStringExtra("way");
        String info = getIntent().getStringExtra("Info");
        switch (way) {
            case "by CreditCard ID" -> transferByCreditCard(currentUser);
            case "by Account ID" -> System.out.println("hell");
            case "by Contact List" -> System.out.println("hel");
            case "by Recent List" -> System.out.println("he");
        }
    }

    public void transferByCreditCard(SimpleUser currentUser) {
        layout = (LinearLayout) findViewById(R.id.TransferByCreditCard);
        id = (EditText) findViewById(R.id.creditIDInput);
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
                } else if (checkCreditID(id.getText().toString())){
                    if (currentUser.getAccount().getCreditCard().hasSetPassword()){
                        if (checkPass(currentUser, pass.getText().toString())){
                            completeTransfer(ways, currentUser, id.getText().toString(), value.getText().toString());
                        }
                    } else{
                        completeTransfer(ways, currentUser, id.getText().toString(), value.getText().toString());
                    }

                }
            }
        });

    }

    public void completeTransfer(Spinner ways, SimpleUser currentUser, String id, String value){
        String selectedItem =(String) ways.getSelectedItem();
        if (MainActivity.getCentralBank().checkWays(MainActivity.getFariBank(), id, value, selectedItem)){
            String[] info = new String[2];
            info[0] = value;
            info[1] = selectedItem;
            SimpleUser receiver = MainActivity.getCurrentUserByCard(id);
            MainActivity.getCentralBank().completeTransferInfo(currentUser,receiver,info, Transfer.this);
        } else{
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

    public boolean checkPass(SimpleUser currentUser, String pass){
        if (pass.equals(Integer.toString(currentUser.getAccount().getCreditCard().getPassword()))){
            return true;
        }
        Toast.makeText(Transfer.this, "Wrong password", Toast.LENGTH_SHORT).show();
        return false;
    }
}