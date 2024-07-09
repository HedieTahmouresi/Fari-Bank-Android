package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private ImageButton show;
    private Button login;
    private EditText phoneNumber;
    private EditText password;

    private static Toast message;

    public static Toast getMessage() {
        return message;
    }

    public static void setMessage(Toast message) {
        Login.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CentralBank centralBank = MainActivity.getCentralBank();
        NeoBank fariBank = MainActivity.getFariBank();
        onClickSignUp(centralBank, fariBank);
        onClickLogin(centralBank, fariBank);
        showPass();
    }

    public void onClickSignUp(CentralBank centralBank, NeoBank fariBank) {
        Button signUp = (Button) findViewById(R.id.button3);
        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Login.this, SignUp.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public boolean checkUserAndPass(String phoneNumber, String password, NeoBank fariBank) {
        SimpleUser currentUser = fariBank.getBankData().getUserByPhone(phoneNumber);
        if (!password.equals(currentUser.getPassword())) {
            setMessage(Toast.makeText(Login.this, "The phone number and password don't match", Toast.LENGTH_LONG));
            return false;
        }
        if (currentUser.isBlocked()) {
            setMessage(Toast.makeText(Login.this, "This user is blocked", Toast.LENGTH_LONG));
            return false;
        }
        if (!currentUser.getAuthenticated().isAuthenticated()) {
            setMessage(Toast.makeText(Login.this, currentUser.getAuthenticated().showRejection(), Toast.LENGTH_LONG));
            return false;
        }
        return true;
    }

    public void onClickLogin(CentralBank centralBank, NeoBank fariBank) {
        phoneNumber = (EditText) findViewById(R.id.editTextPhone);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        login = (Button) findViewById(R.id.button2);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int flag = fariBank.getBankData().checkPhoneNumber(phoneNumber.getText().toString(), "should exist");
                        if (phoneNumber.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                            Toast.makeText(Login.this, "You can't have an empty field", Toast.LENGTH_LONG).show();
                        } else if (flag == 0) {
                            if (checkUserAndPass(phoneNumber.getText().toString(), password.getText().toString(), fariBank)) {
                                Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, DashBoard.class);
                                intent.putExtra("Phone Number", phoneNumber.getText().toString());
                                startActivity(intent);
                            } else {
                                getMessage().show();
                            }
                        } else if (flag == 1) {
                            Toast.makeText(Login.this, "Wrong phone number format", Toast.LENGTH_LONG).show();
                        } else if (flag == 3) {
                            Toast.makeText(Login.this, "No user with this phone number exists", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void showPass() {
        password = (EditText) findViewById(R.id.editTextTextPassword);
        show = (ImageButton) findViewById(R.id.showPass);
        show.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Login.this, password.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


}