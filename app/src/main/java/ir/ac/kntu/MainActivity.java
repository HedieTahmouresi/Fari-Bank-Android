package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private static NeoBank fariBank = new NeoBank("21995282");
    private static Helper helper = new Helper();
    private static CentralBank centralBank = new CentralBank();

    public static NeoBank getFariBank() {
        return fariBank;
    }

    public static void setFariBank(NeoBank fariBank) {
        MainActivity.fariBank = fariBank;
    }

    public static Helper getHelper() {
        return helper;
    }

    public static void setHelper(Helper helper) {
        MainActivity.helper = helper;
    }

    public static CentralBank getCentralBank() {
        return centralBank;
    }

    public static void setCentralBank(CentralBank centralBank) {
        MainActivity.centralBank = centralBank;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getHelper().initiateFari(getFariBank());
        getHelper().initiateCentralBank(getCentralBank(), getFariBank());
        countDown();
    }

    public void countDown() {
        progressBar = findViewById(R.id.progressBar);
        int duration = 10000000 / 6000;

        new CountDownTimer(duration, 100) {

            @Override
            public void onTick(long l) {
                progressStatus = (int) ((duration - l) / (float) duration * 100);
                progressBar.setProgress(progressStatus);
            }

            @Override
            public void onFinish() {
                progressStatus = 100;
                progressBar.setProgress(progressStatus);
                Intent intent = new Intent(MainActivity.this, RoleSelection.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public static SimpleUser getCurrentUser(String phoneNumber) {
        return fariBank.getBankData().getUserByPhone(phoneNumber);
    }

    private static void main(String[] args) {
        System.out.println("hello");
    }
}