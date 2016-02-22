package org.team1515.morscout.fragment.match;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.team1515.morscout.R;
import org.team1515.morscout.entity.FormItem;
import org.team1515.morscout.network.CookieRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoutFragment extends Fragment {
    private RequestQueue queue;
    private SharedPreferences preferences;

    List<FormItem> formItems;

    Button submit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchscout, container, false);

        preferences = getActivity().getSharedPreferences(null, 0);
        queue = Volley.newRequestQueue(getContext());

        submit = (Button) view.findViewById(R.id.matchscout_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        createForm((LinearLayout) view.findViewById(R.id.matchscout_form));

        return view;
    }

    private void createForm(LinearLayout view) {
        formItems = new Gson().fromJson(preferences.getString("matchForm", ""), new TypeToken<List<FormItem>>() {
        }.getType());

        for (FormItem formItem : formItems) {
            View item = null;
            switch (formItem.getType()) {
                case "label":
                    item = new TextView(view.getContext());
                    ((TextView) item).setTextSize(20);
                    ((TextView) item).setText(formItem.getName());
                    break;
                case "text":
                    item = new EditText(view.getContext());
                    ((EditText) item).setHint(formItem.getName());
                    break;
                case "checkbox":
                    item = new CheckBox(view.getContext());
                    ((CheckBox) item).setText(formItem.getName());
                    break;
                case "radio":
                    item = new RadioButton(view.getContext());
                    ((RadioButton) item).setText(formItem.getName());
                    break;
                case "number":
                    LinearLayout boxGroup = new LinearLayout(view.getContext());
                    boxGroup.setOrientation(LinearLayout.HORIZONTAL);
                    boxGroup.setGravity(Gravity.CENTER);

                    //Stores the current value in the textbox
                    final int value[] = {0};
                    final int min = formItem.getMin();
                    final int max = formItem.getMax();

                    // Editable text box
                    final EditText editText = new EditText(view.getContext());
                    editText.setText("" + value[0]);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (editText.getText().toString().isEmpty()) {
                                value[0] = 0;
                                editText.setText("" + value[0]);
                            }
                        }
                    });

                    // Decrement button
                    Button decrementButton = new Button(view.getContext());
                    decrementButton.setText("-");
                    decrementButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (value[0] > min) {
                                value[0]--;
                                editText.setText("" + value[0]);
                            }
                        }
                    });

                    // Increment button
                    Button incrementButton = new Button(view.getContext());
                    incrementButton.setText("+");
                    incrementButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (value[0] < max) {
                                value[0]++;
                                editText.setText("" + value[0]);
                            }
                        }
                    });

                    boxGroup.addView(decrementButton);
                    boxGroup.addView(editText);
                    boxGroup.addView(incrementButton);
                    view.addView(boxGroup);
                    break;
                case "dropdown":
                    List<String> choices = new ArrayList<>();
                    for (int i = 0; i < formItem.getOptions().size(); i++) {
                        choices.add(formItem.getOptions().get(i));
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, choices);

                    item = new Spinner(view.getContext());
                    ((Spinner) item).setAdapter(spinnerArrayAdapter);
                    break;
            }

            if (item != null) {
                item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                view.addView(item);
            }
        }
    }

    public void submitForm() {
        Map<String, String> params = new HashMap<>();
        params.put("context", "match");

        CookieRequest submitionRequest = new CookieRequest(Request.Method.POST, "/getRegionalsForTeam", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(submitionRequest);
    }
}
