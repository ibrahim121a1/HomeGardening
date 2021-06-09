package com.soft.homegardening;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AlarmAdaptorClass extends FirebaseRecyclerAdapter<AlarmModelClass,AlarmAdaptorClass.AlarmViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AlarmAdaptorClass(@NonNull FirebaseRecyclerOptions<AlarmModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i, @NonNull AlarmModelClass alarmModelClass) {
        alarmViewHolder.time.setText(alarmModelClass.getTime());
        alarmViewHolder.label.setText(alarmModelClass.getLabel());

    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_alarm,parent,false);
        AlarmViewHolder alarmViewHolder=new AlarmViewHolder(view);
        return alarmViewHolder;
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView time,label;
        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.single_alarm_time);
            label=itemView.findViewById(R.id.labelTv);

        }
    }
}
