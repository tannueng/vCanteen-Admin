package com.one.vcanteen_admin;

public class Vendor {

    String vendorStatus;
    int vendorId;
    String vendorImage;
    String restaurantName;
    String vendorEmail;
    String vendorPhoneNumber;
    String vendorAccountType;
    String vendorAdminPermission;







    public String getVendorStatus() {
        return vendorStatus;
    }

    public void setVendorStatus(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public String getVendorAccountType() {
        return vendorAccountType;
    }

    public void setVendorAccountType(String vendorAccountType) {
        this.vendorAccountType = vendorAccountType;
    }

    public String getVendorAdminPermission() {
        return vendorAdminPermission;
    }

    public void setVendorAdminPermission(String vendorAdminPermission) {
        this.vendorAdminPermission = vendorAdminPermission;
    }

    public Vendor(String vendorStatus, int vendorId, String vendorImage, String restaurantName, String vendorEmail, String vendorPhoneNumber, String vendorAccountType, String vendorAdminPermission) {
        this.vendorStatus = vendorStatus;
        this.vendorId = vendorId;
        this.vendorImage = vendorImage;
        this.restaurantName = restaurantName;
        this.vendorEmail = vendorEmail;
        this.vendorPhoneNumber = vendorPhoneNumber;
        this.vendorAccountType = vendorAccountType;
        this.vendorAdminPermission = vendorAdminPermission;
    }
}
