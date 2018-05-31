package zjf.bw.com.custombanner;

import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: 晨光光
 * date : 2018/5/31 11:30
 */
public class UploadIcon {
    // 这里我随便写个上传图像工具类

    // 获取ApiService
    private static ApiService getApiService(String string) {
        // 创建OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        ApiService apiService = new Retrofit.Builder()
                .baseUrl(string)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ApiService.class);

        return apiService;
    }


    public static void uploadFile(Map<String, String> map, MultipartBody.Part file) {
        getApiService("https://www.zhaoapi.cn/")
                .uploadPicture(map, file)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Log.e("AA", "onNext --- >" + string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AA", "onError -->" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
