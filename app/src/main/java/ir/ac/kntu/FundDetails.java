package ir.ac.kntu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FundDetails extends AppCompatActivity {
    private TextView id;
    private TextView balance;
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;
    private FloatingActionButton delete;
    private FloatingActionButton transfer;

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void setmAdapter(RecyclerView.Adapter mAdapter) {
        FundDetails.mAdapter = mAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fund_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public boolean transferAbility(Fund fund){
        if (fund instanceof BonusFund bonusFund){
            return false;
        }
        return true;
    }

    public void onClickDelete(SimpleUser currentUser, Fund currentFund){
        delete = (FloatingActionButton) findViewById(R.id.deleteFund);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder warningBuilder = new AlertDialog.Builder(FundDetails.this);
                warningBuilder.setMessage("Are you sure?").setCancelable(false).setPositiveButton("yes", (dialogInterface, i) -> {
                    currentFund.dissolveFund(MainActivity.getFariBank(), FundDetails.this);
                }).setNegativeButton("no", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog warning = warningBuilder.create();
                warning.setTitle("Warning");
                warning.show();
            }
        });
    }

    public String getKindString(Fund fund){
        if (fund instanceof SavingsFund savingsFund){
            return "Savings Fund";
        }else if (fund instanceof BonusFund bonusFund){
            return "Bonus Fund";
        }else if (fund instanceof RemainsFund remainsFund){
            return "Remains Fund";
        }
        return null;
    }

    public void onClickTransferTo(SimpleUser currentUser, Fund currentFund, RecyclerView.Adapter mAdapter){
        transfer = (FloatingActionButton) findViewById(R.id.transferToFund);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!transferAbility(currentFund)){
                    Toast.makeText(FundDetails.this, "you can't transfer money from or to this fund", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(FundDetails.this);
                builder.setTitle("How much would you like to transfer to your fund? ");
                final EditText input = new EditText(FundDetails.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        currentFund.transferToFund( getKindString(currentFund),userInput , FundDetails.this, balance);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    public void onClickTransferFrom(SimpleUser currentUser, Fund currentFund, RecyclerView.Adapter mAdapter){
        transfer = (FloatingActionButton) findViewById(R.id.transferFromFund);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!transferAbility(currentFund)){
                    Toast.makeText(FundDetails.this, "you can't transfer money from or to this fund", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(FundDetails.this);
                builder.setTitle("How much would you like to transfer from your fund? ");
                final EditText input = new EditText(FundDetails.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        currentFund.transferFromFund(getKindString(currentFund),userInput , FundDetails.this, balance);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        Fund currentFund = currentUser.findFund(getIntent().getStringExtra("fund ID"));
        recyclerView = (RecyclerView) findViewById(R.id.transactionRecyclerView);
        seekBar = findViewById(R.id.seekBarTransaction);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setmAdapter(new TransactionAdapter(currentFund.getTransactions(), this));
        recyclerView.setAdapter(getmAdapter());
        id = (TextView) findViewById(R.id.fundIDText);
        balance = (TextView) findViewById(R.id.fundBalance);
        id.setText(currentFund.getFundID());
        seekBar.setMax(currentFund.getTransactions().size() - 1); // Set the maximum value of the SeekBar
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
        balance.setText(Double.toString(currentFund.getBalance()));
        onClickDelete(currentUser, currentFund);
        onClickTransferTo(currentUser, currentFund, getmAdapter());
        onClickTransferFrom(currentUser, currentFund, getmAdapter());

    }
}