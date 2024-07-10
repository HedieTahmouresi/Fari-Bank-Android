package ir.ac.kntu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRequest extends AppCompatActivity {
    private Button add;
    private EditText problem;
    private Spinner sections;
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        sections = (Spinner) findViewById(R.id.spinnerSection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.section_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sections.setAdapter(adapter);
        sections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem = " ";
            }
        });
        onClick(currentUser, selectedItem);
    }

    public void onClick(SimpleUser currentUser, String selectedItem){
        add = (Button) findViewById(R.id.addRequestButton);
        problem = (EditText) findViewById(R.id.requestProblem);
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getSection(selectedItem)!=null && !problem.getText().toString().isEmpty()){
                            currentUser.addRequest(getSection(selectedItem), problem.getText().toString(), MainActivity.getFariBank(), AddRequest.this);
                            finish();
                        } else{
                            Toast.makeText(AddRequest.this, "You can't have an empty field", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public RequestSection getSection(String item) {
        switch (item) {
            case "1", "Authentications":
                return RequestSection.AUTHENTICATIONS;
            case "2", "Report":
                return RequestSection.REPORT;
            case "3", "Funds":
                return RequestSection.FUNDS;
            case "4", "Contacts":
                return RequestSection.CONTACTS;
            case "5", "Transfer":
                return RequestSection.TRANSFER;
            case "6", "Sim Charge":
                return RequestSection.SIM_CHARGE;
            case "7", "Credit Card":
                return RequestSection.CREDIT_CARD;
            case "8", "Settings":
                return RequestSection.SETTINGS;
            default:
                Toast.makeText(AddRequest.this, "You have to choose from these options", Toast.LENGTH_LONG).show();
                return null;
        }

    }
}