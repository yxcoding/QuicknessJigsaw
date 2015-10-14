package com.yxfang.jigsaw.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yxfang.jigsaw.entity.GameImage;
import com.yxfang.jigsaw.entity.ImageSlice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
public class ImageUtil
{
    /**
     * 根据原图切成gridNum * gridNum 的宫格切片
     *
     * @param srcBitmap
     * @param gridNum
     * @return
     */
    public static GameImage cutImage(Bitmap srcBitmap, int gridNum)
    {
        List<ImageSlice> imageSlices = new ArrayList<ImageSlice>();

        //srcBitmap = compressBmpFromBmp(srcBitmap);

        Bitmap bitmap = null;

        int imageWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());
        int childImageWidth = imageWidth / gridNum;

        GameImage gameImage = new GameImage();
        gameImage.setSrcBitmap(Bitmap.createBitmap(srcBitmap, 0, 0, imageWidth, imageWidth));


        for (int i = 0; i < gridNum; i++)
        {
            for (int j = 0; j < gridNum; j++)
            {
                int x = j * childImageWidth;
                int y = i * childImageWidth;
                bitmap = Bitmap.createBitmap(srcBitmap, x, y, childImageWidth, childImageWidth);

                ImageSlice imageSlice = new ImageSlice(j + i * gridNum, bitmap);
                imageSlices.add(imageSlice);
            }
        }

        gameImage.setImageSlices(imageSlices);

        return gameImage;
    }

    private static Bitmap compressBmpFromBmp(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > 100 && options != 10)
        {
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
}
