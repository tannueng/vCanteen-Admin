package com.one.vcanteen_admin;

import java.util.List;

public class VendorList {

    List<Vendor> vendorList;

    public VendorList(List<Vendor> vendorList) {
        this.vendorList = vendorList;
    }

    public List<Vendor> getVendorList() {
        return vendorList;
    }

    public void setVendorList(List<Vendor> vendorList) {
        this.vendorList = vendorList;
    }
}
