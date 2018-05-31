package zjf.bw.com.custombanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    //拍照保存的路径任意
    private String pic_path = Environment.getExternalStorageDirectory() + "/head.jpg";
    //裁剪完成之后图片保存的路径
    private String crop_icon_path = Environment.getExternalStorageDirectory() + "/head_icon.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> list = new ArrayList<>();
        list.add("http://img0.imgtn.bdimg.com/it/u=2801862717,279628383&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2801862717,279628383&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2801862717,279628383&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2801862717,279628383&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2801862717,279628383&fm=27&gp=0.jpg");


        CustomBanner customBanner = findViewById(R.id.cb);

        customBanner.setImageUrls(list);
        customBanner.play();
        customBanner.addPointer();
    }


    // 调用摄像机
    public void takePhoto(View view) {
        Intent intent = new Intent();
        //指定动作...拍照的动作 CAPTURE...捕获
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //给相机传递一个指令,,,告诉他拍照之后保存..MediaStore.EXTRA_OUTPUT向外输出的指令,,,指定存放的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pic_path)));

        //拍照的目的是拿到拍的图片
        startActivityForResult(intent, 1000);
    }

    // 调用相册
    public void photoAlbum(View view) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {

            //拍照保存之后进行裁剪....根据图片的uri路径
            crop(Uri.fromFile(new File(pic_path)));
        }

        //获取相册图片
        if (requestCode == 3000 && resultCode == RESULT_OK) {
            //获取的是相册里面某一张图片的uri地址
            Uri uri = data.getData();

            //根据这个uri地址进行裁剪
            crop(uri);
        }

        if (requestCode == 2000 && resultCode == RESULT_OK) {
            //获取到裁剪完的图片
          //  Bitmap bitmap = data.getParcelableExtra("data");

            //拿到了bitmap图片 ..需要把bitmap图片压缩保存到文件中去..应该去做上传到服务器的操作了
            File saveIconFile = new File(crop_icon_path);

            if (saveIconFile.exists()) {
                saveIconFile.delete();
            }

            try {
                //创建出新的文件
                saveIconFile.createNewFile();

                Log.e("fielName",saveIconFile.getName()+"PP"+saveIconFile.getAbsolutePath());
                //把文件上传到服务器
                uploadIcon(saveIconFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadIcon(File file){
        HashMap<String, String> map = new HashMap<>();
        map.put("uid","15263");
        map.put("token","E36A16BF768F16D0132C01CAFCFC958A" );
        map.put("source","android" );
        map.put("appVersion","101" );
        RequestBody requestBody = MultipartBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        UploadIcon.uploadFile(map, part);
    }

    /**
     * 根据图片的uri路径进行裁剪
     * @param uri
     */
    private void crop(Uri uri) {

        Intent intent = new Intent();

        //指定裁剪的动作
        intent.setAction("com.android.camera.action.CROP");

        //设置裁剪的数据(uri路径)....裁剪的类型(image/*)
        intent.setDataAndType(uri, "image/*");

        //执行裁剪的指令
        intent.putExtra("crop", "true");
        //指定裁剪框的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //指定输出的时候宽度和高度
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        //设置取消人脸识别
        intent.putExtra("noFaceDetection", false);
        //设置返回数据
        intent.putExtra("return-data", true);

        //
        startActivityForResult(intent, 2000);

    }
}
