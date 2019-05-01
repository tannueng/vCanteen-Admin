package com.one.vcanteen_admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    TextView adminEmailText;

    Button signOutButton;
    Button refreshButton;
    private SharedPreferences sharedPref;
    int admin_id;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        vendorListListView = findViewById(R.id.vendorListListView);
        signOutButton = findViewById(R.id.signOutButton);
        adminEmailText = findViewById(R.id.adminEmailText);
        refreshButton = findViewById(R.id.refreshButton);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        admin_id = sharedPref.getInt("admin_id", 0);
        email = sharedPref.getString("email", "empty email");
        System.out.println("ADMIN TOKEN EMAIL Main page "+sharedPref.getString("email", "empty email"));

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        if (!email.equals("empty email")){
            adminEmailText.setText(email);

        }



        ////////// FOR TESTING////////////
        /*Vendor test = new Vendor("CLOSED",
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
        vendorListListView.setAdapter(vendorListAdapter);*/

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AdminMainPageActivity.this);
                //dialog.setTitle("Devahoy");
                dialog.setContentView(R.layout.dialog_sign_out);

                final TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                Button negativeButton = (Button) dialog.findViewById(R.id.negativeButton);
                Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOut();
                        Toast.makeText(getApplicationContext(), "LOG OUT SUCCESS!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

        loadAllVendor();

    }


    private void logOut() {
        sharedPref.edit().putString("token", "NO TOKEN").commit();
        sharedPref.edit().putInt("admin_id", 0).commit();
        sharedPref.edit().putString("email", "empty email").commit();

        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);

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
