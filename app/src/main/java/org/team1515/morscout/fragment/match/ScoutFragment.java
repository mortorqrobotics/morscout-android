package org.team1515.morscout.fragment.match;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
            if(formItem.getType().equals("label")) {
                item = new TextView(view.getContext());
                ((TextView) item).setText(formItem.getName());
                ((TextView) item).setTextSize(20);
                ((TextView) item).setPaintFlags(((TextView) item).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ((TextView) item).setGravity(Gravity.CENTER);
                ((TextView) item).setTextColor(item.getResources().getColor(R.color.black));
            } else {
                item = new RelativeLayout(view.getContext());
                ((RelativeLayout) item).setGravity(Gravity.CENTER);

                RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                switch (formItem.getType()) {
                    case "text":
                        EditText textbox = new EditText(view.getContext());
                        textbox.setHint(formItem.getName());
                        textbox.setSingleLine(false);
                        textbox.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                        textbox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        textbox.setLines(3);
                        textbox.setGravity(Gravity.TOP);
                        textbox.setPadding(4, 4, 4, 4);
                        textbox.setBackgroundResource((R.drawable.edittext_border));
                        textbox.setLayoutParams(centerParams);
                        ((RelativeLayout) item).addView(textbox);
                        break;
                    case "checkbox":
                        CheckBox checkBox = new CheckBox(view.getContext());
                        checkBox.setText(formItem.getName());
                        checkBox.setLayoutParams(centerParams);
                        break;
                    case "radio":
                        LinearLayout container = new LinearLayout(view.getContext());
                        container.setOrientation(LinearLayout.VERTICAL);
                        container.setLayoutParams(centerParams);

                        TextView title = new TextView(view.getContext());
                        title.setText(formItem.getName());
                        title.setTextSize(18);
                        title.setTextColor(item.getResources().getColor(R.color.black));

                        RadioGroup radioGroup = new RadioGroup(view.getContext());
                        for (int i = 0; i < formItem.getOptions().size(); i++) {
                            RadioButton button = new RadioButton(view.getContext());
                            button.setText(formItem.getOptions().get(i));
                            radioGroup.addView(button);
                        }

                        container.addView(title);
                        container.addView(radioGroup);

                        ((RelativeLayout) item).addView(container);
                        break;
                    case "number":
                        title = new TextView(view.getContext());
                        title.setText(formItem.getName() + ": ");
                        title.setTextSize(18);
                        title.setTextColor(item.getResources().getColor(R.color.black));
                        title.setLayoutParams(leftParams);
                        title.setGravity(Gravity.CENTER);

                        container = new LinearLayout(view.getContext());
                        container.setOrientation(LinearLayout.HORIZONTAL);
                        container.setLayoutParams(rightParams);
                        container.setGravity(Gravity.CENTER);

                        //Stores the current value in the textbox
                        final int value[] = {0};
                        final int min = formItem.getMin();
                        final int max = formItem.getMax();

                        // Editable text box
                        final TextView numberView = new TextView(view.getContext());
                        numberView.setTextSize(18);
                        numberView.setText("" + value[0]);

                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, 150);
                        buttonParams.setMargins(10, 0, 10, 0);

                        // Decrement button
                        Button decrementButton = new Button(view.getContext());
                        decrementButton.setLayoutParams(buttonParams);
                        decrementButton.setText("-");
                        decrementButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (value[0] > min) {
                                    value[0]--;
                                    numberView.setText("" + value[0]);
                                }
                            }
                        });

                        // Increment button
                        Button incrementButton = new Button(view.getContext());
                        incrementButton.setLayoutParams(buttonParams);
                        incrementButton.setText("+");
                        incrementButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (value[0] < max) {
                                    value[0]++;
                                    numberView.setText("" + value[0]);
                                }
                            }
                        });

                        container.addView(decrementButton);
                        container.addView(numberView);
                        container.addView(incrementButton);

                        ((RelativeLayout) item).addView(title);
                        ((RelativeLayout) item).addView(container);
                        break;
                    case "dropdown":
                        title = new TextView(view.getContext());
                        title.setText(formItem.getName() + ": ");
                        title.setTextSize(18);
                        title.setTextColor(item.getResources().getColor(R.color.black));
                        title.setLayoutParams(leftParams);

                        List<String> choices = new ArrayList<>();
                        for (int i = 0; i < formItem.getOptions().size(); i++) {
                            choices.add(formItem.getOptions().get(i));
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, choices);

                        Spinner spinner = new Spinner(view.getContext());
                        spinner.setAdapter(spinnerArrayAdapter);
                        spinner.setLayoutParams(rightParams);

                        ((RelativeLayout) item).addView(title);
                        ((RelativeLayout) item).addView(spinner);
                        break;
                }
            }


            if (item != null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Resources resources = item.getContext().getResources();
                params.bottomMargin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8,
                        resources.getDisplayMetrics()
                );
                item.setLayoutParams(params);
                view.addView(item);
            }
        }
    }

    public void submitForm() {
        JSONArray data = new JSONArray();

        LinearLayout form = (LinearLayout) getView().findViewById(R.id.matchscout_form);
        for(int i = 0; i < form.getChildCount(); i++) {
            JSONObject dataObject = new JSONObject();

            View item = form.getChildAt(i);
            if(item instanceof RelativeLayout) {
                for(int j = 0; j < ((RelativeLayout) item).getChildCount(); j++) {
                    View view = ((RelativeLayout) item).getChildAt(j);
                    try {
                        if(view instanceof EditText) { // Text
                            dataObject.put("name", view.getTag().toString());
                            dataObject.put("value", ((EditText) view).getText().toString());
                        } else if (view instanceof CheckBox) {// Check
                            dataObject.put("name", view.getTag().toString());
                            dataObject.put("value", ((CheckBox) view).isChecked());
                        } else if (view instanceof RadioGroup) { // Radio
                            dataObject.put("name", view.getTag().toString());
                            RadioButton selectedButton = (RadioButton) ((RadioGroup) view).getChildAt(((RadioGroup) view).indexOfChild(view.findViewById(((RadioGroup) view).getCheckedRadioButtonId())));
                            if(selectedButton != null) {
                                dataObject.put("value", selectedButton.getText().toString());
                            } else {
                                dataObject.put("value", "");
                            }
                        } else if (view instanceof LinearLayout) { // Number
                            for (int k = 0; k < ((LinearLayout) view).getChildCount(); k++) {
                                if (((LinearLayout) view).getChildAt(k) instanceof EditText) {
                                    dataObject.put("name", view.getTag().toString());
                                    dataObject.put("value", ((EditText) ((LinearLayout) view).getChildAt(k)).getText().toString());
                                }
                            }
                        } else if (view instanceof Spinner) { // Dropdown
                            dataObject.put("name", view.getTag().toString());
                            dataObject.put("value", ((Spinner) view).getSelectedItem().toString());
                        } else {
                            //Something screwy
                        }

                        data.put(dataObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            } else if (item instanceof TextView) {
                dataObject.put("name", item.getTag().toString());
        }

        Map<String, String> params = new HashMap<>();

        //TODO: Fix this mess
        //Because sending json over http is weird, this code must continue the weird trend
        for(int i = 0; i < data.length(); i++) {
            try {
                JSONObject dataObj = data.getJSONObject(i);
                if(dataObj.has("name")) {
                    params.put("data[" + i + "][name]", dataObj.getString("name"));
                }
                if(dataObj.has("value")) {
                    params.put("data[" + i + "][value]", dataObj.getString("value"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

        }
        System.out.println(getArguments().getInt("team"));
        params.put("team", String.valueOf(getArguments().getInt("team")));
        params.put("context", "match");
        params.put("match", String.valueOf(getArguments().getInt("match")));

        CookieRequest submissionRequest = new CookieRequest(Request.Method.POST,
                "/submitReport",
                params,
                preferences,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")) {
                    Toast.makeText(getContext(), "Report Submitted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error submitting report. Make sure you have filled out every item.", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "An error has occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(submissionRequest);
    }
}
