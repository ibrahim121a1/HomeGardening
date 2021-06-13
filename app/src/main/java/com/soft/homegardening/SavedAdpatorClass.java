package com.soft.homegardening;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;


public class SavedAdpatorClass extends FirebaseRecyclerAdapter<ModelClass, SavedAdpatorClass.SavedViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;

    public SavedAdpatorClass(@NonNull FirebaseRecyclerOptions<ModelClass> options, Context context) {

        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SavedViewHolder savedViewHolder, int i, @NonNull ModelClass modelClass) {
        Glide.with(context).load(modelClass.getUrl()).into(savedViewHolder.plantImage);//load image of plant
        String name = modelClass.getName(); //get plant name
        savedViewHolder.plantName.setText(modelClass.getName());//set plant name
        String key = getRef(i).getKey();
        savedViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() { //move to next activity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SavedDataActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_line, parent, false);
        SavedViewHolder savedViewHolder = new SavedViewHolder(view);
        return savedViewHolder;
    }


    class SavedViewHolder extends RecyclerView.ViewHolder {
        TextView plantName;
        CircularImageView plantImage;
        CardView relativeLayout;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.single_lineIV);
            plantName = itemView.findViewById(R.id.single_lineTV);
            relativeLayout = itemView.findViewById(R.id.rvsave);
        }
    }
}
