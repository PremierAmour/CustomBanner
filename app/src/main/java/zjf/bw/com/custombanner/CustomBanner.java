package zjf.bw.com.custombanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * author: 晨光光
 * date : 2018/5/31 10:00
 */
public class CustomBanner extends RelativeLayout {
    private int size;
    private List<String> urls;
    private Context context;
    // 默认轮播
    private boolean isPlay = true;
    private ViewPager viewPager;
    // 创建Handler
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int index = msg.what;
            viewPager.setCurrentItem(index);

            // 根据图片的切换 改变指示器的颜色
            if (linearLayout != null) {
                int childCount = linearLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (index % size == i) {
                        linearLayout.getChildAt(i).setEnabled(true);
                    } else {
                        linearLayout.getChildAt(i).setEnabled(false);
                    }
                }
            }
        }
    };
    private LinearLayout linearLayout;


    public CustomBanner(Context context) {
        this(context, null);
    }

    public CustomBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initView() {
        // 如果在 application里面配置  这里就不需要初始化
        Fresco.initialize(context);
        // 创建ViewPager
        viewPager = new ViewPager(context);
        // 初始化layoutParams
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200);
        // 设置参数(宽高属性)
        viewPager.setLayoutParams(layoutParams);
        //
        viewPager.setAdapter(new MPagerAdapter());
        addView(viewPager);
    }

    // 设置图片路径
    public void setImageUrls(List<String> urls) {
        this.urls = urls;
        this.size = urls.size();
        initView();
    }

    // 设置是否自动轮播
    public void setAutoPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public void play() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; isPlay; i++) {
                    handler.sendEmptyMessage(i);
                    // 睡眠两秒
                    SystemClock.sleep(2000);
                }
            }
        }.start();
    }

    class MPagerAdapter extends PagerAdapter {

        public MPagerAdapter() {
            if (urls == null || urls.size() <= 0) {
                throw new NullPointerException("请设置图片路径");
            }
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // 创建SimpleDraweeView
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
            // 初始化layoutParams
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200);
            // 设置宽高
            simpleDraweeView.setLayoutParams(layoutParams);
            // 转为Uri
            Uri uri = Uri.parse(urls.get(position % size));
            // 设置加载路径
            simpleDraweeView.setImageURI(uri);
            // 填充到父元素
            container.addView(simpleDraweeView);
            return simpleDraweeView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }

    // 添加指示器
    public void addPointer() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 30);
        //设置水平居中 这里没有起作用 不知道为什么
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);

        LayoutParams childLp = new LayoutParams(30, 30);
        childLp.setMargins(13,13,13,13);
        // 有多少张图片就创建多少个点
        for (int i = 0; i < size; i++) {
            View view = new View(context);

            if (i == 0) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }

            view.setBackgroundResource(R.drawable.pointer_selector);

            linearLayout.addView(view,childLp);
        }
        // 添加到父容器
        addView(linearLayout);
    }

    // 设置指示器位置
    public void setPointerLayout() {

    }

    // 合理使用缓存
    public void destroy() {

    }

    protected void onResume() {

    }

    protected void onPause() {

    }
}
