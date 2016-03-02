package org.team1515.morscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.FormItem;

import java.util.ArrayList;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {
    private List<List<FormItem>> reports;

    public ReportListAdapter() {
        this.reports = new ArrayList<>();
    }

    public void setReports(List<List<FormItem>> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_report, parent, false);
        ViewHolder viewHolder = new ViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<FormItem> report = reports.get(position);

        //All of this is for testing purposes
        for(FormItem formItem : report) {
            if(formItem.getValue() != null) {
                TextView items = (TextView) holder.layout.findViewById(R.id.reportlist_items);
                items.setText(items.getText().toString() + "\n" + formItem.getName() + "\t" + formItem.getValue());
            } else {
                TextView title = (TextView) holder.layout.findViewById(R.id.reportlist_items);
                title.setText(title.getText().toString() + "\n" + formItem.getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;

        public ViewHolder(LinearLayout layout) {
            super(layout);
            this.layout = layout;
        }

    }
}