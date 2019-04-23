package customer.app.driveaze.driveaze;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    EditText editphonelogin, editpasswordlogin;
    Button btnSignin;
    LinearLayout textSignUp;
    TextView forgotpassword;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotpassword = findViewById(R.id.textForgotPassword);
        editphonelogin = (EditText) findViewById(R.id.editTextPhoneLogin);
        editpasswordlogin = (EditText) findViewById(R.id.editTextPasswordLogin);

        btnSignin = (Button) findViewById(R.id.btnSignIn);

        textSignUp = (LinearLayout) findViewById(R.id.textForSignup);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PasswordResetActivity.class);
                startActivity(intent);
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class ));
            }
        });


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone  = editphonelogin.getText().toString();
                String password = editpasswordlogin.getText().toString();

                if (TextUtils.isEmpty(phone)){

                    showToast("Please enter contact number");

                }else  if (phone.length() != 10){

                    showToast("Contact number should be of 10 digits");

                }else  if (TextUtils.isEmpty(password)){

                    showToast("Please enter password");

                }else {


                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                    showToast("Login Successful");


                    NetworkAPI networkAPI = ApiClient.getClient().create(NetworkAPI.class);

                    Call<Response> loginResponse = networkAPI.loginuser(phone, password);


                    loginResponse.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                            if (response.body().getCode().equals("success")){

                                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                                startActivity(intent);
                                showToast("Login Successful");

                            }else {
                                showToast("Enter valid credentials");
                            }

                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });

                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: checking google service version");

        int available = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(LoginActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOk: Google play services is missing");
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "we can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
