package customer.app.driveaze.driveaze;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;

public class PasswordResetActivity extends AppCompatActivity {

    TextInputLayout iplcontact, iplotp, iplpassword;
    EditText etcontact, etotp, etpassword;
    String contact, otp, password;
    Button btnVerify, btnReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        iplcontact = findViewById(R.id.inputLayoutContactPR);
        iplotp = findViewById(R.id.inputLayoutOTPPR);
        iplpassword = findViewById(R.id.inputLayoutPasswordPR);

        etcontact = findViewById(R.id.editTexContactPR);
        etotp = findViewById(R.id.editTextOTPPR);
        etpassword = findViewById(R.id.editTextPasswordPR);

        btnVerify = findViewById(R.id.btnSubmitPR);
        btnReset = findViewById(R.id.btnResetPR);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contact = etcontact.getText().toString();

                if (contact.isEmpty()){
                    showToast("Enter contact number");
                } else{

                    NetworkAPI networkAPI = ApiClient.getClient().create(NetworkAPI.class);

                    Call<Response> loginResponse = networkAPI.passwordRequest(contact);

                    loginResponse.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                            if (response.body().getCode().equals("success")){

                                iplotp.setVisibility(View.VISIBLE);
                                iplpassword.setVisibility(View.VISIBLE);
                                etcontact.setEnabled(false);
                                btnVerify.setVisibility(View.GONE);
                                btnReset.setVisibility(View.VISIBLE);


                                showToast(response.body().getMessage());

                            }else {
                                showToast("This contact number is not registered with us");
                            }

                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });

                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("passwordreset", "Contact: "+ contact);

                otp = etotp.getText().toString();
                password = etpassword.getText().toString();

                if (otp.isEmpty()){
                    showToast("Enter OTP");
                }else if (password.isEmpty()){
                    showToast("Enter password");
                }else if (!otp.equals("121212")){
                    showToast("Enter valid OTP");
                }else {

                    NetworkAPI networkAPI = ApiClient.getClient().create(NetworkAPI.class);

                    Call<Response> loginResponse = networkAPI.passwordSubmit(contact,otp, password);

                    loginResponse.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                            if (response.body().getCode().equals("success")){

                                showToast(response.body().getMessage());

                                Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                startActivity(intent);


                            }else if (response.body().getCode().equals("error")){

                                showToast(response.body().getMessage());

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

    private void showToast(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
