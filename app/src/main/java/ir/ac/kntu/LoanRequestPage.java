package ir.ac.kntu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
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

import java.lang.ref.WeakReference;

public class LoanRequestPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;
    private FloatingActionButton button;
    private static loanRequestHandler handler;

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(loanRequestHandler handler) {
        LoanRequestPage.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan_request_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setHandler(new loanRequestHandler(this));
        initialize();
    }

    public void initialize() {
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone number"));
        recyclerView = (RecyclerView) findViewById(R.id.loanRequestRecyclerReview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoanRequestAdapter(currentUser.getLoanRequests(), this, currentUser.getSimCard().getPhoneNumber());
        recyclerView.setAdapter(mAdapter);
        seekBar = (SeekBar) findViewById(R.id.loanRequestSeekBar);
        seekBar.setMax(currentUser.getLoanRequests().size() - 1);
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
        onClickAdd(currentUser);
    }

    private void onClickAdd(SimpleUser currentUser) {
        button = (FloatingActionButton) findViewById(R.id.addLoanRequest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(currentUser);
            }
        });
    }

    public void showAlert(SimpleUser currentUser) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_loan_request, null);
        EditText amount = dialogView.findViewById(R.id.editText1);
        EditText numOfMonths = dialogView.findViewById(R.id.editText2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Loan Request");
        builder.setView(dialogView)
                .setTitle("Custom Dialog")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (amount.getText().toString().isEmpty() || numOfMonths.getText().toString().isEmpty()) {
                            Toast.makeText(LoanRequestPage.this, "You can't have an empty field", Toast.LENGTH_SHORT).show();
                        } else {
                            LoanRequest newLoanRequest = new LoanRequest(Double.parseDouble(amount.getText().toString()), Integer.parseInt(numOfMonths.getText().toString()), currentUser);
                            currentUser.addLoanRequest(newLoanRequest);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setMessage("Please input the wanted info!");
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    public static class loanRequestHandler extends Handler {
        private final WeakReference<LoanRequestPage> activityReference;

        public loanRequestHandler(LoanRequestPage activity) {
            super(Looper.getMainLooper());
            activityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoanRequestPage activity = activityReference.get();
            if (activity != null) {
                activity.initialize();
            }
        }
    }

}