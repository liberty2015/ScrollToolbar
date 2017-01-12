package me.liberty.scrolltoolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import me.liberty.scrolltoolbar.widget.ObservableScrollView;

public class MainActivity extends AppCompatActivity implements ObservableScrollView.ScrollViewChangeListener{

    private Toolbar toolbar;
    private ImageView imageView;
    private int imageHeight;
    private ObservableScrollView scrollView;
    private AppBarLayout appBarLayout;
    private float elevation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window=getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
        ViewGroup contentView= (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView=contentView.getChildAt(0);
//        if (mChildView!=null){
//            ViewCompat.setFitsSystemWindows(mChildView,true);
//        }
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin= getStatusHeight();
        toolbar.setLayoutParams(layoutParams);
//        ViewCompat.setFitsSystemWindows(toolbar,true);
        imageView= (ImageView) findViewById(R.id.image);
        scrollView= (ObservableScrollView) findViewById(R.id.scrollView);
        appBarLayout= (AppBarLayout) findViewById(R.id.appBar);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight=imageView.getHeight();
                scrollView.setScrollViewListerer(MainActivity.this);
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        elevation=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics());

    }

    @Override
    public void onScrollChange(int l, int t, int oldl, int oldt) {
        Log.d("xxxxx","y="+t);
        Log.d("xxxxx","imageHeight="+imageHeight);
        if (t<=0){
            int color=Color.argb(0,63,81,181);
            toolbar.setBackgroundColor(color);
            setStatusBarColor(color);
//            appBarLayout.setElevation(0);
        }else if (t>0&&t<=imageHeight){
            float scale=(float) t/imageHeight;
            float alpha=(scale*255);
            float nowElevation=elevation*scale;
            Log.d("xxxxx","scale="+scale+"  alpha="+alpha);
//            appBarLayout.setElevation(nowElevation);
            int color=Color.argb((int) alpha,63,81,181);
            setStatusBarColor(color);
            toolbar.setBackgroundColor(color);
        }else {
            int color=Color.argb(255,63,81,181);
            toolbar.setBackgroundColor(color);
            setStatusBarColor(color);
//            appBarLayout.setElevation(elevation);
        }
    }

    private int getStatusHeight(){
        int resId=getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight=getResources().getDimensionPixelSize(resId);
        return statusBarHeight;
    }

    private void setStatusBarColor(int statusBarColor){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(statusBarColor);
            return;
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT
                &&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            ViewGroup contentView= (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View statusBarView=contentView.getChildAt(0);
            if (statusBarView!=null&&statusBarView.getMeasuredHeight()==getStatusHeight()){
                statusBarView.setBackgroundColor(statusBarColor);
                return;
            }
            statusBarView=new View(this);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusHeight());
            statusBarView.setBackgroundColor(statusBarColor);
            contentView.addView(statusBarView,params);
        }
    }
}
