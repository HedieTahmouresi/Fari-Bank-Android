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

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.FundViewHolder> {

    private List<Fund> funds;
    private Context context;
    private String phoneNumberUser;

    public static class FundViewHolder extends RecyclerView.ViewHolder {
        private TextView kind;
        private TextView id;

        public TextView getKind() {
            return kind;
        }

        public void setKind(TextView type) {
            this.kind = type;
        }

        public TextView getID() {
            return id;
        }

        public void setID(TextView phoneNumber) {
            this.id = phoneNumber;
        }

        public FundViewHolder(View v) {
            super(v);
            kind = v.findViewById(R.id.fundKind);
            id = v.findViewById(R.id.fundID);
        }
    }

    public FundAdapter(List<Fund> funds, Context context, String phoneNumberUser) {
        this.funds = funds;
        this.context = context;
        this.phoneNumberUser = phoneNumberUser;
    }

    @NonNull
    @Override
    public FundAdapter.FundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FundAdapter.FundViewHolder(LayoutInflater.from(context).inflate(R.layout.fund_single, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FundAdapter.FundViewHolder holder, int position) {
        Fund fund = funds.get(position);
        holder.getKind().setText(getKindString(fund));
        holder.getID().setText(fund.getFundID());
        holder.getKind().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FundDetails.class);
                intent.putExtra("fund ID", fund.getFundID());
                intent.putExtra("Phone Number", phoneNumberUser);
                context.startActivity(intent);

            }
        });
    }

    public String getKindString(Fund fund) {
        if (fund instanceof SavingsFund savingsFund) {
            return "Savings Fund";
        } else if (fund instanceof BonusFund bonusFund) {
            return "Bonus Fund";
        } else if (fund instanceof RemainsFund remainsFund) {
            return "Remains Fund";
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return funds.size();
    }
}
