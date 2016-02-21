package org.team1515.morscout.fragment.match;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.FormItem;

import java.util.List;

public class ScoutFragment extends Fragment {
    private SharedPreferences preferences;
    private RequestQueue queue;

    List<FormItem> formItems;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchscout, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        createForm((LinearLayout) view.findViewById(R.id.matchscout_form));

        return view;
    }

    private void createForm(LinearLayout view) {
        formItems = new Gson().fromJson(preferences.getString("matchForm", ""), new TypeToken<List<FormItem>>(){}.getType());

        for(FormItem formItem : formItems) {
            View item = null;
            switch(formItem.getType()) {
                case "label":
                    item = new TextView(view.getContext());
                    ((TextView)item).setTextSize(20);
                    ((TextView)item).setText(formItem.getName());
                    break;
                case "text":
                    item = new EditText(view.getContext());
                    ((EditText)item).setHint(formItem.getName());
                    break;
                case "checkbox":
                    item = new CheckBox(view.getContext());
                    ((CheckBox)item).setText(formItem.getName());
            }

            if(item != null) {
                item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                view.addView(item);
            }
        }
    }
}
