package edu.gatech.events;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class EventListAdapter implements ListAdapter {

    private Context context;
    private List<Event> events;

    public EventListAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {}

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
        }
        Event event = events.get(i);
        fillView(view, event);
        return view;
    }

    private void fillView(View view, Event event) {
        TextView titleView = (TextView) view.findViewById(android.R.id.text1);
        titleView.setText(event.title);
        TextView timeView = (TextView) view.findViewById(android.R.id.text2);
        timeView.setText(event.time.toString());
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return events.isEmpty();
    }
}
