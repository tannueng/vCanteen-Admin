package com.one.vcanteen_admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminMainPageActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private ProgressDialog progressDialog;

    VendorList vendorList;
    List<Vendor> vendorArrayList;

    ListView vendorListListView;

    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        vendorListListView = findViewById(R.id.vendorListListView);
        signOutButton = findViewById(R.id.signOutButton);


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPageActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });


        ////////// FOR TESTING////////////
        Vendor test = new Vendor("CLOSED",
                132,
                "",
                "THIS IS MY NAME",
                "inwza555007@hotmail.com",
                "0888888888",
                "FACEBOOK",
                "APPROVED");
        vendorArrayList = new ArrayList<Vendor>();
        vendorArrayList.add(test);
        vendorList = new VendorList(vendorArrayList);
        ListAdapter vendorListAdapter = new VendorListAdapter(AdminMainPageActivity.this, vendorList); //Put the arraylist here
        vendorListListView.setAdapter(vendorListAdapter);


        //loadAllVendor();

    }

    private void loadAllVendor() {

        progressDialog = new ProgressDialog(AdminMainPageActivity.this);
        progressDialog = ProgressDialog.show(AdminMainPageActivity.this, "",
                "Loading. Please wait...", true);

        String url="https://vcanteen.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<VendorList> call = jsonPlaceHolderApi.getAllVendor(); //SET LOGIC TO INSERT ID HERE


        call.enqueue(new Callback<VendorList>() {
            @Override
            public void onResponse(Call<VendorList> call, Response<VendorList> response) {

                if (!response.isSuccessful()) {
                    System.out.println("\n\n\n\n********************"+ "Code: " + response.code() +"********************\n\n\n\n");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "You have 0 review.",  Toast.LENGTH_SHORT).show();
                    return;
                }


                vendorList = response.body();
                if(vendorList !=null){

                    ListAdapter vendorListAdapter = new VendorListAdapter(AdminMainPageActivity.this, vendorList); //Put the arraylist here
                    vendorListListView.setAdapter(vendorListAdapter);
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<VendorList> call, Throwable t) {
                //vendorProfile.setText(t.getMessage());
                System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            //super.onBackPressed();

            /*Intent intent = new Intent(this, AdminLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);*/
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);


            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();

        }

        backPressedTime = System.currentTimeMillis();

    }

}
