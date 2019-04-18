package com.one.vcanteen_admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

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

    RequestOptions option = new RequestOptions().centerCrop();;


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
            vendorIdContainer.setText(String.valueOf(singleVendor.getVendorId()));

            //Parse vendor image URL into placeholder using Glide
            //Glide.with(this.getContext()).load(singleVendor.getVendorImage()).apply(option).into(vendorProfileImage);

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
                    String contentWithVendorName = contentText.replace("[vendorName]", singleVendor.getRestaurantName());
                    content.setText(contentWithVendorName);
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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(), "Saved!",  Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), AdminMainPageActivity.class);
                                getContext().startActivity(intent);
                                positiveButton.setEnabled(true);

                            }
                        }, 3000);

                        //Toast.makeText(getApplicationContext(), "MENU DELETED!",  Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

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
                    String contentWithVendorName = contentText.replace("[vendorName]", singleVendor.getRestaurantName());
                    content.setText(contentWithVendorName);
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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(), "Saved!",  Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), AdminMainPageActivity.class);
                                getContext().startActivity(intent);
                                positiveButton.setEnabled(true);

                            }
                        }, 3000);

                        //Toast.makeText(getApplicationContext(), "MENU DELETED!",  Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });



        return customView;
    }

}
