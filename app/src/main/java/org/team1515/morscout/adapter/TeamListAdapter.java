package org.team1515.morscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {

    private List<Team> teams;

    public TeamListAdapter() {
        this.teams = new ArrayList<>();
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_team, parent, false);
        ViewHolder viewHolder = new ViewHolder(layout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team currentTeam = teams.get(position);

        TextView teamNumber = (TextView) holder.layout.findViewById(R.id.teamlist_teamNumber);
        teamNumber.setText(Integer.toString(currentTeam.getNumber()));

        TextView teamName = (TextView) holder.layout.findViewById(R.id.teamlist_teamName);
        teamName.setText(currentTeam.getName());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;

        public ViewHolder(LinearLayout layout) {
            super(layout);
            this.layout = layout;
        }

    }
}