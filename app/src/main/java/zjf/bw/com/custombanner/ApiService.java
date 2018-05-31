package zjf.bw.com.custombanner;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * author: 晨光光
 * date : 2018/5/31 12:11
 */
public interface ApiService {

    // 上传单张图片
    @POST("file/upload")
    @Multipart
    Observable<ResponseBody> uploadPicture(@QueryMap Map<String,String> map, @Part MultipartBody.Part part);
}
