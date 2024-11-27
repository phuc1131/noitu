package com.example.noitu;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    @GET("api/v2/entries/en/{word}") // Ví dụ sử dụng API miễn phí từ dictionaryapi.dev
    Call<Object> checkWord(@Path("word") String word);
}

