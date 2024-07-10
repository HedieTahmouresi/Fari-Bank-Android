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
    private static String selectedItem;

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
                onClick(currentUser, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem = " ";
            }
        });
    }

    public void onClick(SimpleUser currentUser, String selectedItem) {
        add = (Button) findViewById(R.id.addRequestButton);
        problem = (EditText) findViewById(R.id.requestProblem);
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!problem.getText().toString().isEmpty()) {
                            currentUser.addRequest(getSection(selectedItem), problem.getText().toString(), MainActivity.getFariBank(), AddRequest.this);
                            finish();
                        } else {
                            Toast.makeText(AddRequest.this, "You can't have an empty field", Toast.LENGTH_LONG).show();
                        }


                    }
                }
        );
    }

    public RequestSection getSection(String item) {
        switch (item) {
            case "AUTHENTICATIONS":
                return RequestSection.AUTHENTICATIONS;
            case "REPORT":
                return RequestSection.REPORT;
            case "FUNDS":
                return RequestSection.FUNDS;
            case "CONTACTS":
                return RequestSection.CONTACTS;
            case "TRANSFER":
                return RequestSection.TRANSFER;
            case "SIM_CHARGE":
                return RequestSection.SIM_CHARGE;
            case "CREDIT_CARD":
                return RequestSection.CREDIT_CARD;
            case "SETTINGS":
                return RequestSection.SETTINGS;
            default:
                Toast.makeText(AddRequest.this, "You have to choose from these options", Toast.LENGTH_LONG).show();
                return null;
        }

    }
}