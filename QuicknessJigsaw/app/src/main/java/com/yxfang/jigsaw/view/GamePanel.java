package com.yxfang.jigsaw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yxfang.jigsaw.entity.ImageSlice;
import com.yxfang.jigsaw.util.ImageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 游戏的主面板
 * Created by Administrator on 2015/10/13.
 */
public class GamePanel extends RelativeLayout implements View.OnClickListener
{
    // 切片之间的间距
    private int margin;
    // 游戏面板的外间距
    private int padding;

    private boolean isMeasure;

    private int panelWidth;

    // 拼图原图
    private Bitmap srcBitmap;
    // 宫格数目
    private int gridNum = 3;

    // 每个切片的 宽度
    int childImageWidth;

    // 切割后的图片
    private List<ImageSlice> imageSlices;

    // 保存显示的图片容器
    private List<ImageView> ivSlices;

    private RelativeLayout animLayout;

    // 步数统计
    private int step;
    private boolean gameSuccess;
    private int level = 1;

    private static final int GAME_HANDLE_GAME_START = 0x00;
    private static final int GAME_HANDLE_GAME_SUCCESS = 0x01;
    private static final int GAME_HANDLE_GAME_STEP = 0x02;

    public GamePanel(Context context)
    {
        this(context, null);
    }

    public GamePanel(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GamePanel(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 游戏面板初始化
     */
    private void init()
    {
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        padding = minValue(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        panelWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if (!isMeasure)
        {
            //initChildImage();
            initChildImage();
            addChildImage();
            // 开始初始化
            isMeasure = true;

            gameHandler.sendEmptyMessage(GAME_HANDLE_GAME_START);
        }
        setMeasuredDimension(panelWidth, panelWidth);
    }

    /**
     * 初始化游戏数据
     *
     * @param imageSlices
     * @param gridNum
     */
    public void initGame(Bitmap srcBitmap, List<ImageSlice> imageSlices, int gridNum)
    {
        this.srcBitmap = srcBitmap;
        this.imageSlices = imageSlices;
        this.gridNum = gridNum;
    }

    /**
     * 初始化面板里面的图片切片
     */
    private void initChildImage()
    {
        if (imageSlices == null)
        {
            imageSlices = (List<ImageSlice>) ImageUtil.cutImage(srcBitmap, gridNum).getImageSlices();
        }

        sortChildImage();
    }

    private void sortChildImage()
    {
        Collections.sort(imageSlices, new Comparator<ImageSlice>()
        {
            @Override
            public int compare(ImageSlice imageSlice1, ImageSlice imageSlice2)
            {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 将切片添加到面板里面去
     */
    private void addChildImage()
    {
        // 每个切片宽度
        childImageWidth = (panelWidth - padding * 2 - (gridNum - 1) * margin) / gridNum;

        ivSlices = new ArrayList<ImageView>();

        for (int i = 0; i < imageSlices.size(); i++)
        {
            ImageView ivSlice = new ImageView(getContext());
            ivSlice.setId(i + 1);
            ivSlice.setImageBitmap(imageSlices.get(i).getImageBitmap());
            ivSlice.setOnClickListener(this);
            ivSlice.setTag(i + "_" + imageSlices.get(i).getIndex());

            ivSlices.add(ivSlice);

            Log.e("ivSlice Tag = ", ivSlice.getTag() + "");

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(childImageWidth, childImageWidth);
            // 判断是否是 第一列
            if (i % gridNum != 0)
            {
                lp.leftMargin = margin;
                lp.addRule(RelativeLayout.RIGHT_OF, ivSlices.get(i - 1).getId());
            }

            // 如果不是第一行
            if ((i + 1) > gridNum)
            {
                lp.topMargin = margin;
                lp.addRule(RelativeLayout.BELOW, ivSlices.get(i - gridNum).getId());
            }

            addView(ivSlice, lp);
        }
    }


    /**
     * 获取传入数组值里面的 最小值
     *
     * @param values
     * @return
     */
    private int minValue(int... values)
    {
        int min = values[0];
        for (int value : values)
        {
            if (value < min)
            {
                min = value;
            }
        }

        return min;
    }

    private ImageView firstIv;
    private ImageView secondIv;

    @Override
    public void onClick(View v)
    {
        if (isAnimRunning)
        {
            return;
        }

        if (v == firstIv)
        {
            firstIv.setColorFilter(null);
            firstIv = null;
            return;
        }

        if (firstIv == null)
        {
            firstIv = (ImageView) v;
            firstIv.setColorFilter(Color.parseColor("#8898C7D1"));
        }
        else
        {
            secondIv = (ImageView) v;
            changeImageSlice();
        }


    }

    private boolean isAnimRunning;

    /**
     * 切换选中的两个 切片
     */
    private void changeImageSlice()
    {
        firstIv.setColorFilter(null);

        // 初始化动画层
        initAnimLayout();

        final ImageView first = new ImageView(getContext());
        final Bitmap firstBitmap = imageSlices.get(findImageIdByTag((String) firstIv.getTag())).getImageBitmap();
        first.setImageBitmap(firstBitmap);

        LayoutParams lp = new LayoutParams(childImageWidth, childImageWidth);
        lp.leftMargin = firstIv.getLeft() - padding;
        lp.topMargin = firstIv.getTop() - padding;
        first.setLayoutParams(lp);
        animLayout.addView(first);

        final ImageView second = new ImageView(getContext());
        final Bitmap secondBitmap = imageSlices.get(
                findImageIdByTag((String) secondIv.getTag())).getImageBitmap();
        second.setImageBitmap(secondBitmap);

        LayoutParams lp2 = new LayoutParams(childImageWidth, childImageWidth);
        lp2.leftMargin = secondIv.getLeft() - padding;
        lp2.topMargin = secondIv.getTop() - padding;
        second.setLayoutParams(lp2);
        animLayout.addView(second);

        // 设置动画
        final TranslateAnimation anim = new TranslateAnimation(0, secondIv.getLeft()
                - firstIv.getLeft(), 0, secondIv.getTop() - firstIv.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                                                               -secondIv.getLeft() + firstIv.getLeft(), 0, -secondIv.getTop()
                                                                       + firstIv.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);

        anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                firstIv.setVisibility(View.INVISIBLE);
                secondIv.setVisibility(View.INVISIBLE);

                isAnimRunning = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                String firstTag = (String) firstIv.getTag();
                String secondTag = (String) secondIv.getTag();

                firstIv.setImageBitmap(secondBitmap);
                secondIv.setImageBitmap(firstBitmap);

                firstIv.setTag(secondTag);
                secondIv.setTag(firstTag);

                firstIv.setVisibility(View.VISIBLE);
                secondIv.setVisibility(View.VISIBLE);
                first.setVisibility(View.GONE);
                second.setVisibility(View.GONE);

                firstIv = null;
                secondIv = null;

                judgeSucess();

                isAnimRunning = false;
            }
        });


    }

    /**
     * 初始化动画层
     */
    private void initAnimLayout()
    {
        if (animLayout == null)
        {
            animLayout = new RelativeLayout(getContext());
            addView(animLayout);
        }
        else
        {
            animLayout.removeAllViews();
        }
    }

    /**
     * 判断是否拼图成功
     */
    private boolean judgeSucess()
    {
        boolean isSuccess = true;
        for (int i = 0; i < ivSlices.size(); i++)
        {
            ImageView ivSlice = ivSlices.get(i);
            int imageSliceIndex = findImageSliceIndexByTag((String) ivSlice.getTag());

            Log.e("", "Game idnex  = " + ivSlice.getTag());
            if (imageSliceIndex != i)
            {
                isSuccess = false;
                break;
            }
        }

        Log.d("", "Game State  = " + isSuccess);

        if (isSuccess)
        {
            gameSuccess = true;
            gameHandler.sendEmptyMessage(GAME_HANDLE_GAME_SUCCESS);
        }
        else
        {
            gameHandler.sendEmptyMessage(GAME_HANDLE_GAME_STEP);
        }

        return isSuccess;
    }

    public void nextLevel()
    {
        this.removeAllViews();
        firstIv = null;
        secondIv = null;
        animLayout = null;
        gameSuccess = false;
        imageSlices = null;
        // checkTimeEnable();
        gridNum++;
        level++;
        initChildImage();
        addChildImage();
    }

    /**
     * 根据iv tag 获取 imageSliceIndex
     *
     * @param tag
     * @return
     */
    private int findImageSliceIndexByTag(String tag)
    {
        int index = Integer.parseInt(tag.split("_")[1]);
        return index;
    }


    /**
     * 根据iv tag 获取 imageSlices 的下标
     *
     * @param tag
     * @return
     */
    private int findImageIdByTag(String tag)
    {
        int id = Integer.parseInt(tag.split("_")[0]);
        return id;
    }

    private Handler gameHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case GAME_HANDLE_GAME_START:
                    gameListener.onGameStart();
                    break;
                case GAME_HANDLE_GAME_SUCCESS:
                    gameListener.onGameSuccess(level);
                    break;
                case GAME_HANDLE_GAME_STEP:
                    gameListener.onStep(++step);
                    break;
            }
        }
    };

    private GameListener gameListener;

    public void setGameListener(GameListener gameListener)
    {
        this.gameListener = gameListener;
    }

    public interface GameListener
    {
        public void onGameStart();

        public void onGameSuccess(int level);

        public void onStep(int step);
    }
}
