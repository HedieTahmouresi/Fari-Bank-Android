package ir.ac.kntu;

import static ir.ac.kntu.R.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    public void initialize(){
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        recyclerView = (RecyclerView) findViewById(R.id.contactRecyclerReview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ContactAdapter(currentUser.getContacts(), this, currentUser.getSimCard().getPhoneNumber());
        recyclerView.setAdapter(mAdapter);
        seekBar = (SeekBar) findViewById(id.contactSeekBar);
        seekBar.setMax(currentUser.getContacts().size() - 1);
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
        onClick(currentUser, ContactPage.this);
    }

    public void onClick(SimpleUser currentUser, Context context){
        button = (FloatingActionButton) findViewById(id.addContact);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ContactPage.this, AddContact.class);
                        intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

}