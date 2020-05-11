package com.terryyessfung.whatsins.API;

import com.terryyessfung.whatsins.Model.CommentList;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.Model.UserList;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("/user/login")
    Call<LoginResult> login(@Body HashMap<String,String>map);

    @PUT("/user/following/{uid}")
    Call<ResponseBody> updateFollowing(@Path("uid") String uid, @Body HashMap<String,String> map);

    @DELETE("/user/following/{uid}")
    Call<ResponseBody> removeFollowing(@Path("uid") String uid, @Query("fid") String fid);

    @DELETE("api/post/{pid}")
    Call<ResponseBody> deletePostByPid(@Path("pid")String pid);

    @Multipart
    @POST("/user/register")
    Call<ResponseBody> registerWithImg(@Part("name")RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password,
                                @Part MultipartBody.Part file);

    @Multipart
    @POST("/user/register")
    Call<ResponseBody> register(@Part("name")RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password);

    @Multipart
    @POST("/api/posts")
    Call<ResponseBody> postImage(@Part("publisher")RequestBody publisher,
                                       @Part("desc") RequestBody desc,
                                       @Part("label") RequestBody label,
                                       @Part MultipartBody.Part img);

    @GET("/user")
    Call<UserList> getUsersByKeyWords(@Query("key") String keyword);

    @GET("/user/{uid}")
    Call<User> getUserByID(@Path("uid") String uid);

    @GET("/api/posts/user/{uid}")
    Call<PostsList> getPostsByUid(@Path("uid") String uid);

    @GET("/api/posts/{uid}")
    Call<PostsList> fecthFollowingPosts(@Path("uid") String uid);

    @PUT("/api/posts/like/{uid}")
    Call<ResponseBody> updatePostsLike(@Path("uid") String uid, @Body HashMap<String,String> map);

    @POST("/api/comments/post")
    Call<ResponseBody> addComment(@Body HashMap<String,String>map);

    @GET("/api/comments/post/{pid}")
    Call<CommentList> getCommentByPostId(@Path("pid")String postId);

    @GET("/api/posts")
    Call<PostsList> getAllPosts();

    @GET("/api/post/{pid}")
    Call<Post> getPostByPId(@Path("pid") String uid);

    @PUT("/api/post/desc/{pid}")
    Call<ResponseBody> updatePostDesc(@Path("pid") String uid, @Body HashMap<String,String> map);

}
