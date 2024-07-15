package ir.ac.kntu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {
    private List<Loan> loans;
    private Context context;
    private String phoneNumberUser;


    public LoanAdapter(List<Loan> loans, Context context, String phoneNumberUser) {
        this.loans = loans;
        this.context = context;
        this.phoneNumberUser = phoneNumberUser;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LoanAdapter.LoanViewHolder(LayoutInflater.from(context).inflate(R.layout.loan_single, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loans.get(position);
        holder.getLoanID().setText(loan.getId());
        holder.getLoanInfo().setText(loan.toString());
        holder.getLoanID().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanDetails.class);
                intent.putExtra("loan ID", loan.getId());
                intent.putExtra("Phone Number", phoneNumberUser);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder{
        private TextView loanID;
        private TextView loanInfo;

        public TextView getLoanID() {
            return loanID;
        }

        public void setLoanID(TextView loanID) {
            this.loanID = loanID;
        }

        public TextView getLoanInfo() {
            return loanInfo;
        }

        public void setLoanInfo(TextView loanInfo) {
            this.loanInfo = loanInfo;
        }

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            this.loanID = itemView.findViewById(R.id.loanIDText);
            this.loanInfo = itemView.findViewById(R.id.loanInfo);
        }
    }
}
