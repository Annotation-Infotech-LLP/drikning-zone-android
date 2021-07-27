package com.annotation.drinking_zone.HomeDashboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.AdapterLayouts.AddressesRecyclerViewAdapter;
import com.annotation.drinking_zone.AddressRelated.EditAddressActivity;
import com.annotation.drinking_zone.ChangeCurrentPassword.ChangePassword;
import com.annotation.drinking_zone.Login.SignUpActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AddressesData;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements AsyncTaskListener, View.OnClickListener, AdapterView.OnItemSelectedListener, RecyclerViewClickHandler {

    View view;
    TextInputLayout fullname, email, contact, address_contact, address_name, address_line1, address_line2, address_pincode, address_landmark;
    String text_fullname, text_email, text_contact, text_firstname_saved, text_lastname_saved;
    Button  add_address_btn;
    TextView reset_password_current, district_info;
    RadioGroup address_type_selector;
    RadioButton type_home, type_office;
    ImageView add_address, edit_name_btn;
    LinearLayout address_form, addresses_list_layout, no_address_layout;
    RecyclerView addresses_list;
    Spinner address_state, address_district;
    UserSharedPrefDetails sharedPrefDetails;
    ArrayList<String> state_names;
    ArrayList<String> state_names2;
    ArrayList<String> district_names;
    ArrayList<Integer> state_ids;
    ArrayList<Integer> district_ids;
    ArrayList<Integer> state_ids2;
    int state_id, district_id, userId, address_default_id;
    int selectedRadioButtonID;
    String selectedRadioButtonText;
    RadioButton selectedRadioButton;
    private GridLayoutManager gridLayoutManager;
    private AddressesRecyclerViewAdapter addressesRecyclerViewAdapter;
    private List<AddressesData> addressesDataList;
    Dialog alertDialog;
    TextInputLayout et_first_name, et_last_name;
    Button update_profile_btn;
    String first_name_txt, last_name_txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        fullname = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.text_input_email);
        contact = view.findViewById(R.id.text_input_contact_number);
        address_contact = view.findViewById(R.id.address_contact_number);
        address_name = view.findViewById(R.id.address_name);
        address_line1 = view.findViewById(R.id.address_line1);
        address_line2 = view.findViewById(R.id.address_line2);
        address_pincode = view.findViewById(R.id.address_pincode);
        address_landmark = view.findViewById(R.id.address_landmark);
        address_district = view.findViewById(R.id.address_district);
        address_state = view.findViewById(R.id.address_state);
        reset_password_current = view.findViewById(R.id.through_pass);
        add_address_btn = view.findViewById(R.id.add_address_btn);
        add_address = view.findViewById(R.id.add_address);
        address_form = view.findViewById(R.id.layout5);
        address_type_selector = view.findViewById(R.id.address_type_selector);
        type_home = view.findViewById(R.id.address_type_home);
        type_office = view.findViewById(R.id.address_type_office);
        district_info = view.findViewById(R.id.district_text);
        addresses_list_layout = view.findViewById(R.id.addresses_list_layout);
        addresses_list = view.findViewById(R.id.addresses_list);
        no_address_layout = view.findViewById(R.id.no_address_layout);
        edit_name_btn = view.findViewById(R.id.edit_name_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        address_default_id = 0;

        sharedPrefDetails = new UserSharedPrefDetails(view.getContext());
        text_fullname = sharedPrefDetails.getUser_name();
        text_contact = sharedPrefDetails.getUser_contact();
        text_email = sharedPrefDetails.getUser_email();
        userId = sharedPrefDetails.getUser_Id();
        text_firstname_saved = sharedPrefDetails.getUser_firstname();
        text_lastname_saved = sharedPrefDetails.getUser_lastname();

        fullname.getEditText().setText(text_fullname);
        email.getEditText().setText(text_email);
        contact.getEditText().setText(text_contact);
        fullname.setEnabled(false);
        email.setEnabled(false);
        contact.setEnabled(false);
        address_form.setVisibility(View.GONE);

        alertDialog = new Dialog(getActivity());
        alertDialog.setContentView(R.layout.update_name_layout);
        et_first_name = alertDialog.findViewById(R.id.text_input_firstname);
        et_last_name = alertDialog.findViewById(R.id.text_input_lastname);
        update_profile_btn = alertDialog.findViewById(R.id.update_profile_btn);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        reset_password_current.setPaintFlags(reset_password_current.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        state_names = new ArrayList<>();
        state_names2 = new ArrayList<>();
        district_names = new ArrayList<>();
        state_ids = new ArrayList<>();
        state_ids2 = new ArrayList<>();
        district_ids = new ArrayList<>();
        addressesDataList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        addresses_list.setLayoutManager(gridLayoutManager);

        reset_password_current.setOnClickListener(this);
        add_address.setOnClickListener(this);
        add_address_btn.setOnClickListener(this);
        edit_name_btn.setOnClickListener(this);
        update_profile_btn.setOnClickListener(this);

        address_state.setOnItemSelectedListener(this);
        address_district.setOnItemSelectedListener(this);



        String type = "get_address";
        BackgroundWorker getAddresses= new BackgroundWorker(getActivity());
        getAddresses.setAsyncTaskListener(this);
        getAddresses.execute(type, Integer.toString(userId), Integer.toString(address_default_id));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("##tag", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    Log.i("##confirmation", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateResult(String result) {
        Log.i("#Result All states", "updateResult: " + result);
//                Parsing JSON DATA
        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
                    } else if (jsonResult.has("data")) {
                        addresses_list_layout.setVisibility(View.GONE);
                        no_address_layout.setVisibility(View.VISIBLE);
                    } else {
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something Went Wrong, Please try again later", "OK", "INFORMATION SAYS:", this);
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
                                address_state.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, state_names2));
                            } else if (!(state_names.contains(state_names2)) && !(district_names.contains(state_names))) {
                                district_names.addAll(state_names);
                                district_ids.addAll(state_ids);
                                address_district.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, district_names));
                            }
                        } else {
                            new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "failed to load states, Please try again later", "OK", "INFORMATION SAYS:", HomeActivity.class, "Profile");
                        }
                    } else if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        if(message.contains("profile") || message.contains("Profile") || message.contains("Updated") || message.equals("updated"))
                        {
                            sharedPrefDetails.setUser_firstname(first_name_txt);
                            sharedPrefDetails.setUser_lastname(last_name_txt);
                            sharedPrefDetails.setUser_name(first_name_txt+" "+last_name_txt);
                        }
                        new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class, "Profile");
                    } else if (jsonResult.has("data")) {
                        JSONArray addressesList = jsonResult.getJSONArray("data");
                        if (addressesList.length() != 0) {
                            addresses_list_layout.setVisibility(View.VISIBLE);
                            no_address_layout.setVisibility(View.GONE);
                            for (int i = 0; i < addressesList.length(); i++) {
                                JSONObject single_address = addressesList.getJSONObject(i);
                                String addressId = single_address.getString("Id");
                                String addressholder_name = single_address.getString("Name");
                                String address_line1 = single_address.getString("Line1");
                                String address_line2 = single_address.getString("Line2");
                                String address_landmark = single_address.getString("Landmark");
                                String address_mobile = single_address.getString("Mobile");
                                String address_pincode = single_address.getString("Pin");
                                String address_type = single_address.getString("AddressType");
                                String address_state = single_address.getString("State");
                                String address_stateId = single_address.getString("StateId");
                                String address_district = single_address.getString("District");
                                String address_districtId = single_address.getString("DistrictId");
                                String address_status = single_address.getString("Status");

                                AddressesData addressesData = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
                                addressesDataList.add(addressesData);
                            }
                            addressesRecyclerViewAdapter = new AddressesRecyclerViewAdapter(view.getContext(), addressesDataList, this);
                            addresses_list.setAdapter(addressesRecyclerViewAdapter);
                            addressesRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            addresses_list_layout.setVisibility(View.GONE);
                            new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Address is Not added yet", "OK", "INFORMATION SAYS:", HomeActivity.class, "Profile");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (add_address.equals(v)) {
            String type = "get_states";
            address_form.setVisibility(View.VISIBLE);
            if(address_form.isShown())
            {
                address_contact.getEditText().setText(text_contact);
                address_name.getEditText().setText(text_fullname);
            }
            BackgroundWorker loadStates = new BackgroundWorker(getActivity());
            loadStates.setAsyncTaskListener(this);
            loadStates.execute(type);
        }
        else if (reset_password_current.equals(v)) {
            Intent intent = new Intent(view.getContext(), ChangePassword.class);
            view.getContext().startActivity(intent);
        }
        else if(add_address_btn.equals(v))
        {
            boolean validation_rtrnValue = confirmInput();
            int address_id, stateId, districtId;
            String mobile, name, line1, line2, landmark, pincode, addressType;
            address_id =0;
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
                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {
                    selectedRadioButton = (RadioButton) view.findViewById(selectedRadioButtonID);
                    selectedRadioButtonText = selectedRadioButton.getText().toString();

                    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                    backgroundWorker.setAsyncTaskListener(this);
                    backgroundWorker.execute(type,Integer.toString(address_id), Integer.toString(userId), mobile, name, line1, line2, landmark, pincode, selectedRadioButtonText, Integer.toString(stateId), Integer.toString(districtId));
                }
                else{
//                    selectedRadioButton.setError("Please select the refill type");
                    new CustomDialogs(getActivity(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the Delivery Type", "RETRY AGAIN", "INFORMATION SAYS:");
                }
            }
        }
        else if(v.equals(edit_name_btn))
        {
            et_first_name.getEditText().setText(text_firstname_saved);
            et_last_name.getEditText().setText(text_lastname_saved);
            alertDialog.show();
        }
        else if(v.equals(update_profile_btn))
        {

            if(validateName())
            {

                first_name_txt = et_first_name.getEditText().getText().toString();
                last_name_txt = et_last_name.getEditText().getText().toString();


                String update_type = "update_profile";
                BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                backgroundWorker.setAsyncTaskListener(this);
                backgroundWorker.execute(update_type, String.valueOf(userId), first_name_txt, last_name_txt);

                if(alertDialog.isShowing())
                {
                    alertDialog.dismiss();
                }
            }


        }
    }

    private boolean validateName()
    {
        String firstName=et_first_name.getEditText().getText().toString().trim();
        String lastName=et_last_name.getEditText().getText().toString().trim();
        if(firstName.isEmpty() || lastName.isEmpty())
        {
            et_first_name.setError("Field can't be empty");
            et_last_name.setError("Field can't be empty");
            return false;
        }
        else if(!firstName.matches("^[A-Za-z]+$"))
        {
//            ^           beginning of the string,
//          [A-Za-z]    search for alphabetical chars either they are CAPITALS or not
//            +           string contains at least one alphabetical char
//            $           end of the string

            et_first_name.setError("Name should be of only characters");
            return false;
        }
        else if(!lastName.matches("^[A-Za-z]+$"))
        {
            et_last_name.setError("Name should be of only characters");
            return false;
        }
        else
        {
            et_first_name.setError(null);
            et_last_name.setError(null);
            return  true;
        }
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
        value = validateFields();
        Log.i("#returnValue", String.valueOf(value));
        return value;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if (parent.equals(address_state)) {
            int state_index = 0;
            String state=   parent.getItemAtPosition(parent.getSelectedItemPosition()).toString();
            if(!state.equals(""))
            {
                    if(state_names2.contains(state))
                    {
                        state_index = state_names2.indexOf(state);
                        Log.i("state_idSel", "onItemSelected: "+state_index);
                        state_id = state_ids2.get(state_index);
                        Log.i("state_id", "onItemSelected: "+state_id);
//                        Toast.makeText(getActivity().getApplicationContext(), "StateId "+state+" DistrictName "+state_id, Toast.LENGTH_SHORT).show();
                        String type = "get_districts";
                        BackgroundWorker loadDistricts = new BackgroundWorker(getActivity());
                        loadDistricts.setAsyncTaskListener(this);
                        loadDistricts.execute(type, Integer.toString(state_id));
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Select the state so as to load Districts", Toast.LENGTH_SHORT).show();
                    }
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Please Select the state so as to load Districts", Toast.LENGTH_SHORT).show();
            }

        }
        else if(parent.equals(address_district))
        {
            int district_index = 0;
            String district=   parent.getItemAtPosition(parent.getSelectedItemPosition()).toString();
//            Toast.makeText(getActivity().getApplicationContext(),district,Toast.LENGTH_LONG).show();
            if(!district.equals(""))
            {
                    if(district_names.contains(district))
                    {
                        district_index = district_names.indexOf(district);
                        Log.i("district_idSel", "onItemSelected: "+district_index);
                        district_id = district_ids.get(district_index);
                        Toast.makeText(getActivity().getApplicationContext(), "DistrictId "+district_id+" DistrictName "+district, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Select the District", Toast.LENGTH_SHORT).show();
                    }
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Please Select the District", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDeleteClicked(int position) {
//        Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), EditAddressActivity.class);
                intent.putExtra("addressId", Integer.toString(position));
                startActivity(intent);
    }

    @Override
    public void onQuantityUpdated(int position, String quantity) {

    }
}
