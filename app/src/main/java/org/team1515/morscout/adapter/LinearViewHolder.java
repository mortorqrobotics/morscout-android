package org.team1515.morscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class LinearViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout layout;

    public LinearViewHolder(View itemView) {
        super(itemView);

        this.layout = (LinearLayout) itemView;
    }
}
