package in.xiandan.countdowntimer.example;

import static java.util.Locale.ENGLISH;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import in.xiandan.countdowntimer.CountDownTimerSupport;
import in.xiandan.countdowntimer.TimerState;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btnMinus0;
    AppCompatButton btnMinus1;
    AppCompatButton btnMinus2;
    AppCompatButton btnMinus3;
    AppCompatEditText etMinus;

    AppCompatButton btnSeconds0;
    AppCompatButton btnSeconds1;
    AppCompatButton btnSeconds2;
    AppCompatButton btnSeconds3;
    AppCompatEditText etSeconds;

    private int mMinus = 45;
    private int mSeconds = 0;
    private CountDownTimerSupport mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMinus0 = (AppCompatButton) findViewById(R.id.btnMinus0);
        btnMinus1 = (AppCompatButton) findViewById(R.id.btnMinus1);
        btnMinus2 = (AppCompatButton) findViewById(R.id.btnMinus2);
        btnMinus3 = (AppCompatButton) findViewById(R.id.btnMinus3);
        etMinus = (AppCompatEditText) findViewById(R.id.etMinus);

        btnSeconds0 = (AppCompatButton) findViewById(R.id.btnSeconds0);
        btnSeconds1 = (AppCompatButton) findViewById(R.id.btnSeconds1);
        btnSeconds2 = (AppCompatButton) findViewById(R.id.btnSeconds2);
        btnSeconds3 = (AppCompatButton) findViewById(R.id.btnSeconds3);
        etSeconds = (AppCompatEditText) findViewById(R.id.etSeconds);
        refreshMatchTime();
    }

    public void minusAdd10(View v) {
        mMinus = Math.min(45, mMinus + 10);
        refreshMatchTime();
    }

    public void minusAdd1(View v) {
        mMinus = Math.min(45, mMinus + 1);
        refreshMatchTime();
    }

    public void minusRemove1(View v) {
        mMinus = Math.max(5, mMinus - 1);
        refreshMatchTime();
    }

    public void minusRemove10(View v) {
        mMinus = Math.max(5, mMinus - 10);
        refreshMatchTime();
    }

    public void secondsAdd10(View v) {
        mSeconds = Math.min(59, mSeconds + 10);
        refreshMatchTime();
    }

    public void secondsAdd1(View v) {
        mSeconds = Math.min(59, mSeconds + 1);
        refreshMatchTime();
    }

    public void secondsRemove1(View v) {
        mSeconds = Math.max(0, mSeconds - 1);
        refreshMatchTime();
    }

    public void secondsRemove10(View v) {
        mSeconds = Math.max(0, mSeconds - 10);
        refreshMatchTime();
    }

    private void refreshMatchTime() {
        etMinus.setText(String.valueOf(mMinus));
        etSeconds.setText(String.format(ENGLISH, "%02d", mSeconds));
    }

    public void clickResetStart(View v) {
        mMinus = 45;
        mSeconds = 0;
        refreshMatchTime();
    }

    public void clickList(View v) {
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        intent.putExtra(RecyclerViewActivity.MapTypeKey, MapType.Desert);
        intent.putExtra(RecyclerViewActivity.MinKey, mMinus);
        intent.putExtra(RecyclerViewActivity.SecKey, mSeconds);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getStateText() {
        TimerState state = mTimer.getTimerState();
        if (TimerState.START == state) {
            return "正在倒计时";
        } else if (TimerState.PAUSE == state) {
            return "倒计时暂停";
        } else if (TimerState.FINISH == state) {
            return "倒计时闲置";
        }
        return "";
    }

}
