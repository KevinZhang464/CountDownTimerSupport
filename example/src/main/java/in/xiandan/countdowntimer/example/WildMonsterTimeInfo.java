package in.xiandan.countdowntimer.example;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import in.xiandan.countdowntimer.TimerState;

/**
 * @author xiandanin
 * created 2018/11/11 11:19
 */
public class WildMonsterTimeInfo {
    private long duration;
    private long remainingTime;
    private TimerState state;
    private List<WildMonster> monsters;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public TimerState getState() {
        return state;
    }

    public void setState(TimerState state) {
        this.state = state;
    }

    public List<WildMonster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<WildMonster> monsters) {
        this.monsters = monsters;
    }

    public String getMonstersDisplay() {
        List<String> monsterNames = new ArrayList<>();
        for (WildMonster wildMonster : getMonsters()) {
            switch (wildMonster) {
                case MEDIUM_150:
                    monsterNames.add("中大");
                    break;
                case MEDIUM_80:
                    monsterNames.add("中小");
                    break;
                case HOME_150:
                    monsterNames.add("家大");
                    break;
                case HOME_80_1:
                    monsterNames.add("家小1");
                    break;
                case HOME_80_2:
                    monsterNames.add("家小2");
                    break;
                default:
                    break;
            }
        }
        return String.join(",", monsterNames);
    }

    private SpannableString getColorSpan(ForegroundColorSpan span, String text) {
        SpannableString string = new SpannableString(text);
        string.setSpan(span, 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public SpannableStringBuilder getMonstersColorfulDisplay() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan yellow = new ForegroundColorSpan(Color.YELLOW);
        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan green = new ForegroundColorSpan(Color.GREEN);
        for (WildMonster wildMonster : getMonsters()) {
            switch (wildMonster) {
                case MEDIUM_150:
                    SpannableString m150 = getColorSpan(red, "中路大黄点 ");
                    builder.append(m150);
                    break;
                case MEDIUM_80:
                    SpannableString m80 = getColorSpan(yellow, "中路小黄点 ");
                    builder.append(m80);
                    break;
                case HOME_150:
                    SpannableString h150 = getColorSpan(red, "家里大黄点 ");
                    builder.append(h150);
                    break;
                case HOME_80_1:
                    SpannableString h80_1 = getColorSpan(green, "家里小黄点1 ");
                    builder.append(h80_1);
                    break;
                case HOME_80_2:
                    SpannableString h80_2 = getColorSpan(green, "家里小黄点2 ");
                    builder.append(h80_2);
                    break;
                default:
                    break;
            }
        }
        return builder;
    }
}