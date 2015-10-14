package com.yxfang.jigsaw;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yxfang.jigsaw.entity.GameImage;
import com.yxfang.jigsaw.util.ImageUtil;
import com.yxfang.jigsaw.util.TimeUtil;
import com.yxfang.jigsaw.view.GamePanel;
import com.yxfang.jigsaw.view.ThumbnailPanel;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements GamePanel.GameListener, View.OnClickListener
{
    private GamePanel gamePanel;
    private ThumbnailPanel thumbnailPanel;

    private TextView tvTime;
    private TextView tvStep;
    private TextView tvLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        setContentView(R.layout.activity_main);

        int src = getIntent().getIntExtra("src", R.drawable.src2);

        gamePanel = (GamePanel) findViewById(R.id.game_panel);
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), src);
        GameImage gameImage = ImageUtil.cutImage(srcBitmap, 2);
        gamePanel.initGame(srcBitmap, gameImage.getImageSlices(), 2);
        gamePanel.setGameListener(this);

        thumbnailPanel = (ThumbnailPanel) findViewById(R.id.humbnail_panel);
        thumbnailPanel.init(gamePanel, gameImage.getSrcBitmap());

        tvStep = (TextView) findViewById(R.id.tv_step);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLevel = (TextView) findViewById(R.id.tv_level);

        findViewById(R.id.btn_home).setOnClickListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startTimer();
    }

    @Override
    public void onGameStart()
    {
        startTimer();
    }

    @Override
    public void onStep(int step)
    {
        tvStep.setText("Step：" + step);
    }

    @Override
    public void onGameSuccess(int level)
    {
        time = 0;
        tvTime.setText("Time：" + TimeUtil.msToHms(time * 1000));
        tvStep.setText("Step：" + 0);

        tvLevel.setText("Level：" + (level + 1));
        gamePanel.nextLevel();
    }

    private TimerTask timerTask;
    private Timer timer;
    private boolean timerIsStart;
    private int time;

    private Handler timeHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            time++;
            tvTime.setText("Time：" + TimeUtil.msToHms(time * 1000));
        }
    };

    /**
     * 开始定时器
     */
    private void startTimer()
    {
        stopTimer();
        if (timerTask == null)
        {
            timerTask = new TimerTask()
            {

                @Override
                public void run()
                {
                    timeHandler.sendEmptyMessage(0);
                }
            };
        }

        if (timer == null)
        {
            timer = new Timer(true);
        }

        if (!timerIsStart)
        {
            timer.schedule(timerTask, 0, 1000);
            timerIsStart = true;
        }

    }

    /*
     * 停止定时器
     */
    private void stopTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }

        timerIsStart = false;
    }

    @Override
    public void onClick(View view)
    {
        finish();
    }
}
