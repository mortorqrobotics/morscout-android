package org.team1515.morscout.fragment.match;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team1515.morscout.R;
import org.team1515.morscout.entity.FormItem;
import org.team1515.morscout.network.CookieRequest;
import org.w3c.dom.Text;

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
                    item = new RadioGroup(view.getContext());
                    for (int i = 0; i < formItem.getOptions().size(); i++) {
                        RadioButton button = new RadioButton(view.getContext());
                        button.setText(formItem.getOptions().get(i));
                        ((RadioGroup)item).addView(button);
                    }
                    break;
                case "number":
                    item = new LinearLayout(view.getContext());
                    ((LinearLayout) item).setOrientation(LinearLayout.HORIZONTAL);
                    ((LinearLayout) item).setGravity(Gravity.CENTER);

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

                    ((LinearLayout) item).addView(decrementButton);
                    ((LinearLayout) item).addView(editText);
                    ((LinearLayout) item).addView(incrementButton);
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
                item.setTag(formItem.getName());
                item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                view.addView(item);
            }
        }
    }

    public void submitForm() {
        JSONArray data = new JSONArray();

        LinearLayout form = (LinearLayout) getView().findViewById(R.id.matchscout_form);
        for(int i = 0; i < form.getChildCount(); i++) {
            View item = form.getChildAt(i);

            JSONObject dataObject = new JSONObject();
            try {
                if(item instanceof EditText) { // Text
                    dataObject.put("name", item.getTag().toString());
                    dataObject.put("value", ((EditText) item).getText().toString());
                } else if (item instanceof CheckBox) {// Check
                    dataObject.put("name", item.getTag().toString());
                    dataObject.put("value", ((CheckBox) item).isChecked());
                } else if (item instanceof TextView) {
                    dataObject.put("name", item.getTag().toString());
                } else if (item instanceof RadioGroup) { // Radio
                    dataObject.put("name", item.getTag().toString());
                    RadioButton selectedButton = (RadioButton) ((RadioGroup) item).getChildAt(((RadioGroup) item).indexOfChild(item.findViewById(((RadioGroup) item).getCheckedRadioButtonId())));
                    if(selectedButton != null) {
                        dataObject.put("value", selectedButton.getText().toString());
                    } else {
                        dataObject.put("value", "");
                    }
                } else if (item instanceof LinearLayout) { // Number
                    for (int j = 0; j < ((LinearLayout) item).getChildCount(); j++) {
                        if (((LinearLayout) item).getChildAt(j) instanceof EditText) {
                            dataObject.put("name", item.getTag().toString());
                            dataObject.put("value", ((EditText) ((LinearLayout) item).getChildAt(j)).getText().toString());
                        }
                    }
                } else if (item instanceof Spinner) { // Dropdown
                    dataObject.put("name", item.getTag().toString());
                    dataObject.put("value", ((Spinner) item).getSelectedItem().toString());
                } else {
                    //Something screwy
                }

                data.put(dataObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }

        System.out.println(data.toString());

        Map<String, String> params = new HashMap<>();
        params.put("data", data.toString());
        params.put("team", String.valueOf(getArguments().getInt("team")));
        params.put("context", "match");
        params.put("match", String.valueOf(getArguments().getInt("match")));
        System.out.println(getArguments().getInt("match") + "\t" + getArguments().getInt("team"));

        CookieRequest submissionRequest = new CookieRequest(Request.Method.POST, "/submitReport", params, preferences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(submissionRequest);
    }
}
