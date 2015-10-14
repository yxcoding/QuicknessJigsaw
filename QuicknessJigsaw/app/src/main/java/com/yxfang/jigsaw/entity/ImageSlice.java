package com.yxfang.jigsaw.entity;

import android.graphics.Bitmap;

/**
 * 图片切片实体
 * Created by Administrator on 2015/10/13.
 */
public class ImageSlice
{
    private int index;
    private Bitmap imageBitmap;

    public ImageSlice()
    {
    }

    public ImageSlice(int index, Bitmap imageBitmap)
    {
        this.index = index;
        this.imageBitmap = imageBitmap;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }
}
