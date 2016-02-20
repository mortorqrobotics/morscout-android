package org.team1515.morscout.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.team1515.morscout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prozwood on 1/25/16.
 */
public class HelpFragment extends Fragment {

    List<TextView> questions;
    String tempText;
    String tempQuestion;

    Spannable spannable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        questions = new ArrayList<TextView>();
        questions.add((TextView) view.findViewById(R.id.help_question1));
        questions.add((TextView) view.findViewById(R.id.help_question2));

        for(int i = 0; i < questions.size(); i++) {
            tempText = questions.get(i).getText().toString();
            tempQuestion = tempText.substring(0, tempText.indexOf("\n\nA."));

            spannable = new SpannableString(tempText);

            spannable.setSpan(new ForegroundColorSpan(Color.rgb(225, 124, 16)), 0, tempQuestion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, tempQuestion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            questions.get(i).setText(spannable, TextView.BufferType.SPANNABLE);
        }

        return view;
    }
}
