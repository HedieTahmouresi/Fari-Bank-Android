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


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requests;
    private Context context;
    private String phoneNumberUser;

    public RequestAdapter(List<Request> requests, Context context, String phoneNumberUser) {
        this.requests = requests;
        this.context = context;
        this.phoneNumberUser = phoneNumberUser;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestAdapter.RequestViewHolder(LayoutInflater.from(context).inflate(R.layout.request_single, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.getSection().setText(" ~".concat(request.getSection().toString()));
        holder.getRequestInfo().setText(request.toString());
        holder.getRequestInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestDetails.class);
                intent.putExtra("info", request.showInfo(MainActivity.getFariBank().getBankData()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView section;
        private TextView requestInfo;

        public TextView getSection() {
            return section;
        }

        public void setSection(TextView type) {
            this.section = type;
        }

        public TextView getRequestInfo() {
            return requestInfo;
        }

        public void setRequestInfo(TextView info) {
            this.requestInfo = info;
        }

        public RequestViewHolder(View v) {
            super(v);
            section = v.findViewById(R.id.requestSection);
            requestInfo = v.findViewById(R.id.requestInfo);
        }
    }
}
