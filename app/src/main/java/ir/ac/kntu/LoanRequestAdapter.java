package ir.ac.kntu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanRequestAdapter extends RecyclerView.Adapter<LoanRequestAdapter.LoanRequestViewHolder> {
    private List<LoanRequest> loanRequests;
    private Context context;
    private String phoneNumberUser;

    public LoanRequestAdapter(List<LoanRequest> loanRequests, Context context, String phoneNumberUser) {
        this.loanRequests = loanRequests;
        this.context = context;
        this.phoneNumberUser = phoneNumberUser;
    }

    @NonNull
    @Override
    public LoanRequestAdapter.LoanRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LoanRequestAdapter.LoanRequestViewHolder(LayoutInflater.from(context).inflate(R.layout.loan_request_single, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoanRequestAdapter.LoanRequestViewHolder holder, int position) {
        LoanRequest loan = loanRequests.get(position);
        holder.getLoanInfo().setText(loan.toString());
        holder.getLoanInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Loan Request Info").setMessage(loan.toString() + "\n" + loan.getReason());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return loanRequests.size();
    }

    public static class LoanRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView loanInfo;
        private TextView request;

        public TextView getRequest() {
            return request;
        }

        public void setRequest(TextView request) {
            this.request = request;
        }

        public TextView getLoanInfo() {
            return loanInfo;
        }

        public void setLoanInfo(TextView loanInfo) {
            this.loanInfo = loanInfo;
        }

        public LoanRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.request = itemView.findViewById(R.id.kk);
            this.loanInfo = itemView.findViewById(R.id.ll);
        }
    }
}
