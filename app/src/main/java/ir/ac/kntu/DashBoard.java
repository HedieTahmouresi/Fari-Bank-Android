package ir.ac.kntu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;

public class DashBoard extends AppCompatActivity {
    private TextView fullName;
    private TextView accountID;
    private TextView balance;
    private ImageButton show;
    private ImageButton profile;
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;
    private FloatingActionButton chargeAccount;
    private FloatingActionButton transfer;
    private ImageButton funds;
    private ImageButton loans;
    private static DashboardHandler handler;
    private FloatingActionButton report;

    public static DashboardHandler getHandler() {
        return handler;
    }

    public static void setHandler(DashboardHandler handler) {
        DashBoard.handler = handler;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void setmAdapter(RecyclerView.Adapter mAdapter) {
        DashBoard.mAdapter = mAdapter;
    }

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
        setHandler(new DashboardHandler(this));
        initialize();
    }

    public void initialize() {
        String id = getIntent().getStringExtra("Account Id Out");
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
        onClickCharge(currentUser, balance, recyclerView, seekBar);
        onClickTransfer(currentUser, id);
        onClickFunds(currentUser);
        onClickLoans(currentUser);
        onClickReport(currentUser);
    }

    public void onClickFunds(SimpleUser currentUser){
        funds = (ImageButton) findViewById(R.id.funds);
        funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, FundsPage.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                startActivity(intent);
            }
        });
    }

    public void onClickReport(SimpleUser currentUser){
        report = (FloatingActionButton) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, Reports.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                startActivity(intent);
            }
        });
    }

    public void onClickLoans(SimpleUser currentUser){
        loans = (ImageButton) findViewById(R.id.loans);
        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoanServices(currentUser);
            }
        });
    }

    public void showLoanServices(SimpleUser currentUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loan Services :");
        Spinner services = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.loan_services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services.setAdapter(adapter);
        builder.setView(services);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = (String) services.getSelectedItem();
                switch(selected){
                    case "Loan Request"-> {
                        Intent intent = new Intent(DashBoard.this, LoanRequestPage.class);
                        intent.putExtra("Phone number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                    case "Loan Management and Payment"-> {
                        Intent intent = new Intent(DashBoard.this, LoansPage.class);
                        intent.putExtra("Phone number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                    default -> {
                        return;
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.show();
    }

    public void onClickCharge(SimpleUser currentUser, TextView balance, RecyclerView recyclerView, SeekBar seekBar) {
        chargeAccount = (FloatingActionButton) findViewById(R.id.charge);
        chargeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showValueDialog(currentUser, balance, recyclerView, seekBar);
            }
        });
    }

    public void updateTransactions(SimpleUser currentUser, RecyclerView view, SeekBar seekBar) {
        mAdapter = new TransactionAdapter(currentUser.getAccount().getTransactions(), this);
        view.setAdapter(mAdapter);
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
    }

    private void showValueDialog(SimpleUser currentUser, TextView balance, RecyclerView view, SeekBar seekBar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transfer");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                currentUser.getAccount().chargeAccount(MainActivity.getFariBank(), userInput, DashBoard.this);
                balance.setText(Double.toString(currentUser.getAccount().getBalance()));
                updateTransactions(currentUser, view, seekBar);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("How much would you like to charge your account? ");
        builder.show();
    }

    public void onClickTransfer(SimpleUser currentUser, String id) {
        transfer = (FloatingActionButton) findViewById(R.id.transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransferWay(currentUser, id);
            }
        });
    }

    public void showTransferWay(SimpleUser currentUser, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to transfer the money? ");
        Spinner ways = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transfer_ways, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(adapter);
        builder.setView(ways);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = (String) ways.getSelectedItem();
                Intent intent = new Intent(DashBoard.this, Transfer.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                intent.putExtra("way", selected);
                intent.putExtra("Info", " ");
                intent.putExtra("Account Id Out", id);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();
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

    public void onClickProfile(SimpleUser currentUser) {
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

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    public static class DashboardHandler extends Handler {
        private final WeakReference<DashBoard> activityReference;

        public DashboardHandler(DashBoard activity) {
            super(Looper.getMainLooper());
            activityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            DashBoard activity = activityReference.get();
            if (activity != null) {
                activity.initialize();
            }
        }
    }
}