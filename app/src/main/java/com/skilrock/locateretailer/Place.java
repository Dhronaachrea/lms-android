package com.skilrock.locateretailer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.skilrock.config.DummyJson;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.communication.Communication;
import com.skilrock.lms.communication.JSONParser;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Place extends PMSWebTask {
    private String message;
    private String stateCode;
    private Map<String, String> states;
    private Map<String, String> cities;
    private Context context;
    private Selection selection;

    Place(Context context, String message, Selection selection) {
        this(context, null, message, selection);
    }

    Place(Context context, String stateCode, String message, Selection selection) {
        super(context, message);
        this.context = context;
        this.message = message;
        this.stateCode = stateCode;
        this.selection = selection;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        JSONObject stateObject,cityObject;
        try {

            if (stateCode == null) {
                if(GlobalVariables.connectivityExists(context) && GlobalVariables.loadDummyData) {
                    stateObject = JSONParser.parse(DummyJson.dummyStateString);
                } else{
                    stateObject   = Communication.getStateList();
                }
                return states = getStatesCities(stateObject, "stateData", "stateName", "stateCode");
            } else {
                if(GlobalVariables.connectivityExists(context) && GlobalVariables.loadDummyData) {
                    cityObject = JSONParser.parse(DummyJson.dummyCityList);
                } else{
                    cityObject   = Communication.getCityList(stateCode);
                }

                return cities = getStatesCities(cityObject, "cityData", "cityName", "cityCode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object result) {
        if (stateCode == null)
            openDialog(getKeys((Map<String, String>) result), "States", false);
        else
            openDialog(getKeys((Map<String, String>) result), "Cities", true);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
    }

    // data[0] = arrayName, data[1] = city/state name, data[2] = city/state code this is belongs to json
    private Map<String, String> getStatesCities(JSONObject jsonObject, String... data) throws JSONException {
        Map<String, String> map = new HashMap<>();
        JSONArray stateData = jsonObject.getJSONArray(data[0]);
        for (int i = 0; i < stateData.length(); i++) {
            JSONObject object = stateData.getJSONObject(i);
            map.put(object.getString(data[1]), object.getString(data[2]));
        }
        return map;
    }

    private void openDialog(CharSequence[] options, String title, boolean isCity) {
        new StateCityDialog(context, options, isCity).show();
    }

    private CharSequence[] getKeys(Map<String, String> map) {
        CharSequence[] keys = new CharSequence[map.size()];
        Set<Map.Entry<String, String>> set = map.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : set) {
            keys[i] = entry.getKey();
            i++;
        }
        return keys;
    }

    private class StateCityDialog extends android.app.AlertDialog {
        private Context context;
        private ListView contactList;
        private ArrayAdapter<CharSequence> betAdapater;
        private CharSequence[] stringss;
        private ImageView done, close;
        private CustomTextView headerDialog;
        private boolean isCity;
        private String selectedCity;
        private String selectedCityCode;
        private String selectedState;
        private String selectedStateCode;

        public StateCityDialog(Context context, CharSequence[] stringss, boolean isCity) {
            super(context, R.style.DialogZoomAnim);
            this.context = context;
            this.stringss = stringss;
            this.isCity = isCity;
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        }

        public StateCityDialog(CharSequence[] stringss, Context context) {
            super(context, R.style.DialogZoomAnim);
            this.context = context;
            this.stringss = stringss;
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void show() {
            super.show();
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            final View view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bet_dialog, null);
            View headerView = view.findViewById(R.id.header_id);
            done = (ImageView) headerView.findViewById(R.id.done);
            close = (ImageView) headerView.findViewById(R.id.close);
            headerDialog = (CustomTextView) headerView.findViewById(R.id.header_name);
            close.setVisibility(View.INVISIBLE);
            done.setImageResource(R.drawable.close);

//            (findViewById(R.id.close)).setVisibility(View.INVISIBLE);
//            ((ImageView)(findViewById(R.id.done))).setImageResource(R.drawable.close);

            if (isCity)
                headerDialog.setText("Cities");
            else
                headerDialog.setText("States");
            done.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            contactList = (ListView) view.findViewById(R.id.contact_list);
            betAdapater = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_expandable_list_item_1, stringss);
            contactList.setAdapter(betAdapater);
            contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (isCity) {
                        selectedCity = stringss[position].toString();
                        selectedCityCode = cities.get(selectedCity);
                        selection.onCitySelected(selectedCityCode, selectedCity);
                    } else {
                        selectedState = stringss[position].toString();
                        selectedStateCode = states.get(selectedState);
                        selection.onStateSelected(selectedStateCode, selectedState);

                    }
                    cancel();
                }
            });
            this.setContentView(view);
        }

    }

    public interface Selection {
        public void onStateSelected(String stateCode, String stateName);
        public void onCitySelected(String cityCode, String cityName);
    }

}