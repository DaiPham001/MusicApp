package com.example.musicapp.Retrofit;

import com.example.musicapp.Model.Advertisement_Model;
import com.example.musicapp.Model.Album_Model;
import com.example.musicapp.Model.Category_Model;
import com.example.musicapp.Model.CategoryendTopic_Model;
import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Model.Playlist_Model;
import com.example.musicapp.Model.Topic_Model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    // get quang cáo
    @GET("advertisement.php")
    Call<Advertisement_Model> getquangcao();

    // get all playlist
    @GET("getallplaylist.php")
    Call<Playlist_Model> getallplaylist();

    @GET("getplaylist.php")
    Call<Playlist_Model> getplaylist();

    // get chu de và thể loại
    @GET("getcategorytopic.php")
    Call<CategoryendTopic_Model> getcategorytopic();

    // get all chu de
    @GET("getalltopic.php")
    Call<Topic_Model> getalltopic();

    // get 4 album
    @GET("getalbum.php")
    Call<Album_Model> getalbum();

    // get all album
    @GET("getallalbum.php")
    Call<Album_Model> getallalbum();

    // lấy danh dánh music theo luot thich
    @GET("getpopular.php")
    Call<Music_Model> getpopular();

    // // lấy danh dánh music theo idqc
    @POST("getmusic_qc.php")
    @FormUrlEncoded
    Call<Music_Model> getmusic_qc(
            @Field("idqc") int idqc);

    // lấy danh dánh music theo theo idplaylist
    @POST("getmusic_playlist.php")
    @FormUrlEncoded
    Call<Music_Model> getmusic_playlist(
            @Field("idplaylist") int idplaylist);

    // lấy danh dánh music theo idtheloai
    @POST("getmusic_cate_top.php")
    @FormUrlEncoded
    Call<Music_Model> getmusic_cate_top
            (@Field("idtheloai") int idtheloai);

    // lấy danh dánh listmusic theo idalbum
    @POST("getmusic_album.php")
    @FormUrlEncoded
    Call<Music_Model> getmusic_album
    (@Field("idalbum") int idalbum);

    // lấy danh dánh the loai theo chu de
    @POST("getcategory.php")
    @FormUrlEncoded
    Call<Category_Model> getcategory
    (@Field("idchude") int idchude);

    // update cac bài hát đc yêu thích
    @POST("update_luotthich.php")
    @FormUrlEncoded
    Call<Music_Model> update_luotthich
    (@Field("idbaihat") int idbaihat);


    // tìm kiếm bài hát theo tên
    @POST("search.php")
    @FormUrlEncoded
    Call<Music_Model> search
    (@Field("search") String tenbaihat);
}
