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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView transactionInfo;

        public TextView getType() {
            return type;
        }

        public void setType(TextView type) {
            this.type = type;
        }

        public TextView getTransactionInfo() {
            return transactionInfo;
        }

        public void setTransactionInfo(TextView transactionInfo) {
            this.transactionInfo = transactionInfo;
        }

        public TransactionViewHolder(View v) {
            super(v);
            type = v.findViewById(R.id.nameAndLastName);
            transactionInfo = v.findViewById(R.id.number);
        }
    }

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.transaction, parent, false));
    }

    public String getType(Transaction transaction) {
        if (transaction instanceof ChargeTransaction chargeTransaction) {
            return " ~Charge Account";
        }else if (transaction instanceof TransferInsideTransaction transferInsideTransaction) {
            return " ~Inside Transfer";
        } else if (transaction instanceof TransferTransaction) {
            return " ~Transfer";
        } else if (transaction instanceof PaymentTransfer) {
            return " ~Loan Payment";
        }
        return " ~Wire Transfer";
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.getType().setText(getType(transaction));
        holder.getTransactionInfo().setText(transaction.toString());
        holder.getTransactionInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetails.class);
                intent.putExtra("Info", transaction.showInfo(MainActivity.getFariBank()));
                context.startActivity(intent);
            }
        });
    }

    public void addItem(Transaction item) {
        transactions.add(item);
        notifyItemInserted(transactions.size() - 1);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
