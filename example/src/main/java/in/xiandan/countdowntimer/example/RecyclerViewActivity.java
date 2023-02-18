package in.xiandan.countdowntimer.example;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import in.xiandan.countdowntimer.CountDownTimerSupport;
import in.xiandan.countdowntimer.OnCountDownTimerListener;
import in.xiandan.countdowntimer.TimerState;

/**
 * @author xiandanin
 * created 2018/11/9 17:50
 */
public class RecyclerViewActivity extends AppCompatActivity {
    static final String MapTypeKey = "map_type";
    static final String MinKey = "minus_key";
    static final String SecKey = "seconds_key";
    static final String NotifyChannelID = "monster_refresh";
    static final int NOTIFICATION_ID = 100;

    private CountDownTimerSupport mTimer;
    private AppCompatImageView mapView;
    private AppCompatTextView tvTime;
    private RecyclerView rv;
    private ExampleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        MapType mapType = (MapType) getIntent().getSerializableExtra(MapTypeKey);
        int minutes = getIntent().getIntExtra(MinKey, 45);
        int seconds = getIntent().getIntExtra(SecKey, 0) % 60;
        mapView = (AppCompatImageView) findViewById(R.id.ivMap);
        tvTime = (AppCompatTextView) findViewById(R.id.tvTime);
        rv = (RecyclerView) findViewById(R.id.rv);

        List<WildMonsterTimeInfo> data = new ArrayList<>();
        int totalSeconds = Math.min((45 * 60) * 1000, (minutes * 60 + seconds) * 1000);
        int maxDuration = totalSeconds;

        int maxMinutes = minutes;
        if (seconds > 0) {
            maxMinutes += 1;
        }

        if (mapType == MapType.Desert) {
            mapView.setImageResource(R.mipmap.map_desert);
            for (int i = maxMinutes; i > 0; i--) {
                final int duration = i * 60 * 1000;
                List<WildMonster> monsters = new ArrayList<>();
                if (i % 2 == 0) {
                    monsters.add(WildMonster.MEDIUM_80);
                }
                if (i % 3 == 0 && i <= 42) {
                    monsters.add(WildMonster.MEDIUM_150);
                }
                if (monsters.size() > 0) {
                    WildMonsterTimeInfo info = new WildMonsterTimeInfo();
                    info.setDuration(duration);
                    info.setRemainingTime(totalSeconds - duration);
                    info.setState(TimerState.START);
                    info.setMonsters(monsters);
                    data.add(info);
                }

                // half minute
                int halfMin = i - 1;
                final int halfMinDuration = (halfMin * 60 + 30) * 1000;
                List<WildMonster> halfMinMonsters = new ArrayList<>();
                if (halfMin % 2 == 1 && halfMin <= 43) {
                    halfMinMonsters.add(WildMonster.HOME_80_1);
                }
                if (halfMin % 2 == 0 && halfMin <= 42) {
                    halfMinMonsters.add(WildMonster.HOME_80_2);
                }
                if (halfMin % 3 == 1 && halfMin <= 40) {
                    halfMinMonsters.add(WildMonster.HOME_150);
                }
                if (halfMinMonsters.size() > 0) {
                    WildMonsterTimeInfo halfMinInfo = new WildMonsterTimeInfo();
                    halfMinInfo.setDuration(halfMinDuration);
                    halfMinInfo.setRemainingTime(totalSeconds - halfMinDuration);
                    halfMinInfo.setState(TimerState.START);
                    halfMinInfo.setMonsters(halfMinMonsters);
                    data.add(halfMinInfo);
                }
            }
        }

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExampleAdapter(data);
        rv.setAdapter(adapter);

        DateFormat mDateFormat = new SimpleDateFormat("mm:ss");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        mTimer = new CountDownTimerSupport(maxDuration, 1000);
        mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("CountDownTimerSupport", "onTick : " + millisUntilFinished + "ms");
                int currentIndex = 0;
                final int maxIndex = adapter.getData().size();
                for (int i=0; i < maxIndex; i++) {
                    WildMonsterTimeInfo info = adapter.getData().get(i);
                    if (info.getState() == TimerState.FINISH) {
                        currentIndex = Math.min(i + 4, maxIndex);
                        continue;
                    }
                    long newTime = info.getRemainingTime() - 1000;
                    info.setRemainingTime(Math.max(0, newTime));
                    boolean isFinish = newTime <= 0;
                    if (isFinish) {
                        info.setState(TimerState.FINISH);
                        notifyNow(info.getMonstersDisplay());
                    }
                }
                tvTime.setText(String.format("比赛倒计时：%s", mDateFormat.format(millisUntilFinished)));
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(currentIndex);
            }

            @Override
            public void onFinish() {
                Log.d("CountDownTimerSupport", "onFinish");
            }

            @Override
            public void onCancel() {
                Log.d("CountDownTimerSupport", "onCancel");
            }
        });
        mTimer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mTimer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mTimer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.stop();
    }

    private void notifyNow(String message) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("update", "update", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("描述");
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "update");
        builder.setContentTitle("黄点即将刷新");
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.wf);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
