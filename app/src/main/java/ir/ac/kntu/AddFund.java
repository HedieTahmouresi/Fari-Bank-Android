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

public class AddFund extends AppCompatActivity {

    private EditText expDate;
    private EditText valueBonusFund;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_fund);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        expDate = (EditText) findViewById(R.id.expDate);
        valueBonusFund = (EditText) findViewById(R.id.valueBonusFund);
        add = (Button) findViewById(R.id.addBonus);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expDate.getText().toString().isEmpty() || valueBonusFund.getText().toString().isEmpty()) {
                    Toast.makeText(AddFund.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                } else {
                    currentUser.createBonusFund(AddFund.this, valueBonusFund.getText().toString(), expDate.getText().toString());
                    finish();
                }
            }
        });
    }
}