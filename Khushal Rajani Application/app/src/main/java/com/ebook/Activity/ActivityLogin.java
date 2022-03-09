package com.ebook.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebook.R;
import com.ebook.Utility.Authorization;
import com.ebook.Constant.Constant;
import com.ebook.Utility.Function;
import com.ebook.WebService.WebServices;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ActivityLogin extends AppCompatActivity {

    private static final String TAG = ActivityLogin.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private final Function mF = new Function();
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String first_name = null, last_name = null, email = null, image_url = null, FireBaseToken;
    private Context mContext;
    private SharedPreferences sharedpreferences;
    private MaterialButton mButtonFacebook;
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = ActivityLogin.this;

        sharedpreferences = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        FacebookSdk.sdkInitialize(mContext);

        //Facebook Login
        mButtonFacebook = findViewById(R.id.button_facebook);
        FacebookSdk.sdkInitialize(ActivityLogin.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();
        mButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                loginManager.logInWithReadPermissions(ActivityLogin.this, Arrays.asList("email", "public_profile"));
            }
        });

        //Google Login
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(mContext);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Name", personName);
            editor.putString("Email", personEmail);
            editor.putString("Image", "" + personPhoto);
            editor.putString("Type", "Gmail");
            editor.commit();
            editor.apply();
            UserLogin();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void facebookLogin() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            if (object != null) {
                                try {
                                    if (object.has("first_name")) {
                                        first_name = object.getString("first_name");
                                    }
                                    if (object.has("last_name")) {
                                        last_name = object.getString("last_name");
                                    }
                                    if (object.has("email")) {
                                        email = object.getString("email");
                                    }
                                    if (object.has("id")) {
                                        String id = object.getString("id");
                                        image_url = "https://graph.facebook.com/" + id + "/picture?type=square";
                                    }

                                    if (email == null) {
                                        LayoutInflater inflater = getLayoutInflater();
                                        View alertLayout = inflater.inflate(R.layout.dialog, null);
                                        final TextInputEditText etUsername = alertLayout.findViewById(R.id.tiet_username);
                                        final Button BtnEmailSubmit = alertLayout.findViewById(R.id.BtnEmailSubmit);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                        alert.setTitle("Email Address");
                                        alert.setView(alertLayout);
                                        alert.setCancelable(false);
                                        AlertDialog dialog = alert.create();
                                        BtnEmailSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String UserEmail = etUsername.getText().toString();
                                                if (mF.isEmpty(UserEmail)) {
                                                    Toast.makeText(mContext, "Enter Email Address", Toast.LENGTH_SHORT).show();
                                                } else if (mF.Email_validation(UserEmail)) {
                                                    Toast.makeText(mContext, "Enter Valid Email !", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putString("Name", first_name + " " + last_name);
                                                    editor.putString("Email", "" + UserEmail);
                                                    editor.putString("Image", "" + image_url);
                                                    editor.putString("Type", "Facebook");
                                                    editor.commit();
                                                    editor.apply();
                                                    UserLogin();
                                                }
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("Name", first_name + " " + last_name);
                                        editor.putString("Email", "" + email);
                                        editor.putString("Image", "" + image_url);
                                        editor.putString("Type", "Facebook");
                                        editor.commit();
                                        editor.apply();
                                        UserLogin();
                                    }

                                } catch (JSONException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //ActivityExit.exitApplication(mContext);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Name", personName);
            editor.putString("Email", personEmail);
            editor.putString("Image", "" + personPhoto);
            editor.putString("Type", "Gmail");
            editor.commit();
            editor.apply();
            UserLogin();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void UserLogin() {
        String Name, Email, ImageURL, Type;

        Name = sharedpreferences.getString("Name", "");
        Email = sharedpreferences.getString("Email", "");
        ImageURL = sharedpreferences.getString("Image", "");

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JSONObject postData = new JSONObject();
        try {
            postData.put("Name", Name);
            postData.put("Email", Email);
            postData.put("ImageURL", ImageURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.Login, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response", "" + response);
                    JSONObject mObject = new JSONObject(String.valueOf(response));
                    int Code = mObject.getInt("Code");
                    String msg = mObject.getString("message");

                    if (Code == 200) {
                        Authorization.SaveAuthorizationHeader(mContext, "Bearer " + msg);
                        startActivity(new Intent(mContext, ActivityDashboard.class));
                    } else if (Code == 405) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    } else if (Code == 400) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}