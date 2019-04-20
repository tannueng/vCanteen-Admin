package com.one.vcanteen_admin;

import com.google.gson.annotations.SerializedName;

public class ChangePermissionResponse {

    @SerializedName("vendorStatus")
    private String vendorStatus;

    public ChangePermissionResponse(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }

    public String getVendorStatus() {
        return vendorStatus;
    }

    public void setVendorStatus(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }
}
