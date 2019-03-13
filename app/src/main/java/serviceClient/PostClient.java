package serviceClient;

import java.util.List;

import model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PostClient {
    @POST("user")
    Call<Post> postDetails(@Body Post post);

    @PUT("userlog")
    Call<Post> putDetails(@Body Post put);
    //here we return response body. the reason for this is so that we can turn the whole response object to string the later
    //extract values from it using JSONObject.
    @GET("users")
    Call<ResponseBody> getUsers(@Header("Authorization") String token);
}
