package customer.app.driveaze.driveaze;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkAPI {

    /* Registration */

    @POST("user/registration")
    Call<Response> registeruser(@Query("name") String name, @Query("email") String email,
                             @Query("contact") String contact, @Query("password") String password);

    /* Otp verifiy */

    @POST("user/verify/otp")
    Call<Response> verifyuser(@Query("contact") String contact, @Query("otp") String otp);

    /* Login */

    @POST("user/login")
    Call<Response> loginuser(@Query("contact") String contact, @Query("password") String password);

    /* Reset password request */

    @POST("user/reset/password/request")
    Call<Response> passwordRequest(@Query("contact") String contact);

    /* Reset password submit */

    @POST("user/reset/password/submit")
    Call<Response> passwordSubmit(@Query("contact") String contact,
                                  @Query("otp") String otp,
                                  @Query("password") String password);



}
