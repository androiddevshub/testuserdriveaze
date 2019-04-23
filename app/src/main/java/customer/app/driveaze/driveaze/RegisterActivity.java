package customer.app.driveaze.driveaze;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {


    EditText editname, editphone, editemail, editpassword;
    Button btnsignup;
    LinearLayout textSignIn;
    ProgressBar progressBar;
    private String name, email , phone, password, otp;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        editname = findViewById(R.id.editTextName);
        editphone = findViewById(R.id.editTextPhone);
        editemail =  findViewById(R.id.editTextEmail);
        editpassword = findViewById(R.id.editTextPassword);

        btnsignup =  findViewById(R.id.btnSignUp);

        textSignIn = findViewById(R.id.textForSignIn);



        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editname.getText().toString();
                phone = editphone.getText().toString();
                email = editemail.getText().toString();
                password = editpassword.getText().toString();


                if (TextUtils.isEmpty(name)){

                    Toast.makeText(RegisterActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    editname.requestFocus();
                }

                else if (TextUtils.isEmpty(phone)){

                    Toast.makeText(RegisterActivity.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                    editphone.requestFocus();
                }

                else if (TextUtils.isEmpty(email)){

                    Toast.makeText(RegisterActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    editemail.requestFocus();
                }

                else if (TextUtils.isEmpty(password)){

                    Toast.makeText(RegisterActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    editpassword.requestFocus();
                }

                else if (phone.length() != 10){

                    Toast.makeText(RegisterActivity.this, "Phone number should be of 10 digits", Toast.LENGTH_SHORT).show();
                    editphone.requestFocus();
                }

                else {

                    NetworkAPI networkAPI = ApiClient.getClient().create(NetworkAPI.class);

                    Call<Response> registrationResponse = networkAPI.registeruser(name, email, phone, password);

                    registrationResponse.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                            if (response.isSuccessful()){

                                Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                verifyCode();
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


    private void verifyCode() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.verify_otp_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText enterOTP = (EditText) dialogView.findViewById(R.id.editTextOTP);
        final Button btnVerify = (Button) dialogView.findViewById(R.id.btnVerifyOTP);
        final TextView showMobile = (TextView) dialogView.findViewById(R.id.textOTPVerify);
        // final ProgressBar progressBar1 = (ProgressBar) dialogView.findViewById(R.id.progressBar);

        showMobile.setText("Enter OTP sent to "+  phone + " to verify your account");
        //dialogBuilder.setTitle("Send Photos");
        final AlertDialog dialog = dialogBuilder.create();



        btnVerify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                otp  = enterOTP.getText().toString().trim();
                if (otp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"enter OTP",Toast.LENGTH_SHORT).show();
                }
                else  {

                    NetworkAPI networkAPI = ApiClient.getClient().create(NetworkAPI.class);

                    Call<Response> verifyResponse = networkAPI.verifyuser(phone, otp);

                    verifyResponse.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                            if (response.body().getCode().equals("success")){

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                            }else {
                                showToast("Enter valid OTP");
                            }

                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });

                }
            }
        });

        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
