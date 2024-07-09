package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashBoard extends AppCompatActivity {
    private TextView fullName;
    private TextView accountID;
    private TextView balance;
    private ImageButton show;
    private ImageButton profile;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;


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
        recyclerView = (RecyclerView) findViewById(R.id.transactionsRecyclerView);
        seekBar = findViewById(R.id.seekBar3);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TransactionAdapter(currentUser.getAccount().getTransactions(), this);
        recyclerView.setAdapter(mAdapter);
        fullName.setText(currentUser.getName().concat(" ").concat(currentUser.getLastName()));
        accountID.setText(currentUser.getAccount().getAccountId());
        balance.setText("********");
        seekBar.setMax(currentUser.getAccount().getTransactions().size() - 1); // Set the maximum value of the SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    recyclerView.scrollToPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        showBalance(currentUser);
        onClickProfile(currentUser);
    }

    public void showBalance(SimpleUser currentUser) {
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

    public void onClickProfile(SimpleUser currentUser){
        profile = (ImageButton) findViewById(R.id.profile);
        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DashBoard.this, Profile.class);
                        intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                }
        );
    }
}