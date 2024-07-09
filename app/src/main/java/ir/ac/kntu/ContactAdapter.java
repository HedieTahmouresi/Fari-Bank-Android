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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private Context context;
    private String phoneNumberUser;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView phoneNumber;

        public TextView getName() {
            return name;
        }

        public void setName(TextView type) {
            this.name = type;
        }

        public TextView getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(TextView phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public ContactViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.nameAndLastName);
            phoneNumber = v.findViewById(R.id.number);
        }
    }

    public ContactAdapter(List<Contact> contacts, Context context, String phoneNumberUser) {
        this.contacts = contacts;
        this.context = context;
        this.phoneNumberUser = phoneNumberUser;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactAdapter.ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_single, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.getName().setText(contact.getName().concat(" ").concat(contact.getLastName()));
        holder.getPhoneNumber().setText(contact.getSimCard().getPhoneNumber());
        holder.getName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("Contact Info", contact.getSimCard().getPhoneNumber());
                intent.putExtra("User Number", phoneNumberUser);
                context.startActivity(intent);
            }
        });
    }

    public void addItem(Contact item) {
        contacts.add(item);
        notifyItemInserted(contacts.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < contacts.size()) {
            contacts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateItem(int position, Contact newItem) {
        if (position >= 0 && position < contacts.size()) {
            contacts.set(position, newItem);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
