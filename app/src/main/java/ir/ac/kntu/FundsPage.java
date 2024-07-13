package ir.ac.kntu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FundsPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seekBar;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_funds_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void initialize() {
        SimpleUser currentUser = MainActivity.getCurrentUser(getIntent().getStringExtra("Phone Number"));
        recyclerView = (RecyclerView) findViewById(R.id.fundRecyclerReview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FundAdapter(currentUser.getFunds(), this, currentUser.getSimCard().getPhoneNumber());
        recyclerView.setAdapter(mAdapter);
        seekBar = (SeekBar) findViewById(R.id.fundSeekBar);
        seekBar.setMax(currentUser.getFunds().size() - 1);
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

        onClick(currentUser, mAdapter);
    }

    public void onClick(SimpleUser currentUser ,RecyclerView.Adapter mAdapter) {
        button = (FloatingActionButton) findViewById(R.id.addFund);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBonusKinds(currentUser, mAdapter);
                    }
                }
        );
    }

    public void showBonusKinds(SimpleUser currentUser, RecyclerView.Adapter mAdapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What kind of fund do you want to create?");
        Spinner ways = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fund_kinds, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ways.setAdapter(adapter);
        builder.setView(ways);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = (String) ways.getSelectedItem();
                if ("Bonus Fund".equals(selected)){
                    Intent intent = new Intent(FundsPage.this, AddFund.class);
                    intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                    startActivity(intent);
                    mAdapter.notifyDataSetChanged();
                }else{
                    currentUser.addFund(selected, FundsPage.this);
                    mAdapter.notifyDataSetChanged();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Add Fubd");
        warning.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }
}