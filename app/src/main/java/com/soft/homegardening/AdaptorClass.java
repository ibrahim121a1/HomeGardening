package com.soft.homegardening;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AdaptorClass extends FirebaseRecyclerAdapter<ModelClass, AdaptorClass.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    String plant;

    public AdaptorClass(@NonNull FirebaseRecyclerOptions<ModelClass> options, Context context, String plant) {

        super(options);
        this.context = context;
        this.plant = plant;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ModelClass modelClass) {
        Glide.with(context).load(modelClass.getUrl()).into(viewHolder.plantIV);
        viewHolder.plantnameTV.setText(modelClass.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = getRef(i).getKey();
                String name = viewHolder.plantnameTV.getText().toString();
                Intent intent = new Intent(context, ViewDetailActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("plant", plant);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView plantIV;
        TextView plantnameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantnameTV = itemView.findViewById(R.id.plantTV);
            plantIV = itemView.findViewById(R.id.imageplantIV);
        }
    }
}
