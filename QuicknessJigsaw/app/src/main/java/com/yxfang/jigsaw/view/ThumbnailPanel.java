package com.yxfang.jigsaw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/10/14.
 */
public class ThumbnailPanel extends LinearLayout
{
    private GamePanel gamePanel;
    private Bitmap thumbnaiBitmap;
    private int size;

    private boolean isInit = false;

    public ThumbnailPanel(Context context)
    {
        this(context, null);
    }

    public ThumbnailPanel(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ThumbnailPanel(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void init(GamePanel gamePanel, Bitmap thumbnaiBitmap)
    {
        this.gamePanel = gamePanel;
        this.thumbnaiBitmap = thumbnaiBitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit)
        {
            size = (int) (getMeasuredHeight() - gamePanel.getMeasuredHeight()) / 2;
            setBackgroundColor(Color.RED);
            addThumbnai();
            isInit = true;
        }

        setMeasuredDimension(size, size);
    }

    private void addThumbnai()
    {
        Log.e("", thumbnaiBitmap.getWidth() + " " + thumbnaiBitmap.getHeight());

        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        iv.setImageBitmap(ThumbnailUtils.extractThumbnail(thumbnaiBitmap, size, size));
        addView(iv, lp);
    }
}
