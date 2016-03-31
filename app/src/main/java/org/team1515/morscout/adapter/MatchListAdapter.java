package org.team1515.morscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {
    private List<Match> matches;

    public MatchListAdapter() {
        this.matches = new ArrayList<>();
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_match, parent, false);
        ViewHolder viewHolder = new ViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match currentMatch = matches.get(position);

        TextView MatchNumber = (TextView) holder.layout.findViewById(R.id.matchlist_matchNumber);
        MatchNumber.setText("Match " + currentMatch.getNumber());

        TextView[] blueTeams = new TextView[3];
        blueTeams[0] = (TextView) holder.layout.findViewById(R.id.matchlist_blueTeam1);
        blueTeams[1] = (TextView) holder.layout.findViewById(R.id.matchlist_blueTeam2);
        blueTeams[2] = (TextView) holder.layout.findViewById(R.id.matchlist_blueTeam3);

        for(int i = 0; i < 3; i++) {
            blueTeams[i].setText(currentMatch.getBlueAlliance()[i]);
        }

        TextView[] redTeams = new TextView[3];
        redTeams[0] = (TextView) holder.layout.findViewById(R.id.matchlist_redTeam1);
        redTeams[1] = (TextView) holder.layout.findViewById(R.id.matchlist_redTeam2);
        redTeams[2] = (TextView) holder.layout.findViewById(R.id.matchlist_redTeam3);

        for(int i = 0; i < 3; i++) {
            redTeams[i].setText(currentMatch.getRedAlliance()[i]);
        }

        TextView matchScoutProgress = (TextView) holder.layout.findViewById(R.id.matchlist_scoutProgress);
        matchScoutProgress.setText(Integer.toString(currentMatch.getProgress()));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;

        public ViewHolder(LinearLayout layout) {
            super(layout);
            this.layout = layout;
        }

    }
}
