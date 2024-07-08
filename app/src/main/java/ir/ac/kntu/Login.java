package ir.ac.kntu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText phoneNumber;
    private EditText password;

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
        NeoBank fariBank = new NeoBank("21995282");
        Helper helper = new Helper();
        helper.initiateFari(fariBank);
        CentralBank centralBank = new CentralBank();
        helper.initiateCentralBank(centralBank, fariBank);
        onClickSignUp(centralBank, fariBank);
        onClickLogin(centralBank, fariBank);
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

    public boolean checkUserAndPass(String phoneNumber, String password, NeoBank fariBank){
        SimpleUser currentUser = fariBank.getBankData().getUserByPhone(phoneNumber);
        if (!password.equals(currentUser.getPassword())) {
            return false;
        }
        return true;
    }

    public void onClickLogin(CentralBank centralBank, NeoBank fariBank){
        phoneNumber = (EditText) findViewById(R.id.editTextPhone);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        login = (Button) findViewById(R.id.button2);
        String phone = phoneNumber.getText().toString();
        String pass = password.getText().toString();
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int flag = fariBank.getBankData().checkPhoneNumber(phone, "should exist");
                        if (flag==0){
                            if (checkUserAndPass(phone, pass, fariBank)){
                                Intent intent = new Intent(Login.this, DashBoard.class);
                                intent.putExtra("Central Bank", centralBank);
                                intent.putExtra("Fari Bank", fariBank);
                                startActivity(intent);
                            }
                            Toast.makeText(Login.this, "The phone number and password don't match", Toast.LENGTH_LONG).show();
                        } else if(flag==1){
                            Toast.makeText(Login.this, "Wrong phone number format", Toast.LENGTH_LONG).show();
                        } else if (flag==3){
                            Toast.makeText(Login.this, "No user with this phone number exists", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }


}