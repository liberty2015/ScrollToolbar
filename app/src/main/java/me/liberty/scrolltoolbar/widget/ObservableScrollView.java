package me.liberty.scrolltoolbar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by LinJinFeng on 2017/1/12.
 */

public class ObservableScrollView extends ScrollView {
    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param l 滑动之后的x坐标
     * @param t 滑动之后的y坐标
     * @param oldl 滑动之前的x坐标
     * @param oldt 滑动之前的y坐标
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener!=null){
            scrollViewListener.onScrollChange(l, t, oldl, oldt);
        }
    }

    public void setScrollViewListerer(ScrollViewChangeListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    private ScrollViewChangeListener scrollViewListener ;

    public interface ScrollViewChangeListener{
        void onScrollChange(int l, int t, int oldl, int oldt);
    }
}
