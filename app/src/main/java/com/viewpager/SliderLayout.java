package com.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.skilrock.lms.ui.R;
import com.viewpager.Animations.BaseAnimationInterface;
import com.viewpager.Transformers.BaseTransformer;
import com.viewpager.Transformers.TabletTransformer;
import com.viewpager.Tricks.FixedSpeedScroller;

import java.lang.reflect.Field;

/**
 * <p/>
 * There is some properties you can set in XML:
 * <p/>
 * indicator_visibility
 * visible
 * invisible
 * <p/>
 * indicator_shape
 * oval
 * rect
 * <p/>
 * indicator_selected_color
 * <p/>
 * indicator_unselected_color
 * <p/>
 * indicator_selected_drawable
 * <p/>
 * indicator_unselected_drawable
 * <p/>
 * pager_animation
 * Default
 * Accordion
 * Background2Foreground
 * CubeIn
 * DepthPage
 * Fade
 * FlipHorizontal
 * FlipPage
 * Foreground2Background
 * RotateDown
 * RotateUp
 * Stack
 * Tablet
 * ZoomIn
 * ZoomOutSlide
 * ZoomOut
 * <p/>
 * pager_animation_span
 */
public class SliderLayout extends ViewPager {

    private int mTransformerId;

    /**
     * {@link ViewPager} transformer time span.
     */
    private int mTransformerSpan = 1100;

    /**
     * the duration between animation.
     */
    private long mSliderDuration = 4000;

    /**
     * {@link ViewPager} 's transformer
     */
    private BaseTransformer mViewPagerTransformer;

    /**
     * @see BaseAnimationInterface
     */
    private BaseAnimationInterface mCustomAnimation;


    public SliderLayout(Context context) {
        this(context, null);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SliderLayout);

        mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 500);
        mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, Transformer.Tablet.ordinal());
//        mViewPager = (ViewPager) findViewById(R.id.daimajia_slider_viewpager);
        attributes.recycle();
        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan, null);
    }

    /**
     * set ViewPager transformer.
     *
     * @param reverseDrawingOrder
     * @param transformer
     */
    public void setPagerTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        setPageTransformer(reverseDrawingOrder, mViewPagerTransformer);
    }


    /**
     * set the duration between two slider changes.
     *
     * @param period
     * @param interpolator
     */
    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), interpolator, period);
            mScroller.set(this, scroller);
        } catch (Exception e) {

        }
    }

    /**
     * preset transformers and their names
     */
    public enum Transformer {
        Default("Default"),
        Accordion("Accordion"),
        Background2Foreground("Background2Foreground"),
        CubeIn("CubeIn"),
        DepthPage("DepthPage"),
        Fade("Fade"),
        FlipHorizontal("FlipHorizontal"),
        FlipPage("FlipPage"),
        Foreground2Background("Foreground2Background"),
        RotateDown("RotateDown"),
        RotateUp("RotateUp"),
        Stack("Stack"),
        Tablet("Tablet"),
        ZoomIn("ZoomIn"),
        ZoomOutSlide("ZoomOutSlide"),
        ZoomOut("ZoomOut");

        private final String name;

        private Transformer(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(String other) {
            return (other == null) ? false : name.equals(other);
        }
    }

    ;

    /**
     * set a preset viewpager transformer by id.
     *
     * @param transformerId
     */
    public void setPresetTransformer(int transformerId) {
        for (Transformer t : Transformer.values()) {
            if (t.ordinal() == transformerId) {
                setPresetTransformer(t);
                break;
            }
        }
    }

    /**
     * pretty much right? enjoy it. :-D
     *
     * @param ts
     */
    public void setPresetTransformer(Transformer ts) {
        //
        // special thanks to https://github.com/ToxicBakery/ViewPagerTransforms
        //
        BaseTransformer t = null;
        switch (ts) {
            case Tablet:
                t = new TabletTransformer();
                break;
        }
        setPagerTransformer(true, t);
    }

    private PagerAdapter getRealAdapter() {
        PagerAdapter adapter = getAdapter();
        if (adapter != null) {
            return adapter;
        }
        return null;
    }

    /**
     * get the current item position
     *
     * @return
     */
    public int getCurrentPosition() {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        return getCurrentItem() % getRealAdapter().getCount();

    }

    /**
     * set current slider
     *
     * @param position
     */
    public void setCurrentPosition(int position, boolean smooth) {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if (position >= getRealAdapter().getCount()) {
            throw new IllegalStateException("Item position is not exist");
        }
        int p = getCurrentItem() % getRealAdapter().getCount();
        int n = (position - p) + getCurrentItem();
        setCurrentItem(n, smooth);
    }

    public void setCurrentPosition(int position) {
        setCurrentPosition(position, true);
    }

    /**
     * move to prev slide.
     */
    public void movePrevPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        setCurrentItem(getCurrentItem() - 1, smooth);
    }

    public void movePrevPosition() {
        movePrevPosition(true);
    }

    /**
     * move to next slide.
     */
    public void moveNextPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        setCurrentItem(getCurrentItem() + 1, smooth);
    }

    public void moveNextPosition() {
        moveNextPosition(true);
    }
}
