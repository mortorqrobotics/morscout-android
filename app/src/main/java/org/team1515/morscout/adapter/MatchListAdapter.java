package org.team1515.morscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.Match;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<LinearViewHolder> {
    private List<Match> matches;

    DateFormat dateFormat = new SimpleDateFormat("h:mm a");

    public MatchListAdapter() {
        this.matches = new ArrayList<>();
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    public void setProgresses(List<Integer> progresses) {
        if (progresses.size() == matches.size()) {
            for (int i = 0; i < matches.size(); i++) {
                matches.get(i).setProgress(progresses.get(i));
            }
            notifyDataSetChanged();
        } else {
            System.out.println("Could not get progress for matches");
        }
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_match, parent, false);
        LinearViewHolder viewHolder = new LinearViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LinearViewHolder holder, int position) {
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
        if (currentMatch.getProgress() == 6) {
            matchScoutProgress.setText("(Complete)");
        } else if (currentMatch.getProgress() == -1) {
            matchScoutProgress.setText("");
        } else {
            matchScoutProgress.setText("(" + Integer.toString(currentMatch.getProgress()) + " / 6)");
        }

        TextView matchTimeView = (TextView) holder.layout.findViewById(R.id.matchlist_matchTime);
        matchTimeView.setText(dateFormat.format(currentMatch.getTime()));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }
}
