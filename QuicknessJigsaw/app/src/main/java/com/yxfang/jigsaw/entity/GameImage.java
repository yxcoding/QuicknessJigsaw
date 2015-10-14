package com.yxfang.jigsaw.entity;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public class GameImage
{
    private Bitmap srcBitmap;
    private List<ImageSlice> imageSlices;


    public GameImage()
    {
    }


    public GameImage(Bitmap srcBitmap, List<ImageSlice> imageSlices)
    {
        this.srcBitmap = srcBitmap;
        this.imageSlices = imageSlices;
    }

    public Bitmap getSrcBitmap()
    {
        return srcBitmap;
    }

    public void setSrcBitmap(Bitmap srcBitmap)
    {
        this.srcBitmap = srcBitmap;
    }

    public List<ImageSlice> getImageSlices()
    {
        return imageSlices;
    }

    public void setImageSlices(List<ImageSlice> imageSlices)
    {
        this.imageSlices = imageSlices;
    }
}
