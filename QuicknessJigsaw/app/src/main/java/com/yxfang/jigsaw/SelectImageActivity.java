package com.yxfang.jigsaw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/10/14.
 */
public class SelectImageActivity extends Activity
{
    GridView gv;

    private int[] imgs = {R.drawable.src1, R.drawable.src2, R.drawable.src3,
            R.drawable.src4, R.drawable.src5,
            R.drawable.src6, R.drawable.src7, R.drawable.src8};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        setContentView(R.layout.activity_select_image);

        gv = (GridView) findViewById(R.id.gv);
        gv.setAdapter(new ImageGridAdapter());
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(SelectImageActivity.this, MainActivity.class);
                intent.putExtra("src", imgs[i]);
                startActivity(intent);
            }
        });
    }

    class ImageGridAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return imgs.length;
        }

        @Override
        public Object getItem(int i)
        {
            return imgs[i];
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ImageView iv;
            if (view == null)
            {
                iv = new ImageView(SelectImageActivity.this);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                iv.setLayoutParams(lp);
                view = iv;
            }
            else
            {
                iv = (ImageView) view;
            }
            iv.setBackgroundResource(imgs[i]);
            return iv;
        }
    }


}
