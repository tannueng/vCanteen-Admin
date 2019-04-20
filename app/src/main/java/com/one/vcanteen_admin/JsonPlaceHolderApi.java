package com.one.vcanteen_admin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface JsonPlaceHolderApi {

    @GET("v2/admin-main/main")
    Call<VendorList> getAllVendor();

    /*@FormUrlEncoded
    @PUT("v2/admin-main/vendor/permission")     //receive Vendor Status Back
    Call<String> setVendorPermission(@Field("vendorId") int vendorId,
                                     @Field("adminPermission") String adminPermission);*/


    @PUT("v2/admin-main/vendor/permission")     //receive Vendor Status Back
    Call<List<ChangePermissionResponse>> setVendorPermission(@Body VendorChange vendorChange);


    @POST("v2/user-authentication/admin/signin")
    Call<LoginResponse> sendLogin(@Body LoginAdmin loginAdmin);

    @POST("v2/user-authentication/admin/verify/token")
    Call<TokenVerification> verifyToken(@Body Token token);

    @FormUrlEncoded
    @PUT("v1/settings/vendor/orders/cancel-all")
    Call<Void> cancelAllOrder(@Field("vendorId") int vendorId,
                              @Field("cancelReason") String cancelReason);
}
