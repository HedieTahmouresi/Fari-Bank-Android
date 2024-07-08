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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private Button signUp;
    private EditText name;
    private EditText lastName;
    private EditText ssn;
    private EditText phoneNumber;
    private EditText password;
    private static Toast messageSSN;
    private static Toast messagePhoneNumber;
    private static Toast messagePassword;

    public static Toast getMessageSSN() {
        return messageSSN;
    }

    public static void setMessageSSN(Toast messageSSN) {
        SignUp.messageSSN = messageSSN;
    }

    public static Toast getMessagePhoneNumber() {
        return messagePhoneNumber;
    }

    public static void setMessagePhoneNumber(Toast messagePhoneNumber) {
        SignUp.messagePhoneNumber = messagePhoneNumber;
    }

    public static Toast getMessagePassword() {
        return messagePassword;
    }

    public static void setMessagePassword(Toast messagePassword) {
        SignUp.messagePassword = messagePassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CentralBank centralBank = MainActivity.getCentralBank();
        NeoBank fariBank = MainActivity.getFariBank();
        setMessagePassword(Toast.makeText(SignUp.this, "...", Toast.LENGTH_SHORT));
        setMessageSSN(Toast.makeText(SignUp.this, "...", Toast.LENGTH_SHORT));
        setMessagePhoneNumber(Toast.makeText(SignUp.this, "...", Toast.LENGTH_SHORT));
        onClick(centralBank, fariBank);
    }


    public boolean checkInfo(String phoneNumber, String ssn, String password, NeoBank fariBank) {
        int flag = fariBank.getBankData().checkPhoneNumber(phoneNumber, "shouldn't exist");
        if (flag == 1) {
            setMessagePhoneNumber(Toast.makeText(SignUp.this, "Wrong format!", Toast.LENGTH_LONG));
            return false;
        } else if (flag == 2) {
            setMessagePhoneNumber(Toast.makeText(SignUp.this, "This phone number already has an account!", Toast.LENGTH_LONG));
            return false;
        }
        flag = fariBank.getBankData().checkSecurityNumber(ssn);
        if (flag == 1) {
            setMessageSSN(Toast.makeText(SignUp.this, "Wrong format!", Toast.LENGTH_LONG));
            return false;
        } else if (flag == 2) {
            setMessageSSN(Toast.makeText(SignUp.this, "This social security number already has an account!", Toast.LENGTH_LONG));
            return false;
        }
        if (password.length() < 8 || !checkPass(password)) {
            setMessagePassword(Toast.makeText(SignUp.this, "Password is too weak! It should contain at least 8 letters, 1 unique character, 1 capital letter, 1 small letter and 1 number!", Toast.LENGTH_SHORT));
            return false;
        }
        return true;
    }

    public boolean checkPass(String password) {
        String numRegEx = "[0-9]";
        Pattern numPattern = Pattern.compile(numRegEx);
        Matcher numMatcher = numPattern.matcher(password);
        String capitalRegEx = "[A-Z]";
        Pattern capitalPattern = Pattern.compile(capitalRegEx);
        Matcher capitalMatcher = capitalPattern.matcher(password);
        String smallRegEx = "[a-z]";
        Pattern smallPattern = Pattern.compile(smallRegEx);
        Matcher smallMatcher = smallPattern.matcher(password);
        String uniqueRegEx = "[@#$%^&*]";
        Pattern uniquePattern = Pattern.compile(uniqueRegEx);
        Matcher uniqueMatcher = uniquePattern.matcher(password);
        if (numMatcher.find() && capitalMatcher.find() && smallMatcher.find() && uniqueMatcher.find()) {
            return true;
        }
        return false;
    }

    public void onClick(CentralBank centralBank, NeoBank fariBank) {
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.passwordSignUp);
        name = (EditText) findViewById(R.id.name);
        lastName = (EditText) findViewById(R.id.lastName);
        ssn = (EditText) findViewById(R.id.ssn);
        signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkInfo(phoneNumber.getText().toString(), ssn.getText().toString(), password.getText().toString(), fariBank)) {
                            Toast.makeText(SignUp.this, "SignUp was successful", Toast.LENGTH_SHORT).show();
                            String[] names = {name.getText().toString(), lastName.getText().toString()};
                            fariBank.getBankData().signUp(names, phoneNumber.getText().toString(), ssn.getText().toString(), password.getText().toString());
                            Authenticate authentication = new Authenticate(fariBank, fariBank.getBankData().getUserByPhone(phoneNumber.getText().toString()));
                            Thread thread = new Thread(authentication);
                            thread.start();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        } else {
                            messagePhoneNumber.show();
                            messagePassword.show();
                            messageSSN.show();
                        }
                    }
                }
        );
    }
}