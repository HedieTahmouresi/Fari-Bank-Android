package ir.ac.kntu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecentSpinnerAdapter extends ArrayAdapter<Recent> {
    public RecentSpinnerAdapter(Context context, List<Recent> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.recent_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.recent_item);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        Recent item = getItem(position);
        TextView textView = (TextView) view;
        if (item != null) {
            textView.setText(item.getPerson().getName() + " " + item.getPerson().getLastName());
        }

        return view;
    }
}
