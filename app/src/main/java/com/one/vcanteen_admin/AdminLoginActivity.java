package com.one.vcanteen_admin;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private final String url = "https://vcanteen.herokuapp.com/";

    private long backPressedTime;
    private Toast backToast;
    private ProgressDialog progressDialog;

    ImageView logoImage;
    TextInputLayout emailInputLayout;
    TextInputEditText emailInput;
    TextInputLayout passwordInputLayout;
    TextInputEditText passwordInput;
    Button loginButton;

    private String email;
    private String password;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^\\w+@\\w+\\..{2,3}(.{1,})?$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9@!#$%^&+-=](?=\\S+$).{7,}$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        logoImage = findViewById(R.id.logoImage);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);


        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String savedEmail = sharedPref.getString("email", "empty email");
        String savedToken = sharedPref.getString("token", "empty token");
        System.out.println("CURRENT ADMIN TOKEN ====== "+sharedPref.getString("token", "empty token"));
        System.out.println("CURRENT ADMIN TOKEN EMAIL ====== "+sharedPref.getString("email", "empty email"));

        if(savedEmail.equals("empty email") || savedToken.equals("empty token")){
            System.out.println("------------- Admin Status ===== No saved session token -------------");
        } else {
            System.out.println("------------- Admin Status ===== Saved Session token FOUND! -------------");
            checkTokenValidity();
        }



        emailInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInputLayout.setError("");
                passwordInputLayout.setError("");
            }
        });

        passwordInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInputLayout.setError("");
                passwordInputLayout.setError("");
            }
        });

        passwordInput.setOnEditorActionListener(editorListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInputLayout.setError(null);
                if (emailInput.getText().toString().equals("")){
                    emailInputLayout.setError("Please input Email.");

                } else if( !(EMAIL_PATTERN.matcher(emailInput.getText().toString()).matches())){
                    emailInputLayout.setError("Invalid email. Please try again.");

                } else if (passwordInput.getText().toString().equals("")){
                    passwordInputLayout.setError("Please input Password.");

                }else if(!PASSWORD_PATTERN.matcher(passwordInput.getText().toString()).matches()) {
                    passwordInputLayout.setError("Password must be letter, number or these characters _ - * ' \" # & () @");
                } else{


                    email = emailInput.getText().toString();
                    System.out.println("ADMIN EMAIL" + email);
                    password = passwordInput.getText().toString();
                    System.out.println("ADMIN PLAIN PASSWORD" + password);

                    password = new String(Hex.encodeHex(DigestUtils.sha256(password)));
                    System.out.println("ADMIN HASH PASSWORD" + password);

                    sendLogin(email,password);
                }

            }
        });


    }

    private void checkTokenValidity() {

        progressDialog = new ProgressDialog(AdminLoginActivity.this);
        progressDialog = ProgressDialog.show(AdminLoginActivity.this, "",
                "Loading. Please wait...", true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Token token = new Token(sharedPref.getString("email", "empty email"), sharedPref.getString("token", "empty token"));
        System.out.println("ADMIN SENT TOKEN ====== "+sharedPref.getString("token", "empty token"));
        System.out.println("ADMIN SENT TOKEN EMAIL === "+sharedPref.getString("email", "empty email"));

        Call<TokenVerification> call = jsonPlaceHolderApi.verifyToken(token);

        // POST DATA FOR TOKEN VERIFICATION
        call.enqueue(new Callback<TokenVerification>() {
            @Override
            public void onResponse(Call<TokenVerification> call, final Response<TokenVerification> response) {
                if(!response.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Error Occured, please try again.", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        boolean isExpired = response.body().isExpired();
                        System.out.println("ADMIN TOKEN ISEXPIRE? =======" + isExpired);

                        if(isExpired){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(AdminLoginActivity.this, AdminMainPageActivity.class));
                        }
                    }
                }, 1000);


            }

            @Override
            public void onFailure(Call<TokenVerification> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");

            }
        });

    }


    private void sendLogin(final String email, String pass){

        progressDialog = new ProgressDialog(AdminLoginActivity.this);
        progressDialog = ProgressDialog.show(AdminLoginActivity.this, "",
                "Logging in...", true);


        LoginAdmin loginAdmin = new LoginAdmin(email, pass);
        System.out.println("ADMIN EMAIL PASS ===="+ loginAdmin.toString());

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<LoginResponse> call = jsonPlaceHolderApi.sendLogin(loginAdmin);
        //System.out.println(loginVendor.toString());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println(response.code());
                if (response.code() != 200 ) {
                    // Fail
                    progressDialog.dismiss();
                    emailInputLayout.setError("Email or Password is Incorrect");
                } else {
                    // Success

                    // save admin_id, token
                    sharedPref.edit().putString("token", response.body().getToken()).commit();
                    sharedPref.edit().putInt("admin_id", response.body().getAdminId()).commit();
                    sharedPref.edit().putString("email", email).commit();
                    System.out.println("==================TOKEN :::: "+response.body().getToken()+" ==================");
                    System.out.println("==================Admin ID :::: "+response.body().getAdminId()+" ==================");

                    progressDialog.dismiss();
                    startActivity(new Intent(AdminLoginActivity.this, AdminMainPageActivity.class));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");

            }
        });
    }


    public void hideKb(View view){ //For hiding soft keyboard when tap outside
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
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

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_DONE){
                emailInputLayout.setError(null);
                if (emailInput.getText().toString().equals("")){
                    emailInputLayout.setError("Please input Email.");

                } else if( !(EMAIL_PATTERN.matcher(emailInput.getText().toString()).matches())){
                    emailInputLayout.setError("Invalid email. Please try again.");

                } else if (passwordInput.getText().toString().equals("")){
                    passwordInputLayout.setError("Please input Password.");

                }else if(!PASSWORD_PATTERN.matcher(passwordInput.getText().toString()).matches()) {
                    passwordInputLayout.setError("Password must be letter, number or these characters _ - * ' \" # & () @");
                } else{


                    email = emailInput.getText().toString();
                    System.out.println("ADMIN EMAIL" + email);
                    password = passwordInput.getText().toString();
                    System.out.println("ADMIN PLAIN PASSWORD" + password);

                    password = new String(Hex.encodeHex(DigestUtils.sha256(password)));
                    System.out.println("ADMIN HASH PASSWORD" + password);

                    sendLogin(email,password);
                }
            }

            return false;
        }
    };
}
