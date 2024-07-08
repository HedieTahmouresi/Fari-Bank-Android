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
        countDown();
    }

    public void countDown(){
        progressBar = findViewById(R.id.progressBar);
        int duration = 10000000/6000;

        new CountDownTimer(duration, 100){

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


    private static void main(String[] args){
        System.out.println("hello");
    }
}