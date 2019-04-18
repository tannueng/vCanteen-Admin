package com.one.vcanteen_admin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("v2/admin-main/main")
    Call<VendorList> getAllVendor();
}
