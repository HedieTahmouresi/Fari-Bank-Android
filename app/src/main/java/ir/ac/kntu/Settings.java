package ir.ac.kntu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends AppCompatActivity {
    private ImageView showInfo;
    private Switch contactOption;
    private ImageView changeUserPass;
    private LinearLayout changingUserPass;
    private Button changePass;
    private EditText oldPassUser;
    private EditText newPassUser;
    private Switch creditPass;
    private LinearLayout changingCreditPass;
    private LinearLayout settingCreditPass;
    private TextView changeCreditPassText;
    private LinearLayout changingPassLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String phoneNumber = getIntent().getStringExtra("Phone Number");
        SimpleUser currentUser = MainActivity.getCurrentUser(phoneNumber);
        contactOption = (Switch) findViewById(R.id.contactOptionSwitch);
        contactOption.setChecked(currentUser.isContactOption());
        creditPass = (Switch) findViewById(R.id.creditPassSwitch);
        creditPass.setChecked(currentUser.getAccount().getCreditCard().hasSetPassword());
        changingCreditPass = (LinearLayout) findViewById(R.id.creditCardPasswordChange);
        if (creditPass.isChecked()){
            changingCreditPass.setVisibility(View.VISIBLE);

        }
        onClickArrowAbout(currentUser);
        onClickContactOption(currentUser, contactOption);
        onClickUserPass(currentUser);
        onClickCreditSwitch(currentUser, creditPass, changingCreditPass);
        onClickChangePass(currentUser, changingCreditPass);
    }

    public void onClickArrowAbout(SimpleUser currentUser){
        showInfo = (ImageView) findViewById(R.id.arrowAbout);
        showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Account Info").setMessage("Account ID : " + currentUser.getAccount().getAccountId() + "\nCreditCard ID : " + currentUser.getAccount().getCreditCard().getCreditCardId()).setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public void onClickContactOption(SimpleUser currentUser, Switch contactOption){
        contactOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentUser.setContactOption(true);
                } else {
                    currentUser.setContactOption(false);
                }
            }
        });
    }

    public void onClickCreditSwitch(SimpleUser currentUser, Switch creditCard, LinearLayout changeLayout){
        creditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    settingCreditPass = (LinearLayout) findViewById(R.id.settingCreditPassLayout);
                    settingCreditPass.setVisibility(View.VISIBLE);
                    setPassword(currentUser, settingCreditPass, changeLayout);
                } else {
                    currentUser.getAccount().getCreditCard().setSetPassword(false);

                }
            }
        });
    }

    public void onClickChangePass(SimpleUser currentUser, LinearLayout changingCreditPass){
        changeCreditPassText = (TextView) findViewById(R.id.changePassCreditButton);
        changeCreditPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changingPassLayout = (LinearLayout) findViewById(R.id.changingCreditPassLayout);
                changingPassLayout.setVisibility(View.VISIBLE);
                changePass = (Button) findViewById(R.id.changeCreditPass);
                changePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeCreditPass(currentUser, changingPassLayout);
                    }
                });
            }
        });
    }

    public void changeCreditPass(SimpleUser currentUser, LinearLayout layout){
        newPassUser = (EditText) findViewById(R.id.newPassCredit);
        oldPassUser = (EditText) findViewById(R.id.oldPassCredit);
        if (oldPassUser.getText().toString().isEmpty() || newPassUser.getText().toString().isEmpty()){
            Toast.makeText(Settings.this, "You can't have an empty field",Toast.LENGTH_SHORT).show();
        }else if (!(oldPassUser.getText().toString()).equals(Integer.toString(currentUser.getAccount().getCreditCard().getPassword()))){
            Toast.makeText(Settings.this, "Please enter your old password correctly", Toast.LENGTH_SHORT).show();
        } else if (newPassUser.getText().toString().length()!=4){
            Toast.makeText(Settings.this, "Your passcode must be 4 numbers", Toast.LENGTH_SHORT).show();
        } else {
            currentUser.getAccount().getCreditCard().setPassword(Integer.parseInt(newPassUser.getText().toString()));
            Toast.makeText(Settings.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            layout.setVisibility(View.GONE);
        }
    }

    public void setPassword(SimpleUser currentUser, LinearLayout setLayout, LinearLayout changeLayout){
        newPassUser = (EditText) findViewById(R.id.passCreditCard);
        changePass = (Button) findViewById(R.id.setCreditPass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassUser.getText().toString().length()!=4){
                    Toast.makeText(Settings.this, "Your passcode should be 4 numbers!", Toast.LENGTH_SHORT);
                } else {
                    currentUser.getAccount().getCreditCard().setSetPassword(true);
                    currentUser.getAccount().getCreditCard().setPassword(Integer.parseInt(newPassUser.getText().toString()));
                    setLayout.setVisibility(View.GONE);
                    changeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onClickUserPass(SimpleUser currentUser){
        changeUserPass = (ImageView) findViewById(R.id.arrowUserPass);
        changingUserPass = (LinearLayout) findViewById(R.id.changingUserPassLayout);
        changeUserPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changingUserPass.getVisibility() == View.GONE) {
                    changingUserPass.setVisibility(View.VISIBLE);
                    changePass = (Button) findViewById(R.id.changeUserPass);
                    changePass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changePass(currentUser, changingUserPass);
                        }
                    });
                } else {
                    changingUserPass.setVisibility(View.GONE);
                }
            }
        });
    }

    public void changePass(SimpleUser currentUser, LinearLayout layout){
        oldPassUser = (EditText) findViewById(R.id.oldPassUser);
        newPassUser = (EditText) findViewById(R.id.newPassUser);
        if (oldPassUser.getText().toString().isEmpty() || newPassUser.getText().toString().isEmpty()){
            Toast.makeText(Settings.this, "You can't have an empty field",Toast.LENGTH_SHORT).show();
        }else if (!oldPassUser.getText().toString().equals(currentUser.getPassword())){
            Toast.makeText(Settings.this, "Please enter your old password correctly", Toast.LENGTH_SHORT).show();
        } else if (!checkPass(newPassUser.getText().toString()) || newPassUser.getText().toString().length()<8){
            Toast.makeText(Settings.this, "Password is too weak! It should contain at least 8 letters, 1 unique character, 1 capital letter, 1 small letter and 1 number!", Toast.LENGTH_SHORT).show();
        } else {
            currentUser.setPassword(newPassUser.getText().toString());
            Toast.makeText(Settings.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            layout.setVisibility(View.GONE);
        }
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
}