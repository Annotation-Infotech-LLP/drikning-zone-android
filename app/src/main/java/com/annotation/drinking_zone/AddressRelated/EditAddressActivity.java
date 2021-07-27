package com.annotation.drinking_zone.AddressRelated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditAddressActivity extends AppCompatActivity implements AsyncTaskListener, View.OnClickListener, AdapterView.OnItemSelectedListener  {

    TextInputLayout address_contact, address_name, address_line1, address_line2, address_pincode, address_landmark;
    Button update_address_btn, delete_address_btn;
    RadioGroup address_type_selector;
    RadioButton type_home, type_office;
    Spinner address_state, address_district;
    UserSharedPrefDetails sharedPrefDetails;
    ArrayList<String> state_names;
    ArrayList<String> state_names2;
    ArrayList<String> district_names;
    ArrayList<Integer> state_ids;
    ArrayList<Integer> district_ids;
    ArrayList<Integer> state_ids2;
    int state_id, district_id, userId;
    String address_default_id;
    int selectedRadioButtonID;
    String selectedRadioButtonText;
    RadioButton selectedRadioButton;
    String addressId, addressholder_name, add_line1, add_line2, add_landmark, address_mobile,add_pincode, address_type, add_state, address_stateId, add_district, address_districtId, address_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        address_contact = findViewById(R.id.address_contact_number);
        address_name = findViewById(R.id.address_name);
        address_line1 = findViewById(R.id.address_line1);
        address_line2 = findViewById(R.id.address_line2);
        address_pincode = findViewById(R.id.address_pincode);
        address_landmark = findViewById(R.id.address_landmark);
        address_district = findViewById(R.id.address_district);
        address_state = findViewById(R.id.address_state);
        update_address_btn = findViewById(R.id.update_address_btn);
        address_type_selector = findViewById(R.id.address_type_selector);
        type_home = findViewById(R.id.address_type_home);
        type_office = findViewById(R.id.address_type_office);
        delete_address_btn = findViewById(R.id.delete_address_btn);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        address_default_id = b.getString("addressId");

        sharedPrefDetails = new UserSharedPrefDetails(this);
        userId = sharedPrefDetails.getUser_Id();

        state_names = new ArrayList<>();
        state_names2 = new ArrayList<>();
        district_names = new ArrayList<>();
        state_ids = new ArrayList<>();
        state_ids2 = new ArrayList<>();
        district_ids = new ArrayList<>();


        String statesType = "get_states";
        BackgroundWorker loadStates = new BackgroundWorker(this);
        loadStates.setAsyncTaskListener(this);
        loadStates.execute(statesType);


        String type = "get_address";
        BackgroundWorker getAddresses= new BackgroundWorker(this);
        getAddresses.setAsyncTaskListener(this);
        getAddresses.execute(type, Integer.toString(userId), address_default_id);

        address_state.setOnItemSelectedListener(this);
        address_district.setOnItemSelectedListener(this);

        update_address_btn.setOnClickListener(this);
        delete_address_btn.setOnClickListener(this);

    }

    @Override
    public void updateResult(String result) {
        Log.i("#Result All states", "updateResult: " + result);

        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                boolean success = jsonResult.getBoolean("status");
                if (!success) {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
                    } else {
                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something Went Wrong, Please try again later", "OK", "INFORMATION SAYS:", HomeActivity.class);
                    }
                } else if (success) {
                    if (jsonResult.has("msg") && jsonResult.has("data")) {
                        JSONArray stateList = jsonResult.getJSONArray("data");
                        if (stateList.length() != 0) {

                            state_names.clear();
                            state_ids.clear();
                            district_names.clear();
                            district_ids.clear();
                            for (int i = 0; i < stateList.length(); i++) {
                                JSONObject single_state = stateList.getJSONObject(i);
                                int state_id = single_state.getInt("Id");
                                String state_name = single_state.getString("Name");
                                state_names.add(state_name);
                                state_ids.add(state_id);
                            }

                            if (state_names2.isEmpty()) {
                                state_names2.addAll(state_names);
                                state_ids2.addAll(state_ids);
                                address_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, state_names2));

                            } else if (!(state_names.contains(state_names2)) && !(district_names.contains(state_names))) {
                                district_names.addAll(state_names);
                                district_ids.addAll(state_ids);
                                address_district.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, district_names));

                                ArrayAdapter<String> spinnerAdap = (ArrayAdapter<String>) address_district.getAdapter();
                                int spinnerPosition = district_names.indexOf(add_district);
                                if (spinnerPosition != 0) {
                                    address_district.setSelection(spinnerPosition);
                                } else {
                                    address_district.setSelection(0);
                                }
                            }
                        } else {
                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "failed to load states, Please try again later", "OK", "INFORMATION SAYS:", EditAddressActivity.class);
                        }
                    } else if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
                    } else if (jsonResult.has("data")) {
                        JSONArray addressesList = jsonResult.getJSONArray("data");
                        if (addressesList.length() != 0) {
                            for (int i = 0; i < addressesList.length(); i++) {
                                JSONObject single_address = addressesList.getJSONObject(i);
                                addressId = single_address.getString("Id");
                                addressholder_name = single_address.getString("Name");
                                add_line1 = single_address.getString("Line1");
                                add_line2 = single_address.getString("Line2");
                                add_landmark = single_address.getString("Landmark");
                                address_mobile = single_address.getString("Mobile");
                                add_pincode = single_address.getString("Pin");
                                address_type = single_address.getString("AddressType");
                                add_state = single_address.getString("State");
                                address_stateId = single_address.getString("StateId");
                                add_district = single_address.getString("District");
                                address_districtId = single_address.getString("DistrictId");
                                address_status = single_address.getString("Status");

                                address_name.getEditText().setText(addressholder_name);
                                address_line1.getEditText().setText(add_line1);
                                address_line2.getEditText().setText(add_line2);
                                address_landmark.getEditText().setText(add_landmark);
                                address_contact.getEditText().setText(address_mobile);
                                address_pincode.getEditText().setText(add_pincode);

                                selectedRadioButtonID = address_type_selector.getCheckedRadioButtonId();
                                if (selectedRadioButtonID == -1) {
                                    if (address_type.equals(type_home.getText().toString())) {
                                        type_home.setChecked(true);
                                    } else if (address_type.equals(type_office.getText().toString())) {
                                        type_office.setChecked(true);
                                    }
                                }

                                if (state_names2.contains(add_state)) {
                                    ArrayAdapter<String> spinnerAdap = (ArrayAdapter<String>) address_state.getAdapter();
                                    int spinnerPosition = state_names2.indexOf(add_state);
                                    address_state.setSelection(spinnerPosition);
                                }
                            }
                        } else {
                            new CustomDialogs(this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Address is Not added yet", "OK", "INFORMATION SAYS:", EditAddressActivity.class);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        if(update_address_btn.equals(v))
        {
            boolean validation_rtrnValue = confirmInput();
            int stateId, districtId;
            String mobile, name, line1, line2, landmark, pincode, addressType;
            String type = "update_address";
            mobile = address_contact.getEditText().getText().toString();
            name = address_name.getEditText().getText().toString();
            line1 = address_line1.getEditText().getText().toString();
            line2 = address_line2.getEditText().getText().toString();
            landmark = address_landmark.getEditText().getText().toString();
            pincode = address_pincode.getEditText().getText().toString();
            stateId = state_id;
            districtId = district_id;

            if(validation_rtrnValue)
            {
                selectedRadioButtonID = address_type_selector.getCheckedRadioButtonId();
                Log.i("#addressClick", "onClick: "+selectedRadioButtonID);
                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {
                    selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    selectedRadioButtonText = selectedRadioButton.getText().toString();
                    Log.i("#addBtn", "onClick: "+selectedRadioButton);
                    Log.i("#addbtnTex", "onClick: "+selectedRadioButtonText);

                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.setAsyncTaskListener(this);
                    backgroundWorker.execute(type,address_default_id, Integer.toString(userId), mobile, name, line1, line2, landmark, pincode, selectedRadioButtonText, Integer.toString(stateId), Integer.toString(districtId));
                }
                else{
//                    selectedRadioButton.setError("Please select the refill type");
                    new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the Delivery Type", "RETRY AGAIN", "INFORMATION SAYS:");
                }
            }
        }
        else if(delete_address_btn.equals(v))
        {
            String type = "delete_address";

            BackgroundWorker deleteAddress = new BackgroundWorker(this);
            deleteAddress.setAsyncTaskListener(this);
            deleteAddress.execute(type, Integer.toString(userId), address_default_id);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.equals(address_state)) {
            int state_index;
            String state=   parent.getItemAtPosition(parent.getSelectedItemPosition()).toString();
            if(!state.equals(""))
            {
                if(state_names2.contains(state))
                {
                    state_index = state_names2.indexOf(state);
                    state_id = state_ids2.get(state_index);
                    String type = "get_districts";

                    BackgroundWorker loadDistricts = new BackgroundWorker(this);
                    loadDistricts.setAsyncTaskListener(this);
                    loadDistricts.execute(type, Integer.toString(state_id));
                }
                else
                {
                    Toast.makeText(this, "Please Select the state so as to load Districts", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Please Select the state so as to load Districts", Toast.LENGTH_SHORT).show();
            }

        }
        else if(parent.equals(address_district))
        {
            int district_index = 0;
            String district=   parent.getItemAtPosition(parent.getSelectedItemPosition()).toString();
            if(!district.equals(""))
            {
                if(district_names.contains(district))
                {
                    district_index = district_names.indexOf(district);
                    district_id = district_ids.get(district_index);
                }
                else
                {
                    Toast.makeText(this, "Please Select the District", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Please Select the District", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private boolean validateFields()
    {
        String name=address_name.getEditText().getText().toString().trim();
        String line1=address_line1.getEditText().getText().toString().trim();
        String line2=address_line2.getEditText().getText().toString().trim();
        String landmark=address_landmark.getEditText().getText().toString().trim();
        String pincode=address_pincode.getEditText().getText().toString().trim();
        String contact_number= address_contact.getEditText().getText().toString().trim();

        if(contact_number.isEmpty())
        {
            address_contact.setError("Field can't be empty");
            return  false;
        }
        else if(!Patterns.PHONE.matcher(contact_number).matches())
        {
            address_contact.setError("Please enter a valid contact number");
            return false;
        }
        else if(contact_number.contains(" "))
        {
            address_contact.setError("There cant be space in contact");
            return false;
        }
        else if(name.isEmpty())
        {
            address_name.setError("Field can't be empty");
            return false;
        }
        else if(line1.isEmpty())
        {
            address_line1.setError("Field can't be empty");
            return false;
        }
        else if(line2.isEmpty())
        {
            address_line2.setError("Field can't be empty");
            return false;
        }
        else if(landmark.isEmpty())
        {
            address_landmark.setError("Field can't be empty");
            return false;
        }
        else if(!name.matches("^[A-Za-z ]+$") )
        {
//            ^           beginning of the string,
//          [A-Za-z]    search for alphabetical chars either they are CAPITALS or not
//            +           string contains at least one alphabetical char
//            $           end of the string

            address_name.setError("Name should be of only characters");
            return false;
        }
        else if(!pincode.matches("[0-9]{6}"))
        {
            address_pincode.setError("Please Enter a valid Pincode");
            return false;
        }
        else
        {
            address_landmark.setError(null);
            address_line2.setError(null);
            address_line1.setError(null);
            address_pincode.setError(null);
            address_contact.setError(null);
            address_name.setError(null);
            return  true;
        }
    }

    public boolean confirmInput()
    {
        boolean value = true;
        if(!validateFields())
        {
            value = false;
        }else
        {
            value = true;
        }
        Log.i("#returnValue", String.valueOf(value));
        return value;
    }

}
