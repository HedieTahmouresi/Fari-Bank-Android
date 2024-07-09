package ir.ac.kntu;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    List<Transaction> transactions;
    Context context;

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView type;
        public TextView transactionInfo;

        public TransactionViewHolder(View v) {
            super(v);
            type = v.findViewById(R.id.textView4);
            transactionInfo = v.findViewById(R.id.textView5);
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

    public String getType(Transaction transaction){
        if (transaction instanceof ChargeTransaction chargeTransaction){
            return " ~Charge Account";
        } else if (transaction instanceof SimChargeTransaction simChargeTransaction){
            return " ~Charge Sim Card";
        } else if (transaction instanceof TransferInsideTransaction transferInsideTransaction){
            return " ~Inside Transfer";
        } else if (transaction instanceof TransferTransaction){
            return " ~Transfer";
        }
        return " ~Wire Transfer";
    }
    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.type.setText(getType(transaction));
        holder.transactionInfo.setText(transaction.toString());
        holder.transactionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetails.class);
                intent.putExtra("Info", transaction.showInfo(MainActivity.getFariBank()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
