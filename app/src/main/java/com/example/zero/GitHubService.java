package com.example.zero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Zero on 2017/3/13.
 */

public interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors1(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors2(@Path("owner") String owner, @Path("repo") String repo);

    //利用自定义的Converter和CallAdapter实现转换
    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors3(@Path("owner") String owner, @Path("repo") String repo);
}
