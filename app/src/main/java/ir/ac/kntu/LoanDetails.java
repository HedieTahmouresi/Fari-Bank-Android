package ir.ac.kntu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.ref.WeakReference;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import ir.ac.kntu.util.Calendar;

public class LoanDetails extends AppCompatActivity {
    private Button pay;
    private TextView id;
    private TextView amount;
    private TextView paymentID;
    private TextView paymentDue;
    private TextView beenPayed;
    private TextView delays;
    private TextView paymentsLeft;
    private static TextView fullPay;
    private static LoanHandler handler;

    public static LoanHandler getHandler() {
        return handler;
    }

    public static void setHandler(LoanHandler handler) {
        LoanDetails.handler = handler;
    }

    public static TextView getFullPay() {
        return fullPay;
    }

    public static void setFullPay(TextView fullPay) {
        LoanDetails.fullPay = fullPay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setHandler(new LoanHandler(this));
        initialize();
    }

    public void initialize() {
        setFullPay(findViewById(R.id.fullPayment));
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        Loan currentLoan = currentUser.findLoan(getIntent().getStringExtra("loan ID"));
        if (currentLoan.hasBeenFullyPaid()) {
            getFullPay().setText("This Loan has been fully paid!");
        }
        id = (TextView) findViewById(R.id.loanID);
        amount = (TextView) findViewById(R.id.loanAmount);
        id.setText(currentLoan.getId());
        amount.setText(Double.toString(currentLoan.getAmount()));
        initializePayment(currentUser, currentLoan);
    }

    @SuppressLint("SetTextI18n")
    public void initializePayment(SimpleUser currentUser, Loan currentLoan) {
        Payment currentPayment = currentLoan.getCurrentPayment();
        ZonedDateTime zonedDateTime = currentPayment.getDueDate().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        paymentID = (TextView) findViewById(R.id.paymentID);
        paymentID.setText(Integer.toString(currentPayment.getId()));
        paymentDue = (TextView) findViewById(R.id.paymentDue);
        paymentDue.setText(datePart.toString());
        beenPayed = (TextView) findViewById(R.id.hasBeenPayed);
        if (currentPayment.hasBeenPayed()) {
            beenPayed.setText("True");
        } else {
            beenPayed.setText("False");
        }
        paymentsLeft = (TextView) findViewById(R.id.paymentsLeft);
        String left = Integer.toString(currentLoan.getNumOfMonths() - currentPayment.getId());
        paymentsLeft.setText(left);
        delays = (TextView) findViewById(R.id.paymentDelays);
        Instant now = Calendar.now();
        Duration duration = Duration.between(currentPayment.getDueDate(), now);
        long numOfDays = duration.toDays();
        if (numOfDays > 0) {
            delays.setText("True");
        } else {
            delays.setText("False");
        }
        onClickPay(currentUser, currentLoan, beenPayed);
    }


    public void onClickPay(SimpleUser currentUser, Loan currentLoan, TextView textView) {
        pay = (Button) findViewById(R.id.payLoan);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLoan.getCurrentPayment().pay(LoanDetails.this, textView);
                initializePayment(currentUser, currentLoan);
            }
        });
    }

    public static class LoanHandler extends Handler {
        private final WeakReference<LoanDetails> activityReference;

        public LoanHandler(LoanDetails activity) {
            super(Looper.getMainLooper());
            activityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            LoanDetails activity = activityReference.get();
            if (activity != null) {
                activity.initialize();
            }
        }
    }
}