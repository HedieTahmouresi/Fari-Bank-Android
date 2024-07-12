package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {
    private ImageView contact;
    private ImageView support;
    private ImageView settings;
    private ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String phoneNumber = getIntent().getStringExtra("Phone Number");
        SimpleUser currentUser = MainActivity.getCurrentUser(phoneNumber);
        onClickContact(currentUser);
        onClickSettings(currentUser);
        onClickSupport(currentUser);
        onClickLogOut();
    }

    public void onClickContact(SimpleUser currentUser) {
        contact = (ImageView) findViewById(R.id.arrowContact);
        contact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentUser.isContactOption()) {
                            Intent intent = new Intent(Profile.this, ContactPage.class);
                            intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                            startActivity(intent);
                        } else {
                            Toast.makeText(Profile.this, "Your contact option is off!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void onClickSupport(SimpleUser currentUser) {
        support = (ImageView) findViewById(R.id.arrowSupport);
        support.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Profile.this, Support.class);
                        intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickSettings(SimpleUser currentUser) {
        settings = (ImageView) findViewById(R.id.arrowSettings);
        settings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Profile.this, Settings.class);
                        intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickLogOut() {
        logout = (ImageButton) findViewById(R.id.logOut);
        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Profile.this, Login.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}