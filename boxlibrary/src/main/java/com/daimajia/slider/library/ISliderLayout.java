package com.daimajia.slider.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Animations.IBaseAnimationInterface;
import com.daimajia.slider.library.Animations.IDescriptionAnimation;
import com.daimajia.slider.library.Indicators.IPagerIndicator;
import com.daimajia.slider.library.SliderTypes.IBaseSliderView;
import com.daimajia.slider.library.Transformers.IAccordionTransformer;
import com.daimajia.slider.library.Transformers.IBackgroundToForegroundTransformer;
import com.daimajia.slider.library.Transformers.IBaseTransformer;
import com.daimajia.slider.library.Transformers.ICubeInTransformer;
import com.daimajia.slider.library.Transformers.IDefaultTransformer;
import com.daimajia.slider.library.Transformers.IDepthPageTransformer;
import com.daimajia.slider.library.Transformers.IFadeTransformer;
import com.daimajia.slider.library.Transformers.IFlipHorizontalTransformer;
import com.daimajia.slider.library.Transformers.IFlipPageViewTransformer;
import com.daimajia.slider.library.Transformers.IForegroundToBackgroundTransformer;
import com.daimajia.slider.library.Transformers.IRotateDownTransformer;
import com.daimajia.slider.library.Transformers.IRotateUpTransformer;
import com.daimajia.slider.library.Transformers.IStackTransformer;
import com.daimajia.slider.library.Transformers.ITabletTransformer;
import com.daimajia.slider.library.Transformers.IZoomInTransformer;
import com.daimajia.slider.library.Transformers.IZoomOutSlideTransformer;
import com.daimajia.slider.library.Transformers.IZoomOutTransformer;
import com.daimajia.slider.library.Tricks.IFixedSpeedScroller;
import com.daimajia.slider.library.Tricks.IInfinitePagerAdapter;
import com.daimajia.slider.library.Tricks.IInfiniteViewPager;
import com.daimajia.slider.library.Tricks.IViewPagerEx;
import net.izhuo.app.library.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SliderLayout is compound layout. This is combined with
 * {@link com.daimajia.slider.library.Indicators.IPagerIndicator} and
 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx} .
 *
 * There is some properties you can set in XML:
 *
 * indicator_visibility visible invisible
 *
 * indicator_shape oval rect
 *
 * indicator_selected_color
 *
 * indicator_unselected_color
 *
 * indicator_selected_drawable
 *
 * indicator_unselected_drawable
 *
 * pager_animation Default Accordion Background2Foreground CubeIn DepthPage Fade
 * FlipHorizontal FlipPage Foreground2Background RotateDown RotateUp Stack
 * Tablet ZoomIn ZoomOutSlide ZoomOut
 *
 * pager_animation_span
 *
 *
 */
@SuppressLint("HandlerLeak")
public class ISliderLayout extends RelativeLayout {

	private Context mContext;
	/**
	 * InfiniteViewPager is extended from ViewPagerEx. As the name says, it can
	 * scroll without bounder.
	 */
	private IInfiniteViewPager mViewPager;

	/**
	 * InfiniteViewPager adapter.
	 */
	private ISliderAdapter mSliderAdapter;

	/**
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx} indicator.
	 */
	private IPagerIndicator mIndicator;

	/**
	 * A timer and a TimerTask using to cycle the
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx}.
	 */
	private Timer mCycleTimer;
	private TimerTask mCycleTask;

	/**
	 * For resuming the cycle, after user touch or click the
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx}.
	 */
	private Timer mResumingTimer;
	private TimerTask mResumingTask;

	/**
	 * If {@link com.daimajia.slider.library.Tricks.IViewPagerEx} is Cycling
	 */
	private boolean mCycling;

	/**
	 * Determine if auto recover after user touch the
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx}
	 */
	private boolean mAutoRecover = true;

	private int mTransformerId;

	/**
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx} transformer time
	 * span.
	 */
	private int mTransformerSpan = 1100;

	private boolean mAutoCycle;

	/**
	 * the duration between animation.
	 */
	private long mSliderDuration = 4000;

	/**
	 * Visibility of
	 * {@link com.daimajia.slider.library.Indicators.IPagerIndicator}
	 */
	private IPagerIndicator.IndicatorVisibility mIndicatorVisibility = IPagerIndicator.IndicatorVisibility.Visible;

	/**
	 * {@link com.daimajia.slider.library.Tricks.IViewPagerEx} 's transformer
	 */
	private IBaseTransformer mViewPagerTransformer;

	/**
	 * @see IBaseAnimationInterface
	 */
	private IBaseAnimationInterface mCustomAnimation;

	/**
	 * {@link com.daimajia.slider.library.Indicators.IPagerIndicator} shape, rect
	 * or oval.
	 */

	public ISliderLayout(Context context) {
		this(context, null);
	}

	public ISliderLayout(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.SliderStyle);
	}

	public ISliderLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(context)
				.inflate(R.layout.box_view_slider_layout, this, true);

		final TypedArray attributes = context.getTheme()
				.obtainStyledAttributes(attrs, R.styleable.BoxSliderLayout,
						defStyle, 0);

		mTransformerSpan = attributes.getInteger(
				R.styleable.BoxSliderLayout_pager_animation_span, 1100);
		mTransformerId = attributes.getInt(
				R.styleable.BoxSliderLayout_pager_animation,
				Transformer.Default.ordinal());
		mAutoCycle = attributes.getBoolean(R.styleable.BoxSliderLayout_auto_cycle,
				true);
		int visibility = attributes.getInt(
				R.styleable.BoxSliderLayout_indicator_visibility, 0);
		for (IPagerIndicator.IndicatorVisibility v : IPagerIndicator.IndicatorVisibility
				.values()) {
			if (v.ordinal() == visibility) {
				mIndicatorVisibility = v;
				break;
			}
		}
		mSliderAdapter = new ISliderAdapter(mContext);
		PagerAdapter wrappedAdapter = new IInfinitePagerAdapter(mSliderAdapter);

		mViewPager = (IInfiniteViewPager) findViewById(R.id.box_daimajia_slider_viewpager);
		mViewPager.setAdapter(wrappedAdapter);

		mViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_UP:
					recoverCycle();
					break;
				}
				return false;
			}
		});

		attributes.recycle();
		setPresetIndicator(PresetIndicators.Center_Bottom);
		setPresetTransformer(mTransformerId);
		setSliderTransformDuration(mTransformerSpan, null);
		setIndicatorVisibility(mIndicatorVisibility);
		if (mAutoCycle) {
			startAutoCycle();
		}
	}

	public void addOnPageChangeListener(
			IViewPagerEx.OnPageChangeListener onPageChangeListener) {
		if (onPageChangeListener != null) {
			mViewPager.addOnPageChangeListener(onPageChangeListener);
		}
	}

	public void removeOnPageChangeListener(
			IViewPagerEx.OnPageChangeListener onPageChangeListener) {
		mViewPager.removeOnPageChangeListener(onPageChangeListener);
	}

	public void setCustomIndicator(IPagerIndicator indicator) {
		if (mIndicator != null) {
			mIndicator.destroySelf();
		}
		mIndicator = indicator;
		mIndicator.setIndicatorVisibility(mIndicatorVisibility);
		mIndicator.setViewPager(mViewPager);
		mIndicator.redraw();
	}

	public <T extends IBaseSliderView> void addSlider(T imageContent) {
		mSliderAdapter.addSlider(imageContent);
	}

	private android.os.Handler mh = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			moveNextPosition(true);
		}
	};

	public void startAutoCycle() {
		startAutoCycle(mSliderDuration, mSliderDuration, mAutoRecover);
	}

	/**
	 * start auto cycle.
	 * 
	 * @param delay
	 *            delay time
	 * @param duration
	 *            animation duration time.
	 * @param autoRecover
	 *            if recover after user touches the slider.
	 */
	public void startAutoCycle(long delay, long duration, boolean autoRecover) {
		if (mCycleTimer != null)
			mCycleTimer.cancel();
		if (mCycleTask != null)
			mCycleTask.cancel();
		if (mResumingTask != null)
			mResumingTask.cancel();
		if (mResumingTimer != null)
			mResumingTimer.cancel();
		mSliderDuration = duration;
		mCycleTimer = new Timer();
		mAutoRecover = autoRecover;
		mCycleTask = new TimerTask() {
			@Override
			public void run() {
				mh.sendEmptyMessage(0);
			}
		};
		mCycleTimer.schedule(mCycleTask, delay, mSliderDuration);
		mCycling = true;
		mAutoCycle = true;
	}

	/**
	 * pause auto cycle.
	 */
	private void pauseAutoCycle() {
		if (mCycling) {
			mCycleTimer.cancel();
			mCycleTask.cancel();
			mCycling = false;
		} else {
			if (mResumingTimer != null && mResumingTask != null) {
				recoverCycle();
			}
		}
	}

	/**
	 * set the duration between two slider changes. the duration value must >=
	 * 500
	 * 
	 * @param duration
	 */
	public void setDuration(long duration) {
		if (duration >= 500) {
			mSliderDuration = duration;
			if (mAutoCycle && mCycling) {
				startAutoCycle();
			}
		}
	}

	/**
	 * stop the auto circle
	 */
	public void stopAutoCycle() {
		if (mCycleTask != null) {
			mCycleTask.cancel();
		}
		if (mCycleTimer != null) {
			mCycleTimer.cancel();
		}
		if (mResumingTimer != null) {
			mResumingTimer.cancel();
		}
		if (mResumingTask != null) {
			mResumingTask.cancel();
		}
		mAutoCycle = false;
		mCycling = false;
	}

	/**
	 * when paused cycle, this method can weak it up.
	 */
	private void recoverCycle() {
		if (!mAutoRecover || !mAutoCycle) {
			return;
		}

		if (!mCycling) {
			if (mResumingTask != null && mResumingTimer != null) {
				mResumingTimer.cancel();
				mResumingTask.cancel();
			}
			mResumingTimer = new Timer();
			mResumingTask = new TimerTask() {
				@Override
				public void run() {
					startAutoCycle();
				}
			};
			mResumingTimer.schedule(mResumingTask, 6000);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			pauseAutoCycle();
			break;
		}
		return false;
	}

	/**
	 * set ViewPager transformer.
	 * 
	 * @param reverseDrawingOrder
	 * @param transformer
	 */
	public void setPagerTransformer(boolean reverseDrawingOrder,
			IBaseTransformer transformer) {
		mViewPagerTransformer = transformer;
		mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
		mViewPager.setPageTransformer(reverseDrawingOrder,
				mViewPagerTransformer);
	}

	/**
	 * set the duration between two slider changes.
	 * 
	 * @param period
	 * @param interpolator
	 */
	public void setSliderTransformDuration(int period, Interpolator interpolator) {
		try {
			Field mScroller = IViewPagerEx.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			IFixedSpeedScroller scroller = new IFixedSpeedScroller(
					mViewPager.getContext(), interpolator, period);
			mScroller.set(mViewPager, scroller);
		} catch (Exception e) {

		}
	}

	/**
	 * preset transformers and their names
	 */
	public enum Transformer {
		Default("Default"), Accordion("Accordion"), Background2Foreground(
				"Background2Foreground"), CubeIn("CubeIn"), DepthPage(
				"DepthPage"), Fade("Fade"), FlipHorizontal("FlipHorizontal"), FlipPage(
				"FlipPage"), Foreground2Background("Foreground2Background"), RotateDown(
				"RotateDown"), RotateUp("RotateUp"), Stack("Stack"), Tablet(
				"Tablet"), ZoomIn("ZoomIn"), ZoomOutSlide("ZoomOutSlide"), ZoomOut(
				"ZoomOut");

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
	};

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
	 * set preset PagerTransformer via the name of transforemer.
	 * 
	 * @param transformerName
	 */
	public void setPresetTransformer(String transformerName) {
		for (Transformer t : Transformer.values()) {
			if (t.equals(transformerName)) {
				setPresetTransformer(t);
				return;
			}
		}
	}

	/**
	 * Inject your custom animation into PageTransformer, you can know more
	 * details in
	 * {@link IBaseAnimationInterface},
	 * and you can see a example in
	 * {@link IDescriptionAnimation}
	 * 
	 * @param animation
	 */
	public void setCustomAnimation(IBaseAnimationInterface animation) {
		mCustomAnimation = animation;
		if (mViewPagerTransformer != null) {
			mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
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
		IBaseTransformer t = null;
		switch (ts) {
		case Default:
			t = new IDefaultTransformer();
			break;
		case Accordion:
			t = new IAccordionTransformer();
			break;
		case Background2Foreground:
			t = new IBackgroundToForegroundTransformer();
			break;
		case CubeIn:
			t = new ICubeInTransformer();
			break;
		case DepthPage:
			t = new IDepthPageTransformer();
			break;
		case Fade:
			t = new IFadeTransformer();
			break;
		case FlipHorizontal:
			t = new IFlipHorizontalTransformer();
			break;
		case FlipPage:
			t = new IFlipPageViewTransformer();
			break;
		case Foreground2Background:
			t = new IForegroundToBackgroundTransformer();
			break;
		case RotateDown:
			t = new IRotateDownTransformer();
			break;
		case RotateUp:
			t = new IRotateUpTransformer();
			break;
		case Stack:
			t = new IStackTransformer();
			break;
		case Tablet:
			t = new ITabletTransformer();
			break;
		case ZoomIn:
			t = new IZoomInTransformer();
			break;
		case ZoomOutSlide:
			t = new IZoomOutSlideTransformer();
			break;
		case ZoomOut:
			t = new IZoomOutTransformer();
			break;
		}
		setPagerTransformer(true, t);
	}

	/**
	 * Set the visibility of the indicators.
	 * 
	 * @param visibility
	 */
	public void setIndicatorVisibility(
			IPagerIndicator.IndicatorVisibility visibility) {
		if (mIndicator == null) {
			return;
		}

		mIndicator.setIndicatorVisibility(visibility);
	}

	public IPagerIndicator.IndicatorVisibility getIndicatorVisibility() {
		if (mIndicator == null) {
			return mIndicator.getIndicatorVisibility();
		}
		return IPagerIndicator.IndicatorVisibility.Invisible;

	}

	/**
	 * get the {@link com.daimajia.slider.library.Indicators.IPagerIndicator}
	 * instance. You can manipulate the properties of the indicator.
	 * 
	 * @return
	 */
	public IPagerIndicator getPagerIndicator() {
		return mIndicator;
	}

	public enum PresetIndicators {
		Center_Bottom("Center_Bottom", R.id.box_default_center_bottom_indicator), Right_Bottom(
				"Right_Bottom", R.id.box_default_bottom_right_indicator), Left_Bottom(
				"Left_Bottom", R.id.box_default_bottom_left_indicator), Center_Top(
				"Center_Top", R.id.box_default_center_top_indicator), Right_Top(
				"Right_Top", R.id.box_default_center_top_right_indicator), Left_Top(
				"Left_Top", R.id.box_default_center_top_left_indicator);

		private final String name;
		private final int id;

		private PresetIndicators(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String toString() {
			return name;
		}

		public int getResourceId() {
			return id;
		}
	}

	public void setPresetIndicator(PresetIndicators presetIndicator) {
		IPagerIndicator pagerIndicator = (IPagerIndicator) findViewById(presetIndicator
				.getResourceId());
		setCustomIndicator(pagerIndicator);
	}

	@SuppressWarnings("unused")
	private IInfinitePagerAdapter getWrapperAdapter() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			return (IInfinitePagerAdapter) adapter;
		} else {
			return null;
		}
	}

	private ISliderAdapter getRealAdapter() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			return ((IInfinitePagerAdapter) adapter).getRealAdapter();
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

		return mViewPager.getCurrentItem() % getRealAdapter().getCount();

	}

	/**
	 * get current slider.
	 * 
	 * @return
	 */
	public IBaseSliderView getCurrentSlider() {

		if (getRealAdapter() == null)
			throw new IllegalStateException("You did not set a slider adapter");

		int count = getRealAdapter().getCount();
		int realCount = mViewPager.getCurrentItem() % count;
		return getRealAdapter().getSliderView(realCount);
	}

	/**
	 * remove the slider at the position. Notice: It's a not perfect method, a
	 * very small bug still exists.
	 */
	public void removeSliderAt(int position) {
		if (getRealAdapter() != null) {
			getRealAdapter().removeSliderAt(position);
			mViewPager.setCurrentItem(mViewPager.getCurrentItem(), false);
		}
	}

	/**
	 * remove all the sliders. Notice: It's a not perfect method, a very small
	 * bug still exists.
	 */
	public void removeAllSliders() {
		if (getRealAdapter() != null) {
			int count = getRealAdapter().getCount();
			getRealAdapter().removeAllSliders();
			// a small bug, but fixed by this trick.
			// bug: when remove adapter's all the sliders.some caching slider
			// still alive.
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + count,
					false);
		}
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
		int p = mViewPager.getCurrentItem() % getRealAdapter().getCount();
		int n = (position - p) + mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(n, smooth);
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

		mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, smooth);
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

		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, smooth);
	}

	public void moveNextPosition() {
		moveNextPosition(true);
	}
}
