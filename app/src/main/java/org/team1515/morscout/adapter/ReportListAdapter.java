package org.team1515.morscout.adapter;


import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        TextView title = (TextView) holder.layout.findViewById(R.id.reportlist_title);
        String titleText = "Report " + (position + 1);
        title.setText(titleText);

        //All of this is for testing purposes
        for(FormItem formItem : report) {
            View item = null;

            //Check for titles
            if(formItem.getValue() == null) {
                item = new TextView(holder.layout.getContext());
                ((TextView) item).setText(formItem.getName());
                ((TextView) item).setTextSize(19);
                ((TextView) item).setPaintFlags(((TextView) item).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) item).setGravity(Gravity.CENTER);
                ((TextView) item).setTextColor(item.getResources().getColor(R.color.black));
            } else {
                item = new RelativeLayout(holder.layout.getContext());

                DisplayMetrics displayMetrics = item.getContext().getResources().getDisplayMetrics();
                float screenWidth = displayMetrics.widthPixels;

                TextView key = new TextView(item.getContext());
                RelativeLayout.LayoutParams keyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                keyParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                key.setLayoutParams(keyParams);
                key.setWidth((int) (screenWidth * 3 / 8));
                String keyString = formItem.getName() + ": ";
                key.setText(keyString);
                key.setTextSize(17);
                ((TextView) key).setTextColor(item.getResources().getColor(R.color.black));
                key.setGravity(Gravity.START);

                TextView value = new TextView(item.getContext());
                RelativeLayout.LayoutParams valueParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                valueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                value.setLayoutParams(valueParams);
                value.setWidth((int) (screenWidth * 3 / 8));
                value.setText(formItem.getValue());
                value.setTextSize(17);
                ((TextView) value).setTextColor(item.getResources().getColor(R.color.black));
                value.setGravity(Gravity.START);

                ((RelativeLayout) item).addView(key);
                ((RelativeLayout) item).addView(value);
            }

            if (item != null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Resources r = item.getContext().getResources();
                int margin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2,
                        r.getDisplayMetrics()
                );
                params.setMargins(0, margin, 0, margin);
                item.setLayoutParams(params);
                holder.layout.addView(item);
            }
        }

        //Style report
//        holder.layout.setBackgroundResource(R.drawable.black_border);
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