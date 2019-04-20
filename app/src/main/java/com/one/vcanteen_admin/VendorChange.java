package com.one.vcanteen_admin;

import com.google.gson.annotations.SerializedName;

public class VendorChange {

    @SerializedName("vendorId")
    private int vendorId;

    @SerializedName("adminPermission")
    private String adminPermission;

    public VendorChange(int vendorId, String adminPermission) {
        this.vendorId = vendorId;
        this.adminPermission = adminPermission;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(String adminPermission) {
        this.adminPermission = adminPermission;
    }

    @Override
    public String toString() {
        return "VendorChange{" +
                "vendorId=" + vendorId +
                ", adminPermission='" + adminPermission + '\'' +
                '}';
    }

    /*@SerializedName("vendorStatus")
    private String vendorStatus;

    public VendorChange(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }

    public String getVendorStatus() {
        return vendorStatus;
    }

    public void setVendorStatus(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }*/
}
