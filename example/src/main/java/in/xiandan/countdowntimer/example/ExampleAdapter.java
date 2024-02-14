package in.xiandan.countdowntimer.example;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import in.xiandan.countdowntimer.TimerState;

/**
 * @author xiandanin
 * created 2018/11/9 17:52
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.Holder> {
    private List<WildMonsterTimeInfo> mData;
    private SimpleDateFormat mDateFormat;

    public ExampleAdapter(List<WildMonsterTimeInfo> data) {
        this.mData = data;
//        this.mDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        this.mDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_example, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final WildMonsterTimeInfo item = mData.get(position);
        holder.tv_index.setText(mDateFormat.format(item.getDuration()));
        holder.tv_duration.setText(item.getMonstersColorfulDisplay());
        final String remainingTime = mDateFormat.format(item.getRemainingTime());

        if (item.getState() == TimerState.START) {
            holder.tv_timer.setTextColor(holder.itemView.getResources().getColor(R.color.colorAccent));
            holder.tv_timer.setText(String.format("%s：%s", "黄点倒计时", remainingTime));
        } else if (item.getState() == TimerState.PAUSE) {
            holder.tv_timer.setTextColor(Color.GRAY);
            holder.tv_timer.setText(String.format("%s：%s", "倒计时暂停", remainingTime));
        } else {
            holder.tv_timer.setTextColor(Color.GRAY);
            holder.tv_timer.setText(String.format("%s：%s", "黄点已刷新", remainingTime));
        }
        if (item.getState() == TimerState.FINISH || item.isNotified()) {
            holder.rl_background.setBackgroundColor(Color.WHITE);
        } else {
            holder.rl_background.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<WildMonsterTimeInfo> getData() {
        return mData;
    }

    static class Holder extends RecyclerView.ViewHolder {

        private View rl_background;
        private TextView tv_duration;
        private TextView tv_timer;
        private TextView tv_index;

        private Holder(View itemView) {
            super(itemView);
            rl_background = itemView.findViewById(R.id.rlBackground);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_timer = (TextView) itemView.findViewById(R.id.tv_timer);
            tv_index = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }
}
