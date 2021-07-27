package com.annotation.drinking_zone.Utility;

public class AddressesData {

//    private  int id;
    private String id;
    private String name;
    private String line1;
    private String line2;
    private String landmark;
    private String mobile;
    private String pincode;
    private String addressType;
    private String state;
    private String state_id;
    private String district;
    private String district_id;
    private String status;

    public AddressesData(String id, String name, String line1, String line2, String landmark, String mobile, String pincode, String addressType, String state, String state_id, String district, String district_id, String status) {
        this.id = id;
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.landmark = landmark;
        this.mobile = mobile;
        this.pincode = pincode;
        this.addressType = addressType;
        this.state = state;
        this.state_id = state_id;
        this.district = district;
        this.district_id = district_id;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
