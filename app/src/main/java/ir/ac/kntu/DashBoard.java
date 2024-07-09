package ir.ac.kntu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashBoard extends AppCompatActivity {
    private TextView fullName;
    private TextView accountID;
    private TextView balance;
    private ImageButton show;
    private ImageButton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        fullName = (TextView) findViewById(R.id.fullName);
        accountID = (TextView) findViewById(R.id.accountID);
        balance = (TextView) findViewById(R.id.Balance);
        fullName.setText(currentUser.getName().concat(" ").concat(currentUser.getLastName()));
        accountID.setText(currentUser.getAccount().getAccountId());
        balance.setText("********");
        showBalance(currentUser);
    }

    public void showBalance(SimpleUser currentUser){
        show = (ImageButton) findViewById(R.id.showBalance);
        show.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        balance.setText(Double.toString(currentUser.getAccount().getBalance()));
                    }
                }
        );
    }
}