package com.one.vcanteen_admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VendorListAdapter extends ArrayAdapter {

    ImageView statusLight;
    TextView vendorIdContainer;
    ImageView vendorProfileImage;
    TextView vendorName;
    TextView vendorEmail;
    TextView phoneNumber;
    TextView accountType;
    TextView permissionStatus;
    Button approveButton;
    Button rejectButton;

    Vendor singleVendor;
    private VendorList mVendorList;
    private List<Vendor> mVendorArrayList;
    View customView;

    RequestOptions option = new RequestOptions().centerCrop();


    String restaurantName;


    VendorListAdapter(Context context, VendorList List){
        super(context, R.layout.vendor_list_item, List.vendorList);
        mVendorList = List;
        mVendorArrayList = List.vendorList;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater orderInflater = LayoutInflater.from(getContext());
        customView = orderInflater.inflate(R.layout.vendor_list_item, parent, false);


        statusLight = customView.findViewById(R.id.statusLight);
        vendorIdContainer = customView.findViewById(R.id.vendorIdContainer);
        vendorProfileImage = customView.findViewById(R.id.vendorProfileImage);
        vendorName = customView.findViewById(R.id.vendorName);
        vendorEmail = customView.findViewById(R.id.vendorEmail);
        phoneNumber = customView.findViewById(R.id.phoneNumber);
        accountType = customView.findViewById(R.id.accountType);
        permissionStatus = customView.findViewById(R.id.permissionStatus);

        approveButton = customView.findViewById(R.id.approveButton);
        rejectButton = customView.findViewById(R.id.rejectButton);

        singleVendor = (Vendor) getItem(position);

        final int vendor_id = singleVendor.getVendorId();
        final String restaurantName = singleVendor.getRestaurantName();
        final String status = singleVendor.getVendorAdminPermission();


        if(status.equals("APPROVED")){
            approveButton.setEnabled(false);
            rejectButton.setEnabled(true);
            approveButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.VISIBLE);
        } else if(status.equals("REJECTED")){
            approveButton.setEnabled(true);
            rejectButton.setEnabled(false);
            approveButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.GONE);
        } else {
            approveButton.setEnabled(true);
            rejectButton.setEnabled(true);
            approveButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
        }


        //Putting everything into list item
        if(singleVendor != null){

            //Set the status light into 3 cases
            if (!singleVendor.getVendorAdminPermission().equals("APPROVED")){

                //if Vendor is not approve, light will gone
                statusLight.setVisibility(View.INVISIBLE);

            } else if(singleVendor.getVendorStatus().equals("OPEN")){

                //if Open, light is green
                statusLight.setVisibility(View.VISIBLE);
                statusLight.setImageResource(R.drawable.ic_vendor_status_open);

            } else if(singleVendor.getVendorStatus().equals("CLOSED")){

                //if close, light is red
                statusLight.setVisibility(View.VISIBLE);
                statusLight.setImageResource(R.drawable.ic_vendor_status_close);

            }


            //Parse vendorId from JSON into placeholder
            vendorIdContainer.setText(String.valueOf(vendor_id));

            //Parse vendor image URL into placeholder using Glide
            Glide.with(this.getContext()).load(singleVendor.getVendorImage()).apply(option).into(vendorProfileImage);

            if(singleVendor.getVendorImage() == null)
                Glide.with(this.getContext()).load(R.drawable.ic_vendor_default).apply(option).into(vendorProfileImage);

            vendorName.setText(singleVendor.getRestaurantName());
            vendorEmail.setText(singleVendor.getVendorEmail());
            phoneNumber.setText(singleVendor.getVendorPhoneNumber());
            accountType.setText(singleVendor.getVendorAccountType());


            String vPermission = singleVendor.getVendorAdminPermission();
            if (vPermission.equals("APPROVED")){
                permissionStatus.setText(vPermission);
                permissionStatus.setTextColor(Color.parseColor("#FF9C9C"));

                //add check icon in front of this text
                Drawable img = getContext().getResources().getDrawable( R.drawable.ic_check );
                img.setBounds(0,0,4,0);
                permissionStatus.setCompoundDrawables(img, null, null, null);
            } else if(vPermission.equals("REJECTED")){
                permissionStatus.setText(vPermission);
                permissionStatus.setTextColor(Color.parseColor("#FF4141"));
            } else {
                permissionStatus.setText(vPermission); //PENDING
                permissionStatus.setTextColor(Color.parseColor("#F2C94C"));
            }

        }




        ////////////////////////////////////////Button Onclick///////////////////////////////////////

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.dialog_approve);

                final TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                final TextView content = (TextView) dialog.findViewById(R.id.dialogContent);
                Button negativeButton = (Button) dialog.findViewById(R.id.negativeButton);
                final Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);

                String contentText = content.getText().toString();
                if (singleVendor.getRestaurantName() != null){
                    String contentWithVendorName = contentText.replace("[vendorName]", "<b>"+restaurantName+"</b>");
                    content.setText(Html.fromHtml(contentWithVendorName));
                }



                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(), "Saving...",  Toast.LENGTH_SHORT).show();
                        positiveButton.setEnabled(false);

                        //deleteThisMenu();
                        changeVendorPermission(vendor_id,"APPROVED");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(), "Saved!",  Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), AdminMainPageActivity.class);
                                getContext().startActivity(intent);

                                positiveButton.setEnabled(true);
                                dialog.dismiss();
                            }
                        }, 2000);

                        //Toast.makeText(getApplicationContext(), "MENU DELETED!",  Toast.LENGTH_SHORT).show();


                    }
                });

                dialog.show();

            }
        });


        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.dialog_reject);

                final TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                final TextView content = (TextView) dialog.findViewById(R.id.dialogContent);
                Button negativeButton = (Button) dialog.findViewById(R.id.negativeButton);
                final Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);

                String contentText = content.getText().toString();
                if (singleVendor.getRestaurantName() != null){
                    String contentWithVendorName = contentText.replace("[vendorName]", "<b>"+restaurantName+"</b>");
                    content.setText(Html.fromHtml(contentWithVendorName));
                }



                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(), "Saving...",  Toast.LENGTH_SHORT).show();
                        positiveButton.setEnabled(false);

                        //deleteThisMenu();
                        changeVendorPermission(vendor_id,"REJECTED");



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(), "Saved!",  Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), AdminMainPageActivity.class);
                                getContext().startActivity(intent);

                                positiveButton.setEnabled(true);
                                dialog.dismiss();
                            }
                        }, 2000);

                        //Toast.makeText(getApplicationContext(), "MENU DELETED!",  Toast.LENGTH_SHORT).show();


                    }
                });

                dialog.show();

            }
        });



        return customView;
    }

    private void changeVendorPermission(final int vendor_id, final String permission) {

        String url="https://vcanteen.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        VendorChange vendorChange = new VendorChange(vendor_id,permission);
        System.out.println("==================== SENT Vendor change: "+vendorChange.toString()+" ==================");

        Call<List<ChangePermissionResponse>> call = jsonPlaceHolderApi.setVendorPermission(vendorChange); //SET LOGIC TO INSERT ID HERE
        System.out.println("==================== SENT Vendor id: "+vendor_id+"- Permission: "+permission +" ==================");


        call.enqueue(new Callback<List<ChangePermissionResponse>>() {
            @Override
            public void onResponse(Call<List<ChangePermissionResponse>> call, Response<List<ChangePermissionResponse>> response) {

                if (!response.isSuccessful()) {
                    System.out.println("\n\n\n\n********************"+ "Code: " + response.code() +"********************\n\n\n\n");

                    Toast.makeText(getContext(), "Code: " + response.code(),  Toast.LENGTH_SHORT).show();
                    return;
                }


                List<ChangePermissionResponse> changePermissionResponse = response.body();
                System.out.println("\n\n\n\n********************"+ " RESPONSE:: "+ changePermissionResponse.get(0).getVendorStatus() +" ********************\n\n\n\n");
                System.out.println("\n\n\n\n********************"+ " Vendor Permission Changed "+"********************\n\n\n\n");

                if(permission.equals("REJECTED")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("\n\n\n\n********************"+ " begin cancel order "+"********************\n\n\n\n");
                            cancelAllCookingOrders(vendor_id);

                        }
                    }, 2000);

                }

            }

            @Override
            public void onFailure(Call<List<ChangePermissionResponse>> call, Throwable t) {
                System.out.println("\n\n\n\n******************** FAIL: "+ t.getMessage() +"********************\n\n\n\n");


            }
        });

    }


    private void cancelAllCookingOrders(final int vendor_id) {

        String url = "https://vcanteen.herokuapp.com/";
        String reason = "The vendor is temporarily blocked from the system.";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Void> call = jsonPlaceHolderApi.cancelAllOrder(vendor_id,reason);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()) {
                    //vendorNameInput.setText("Code: " + response.code());
                    System.out.println("\n\n\n\n********************" + "Code: " + response.code() + "********************\n\n\n\n");
                    return;
                }

                System.out.println("\n\n\n\n********************"+ " ALL COOKING of vendor id "+ vendor_id +"********************\n\n\n\n");


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //vendorProfile.setText(t.getMessage());
                System.out.println("\n\n\n\n********************" + t.getMessage() + "********************\n\n\n\n");


            }
        });

    }

}
