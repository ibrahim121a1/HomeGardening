package com.soft.homegardening;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyGardenAdaptor extends FirebaseRecyclerAdapter<ModelClass, MyGardenAdaptor.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;

    public MyGardenAdaptor(@NonNull FirebaseRecyclerOptions<ModelClass> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ModelClass modelClass) {
        String key=getRef(i).getKey();
        Glide.with(context).load(modelClass.getUrl()).into(viewHolder.plantImage);//load plant image
        viewHolder.plantName.setText(modelClass.getName());//set plant name
        viewHolder.lastseenTV.setText(modelClass.getSeen());//set last seen of plant
        Calendar calender=Calendar.getInstance();
        String saveCurrentTime;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentTime = currentTime.format(calender.getTime());//get current time of calander
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ViewFavPlantActivity.class);
                intent.putExtra("name","My Garden");
                intent.putExtra("key",key);
                intent.putExtra("seen",saveCurrentTime);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_garden, parent, false);
        return new ViewHolder(view);
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        //declare variable
        TextView plantName;
        ImageView plantImage;
        TextView lastseenTV;
        CardView relativeLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            //initialize variable
            plantImage = itemView.findViewById(R.id.gardenIV);
            plantName = itemView.findViewById(R.id.plant_name_TV);
            lastseenTV = itemView.findViewById(R.id.lastseenTV);
            relativeLayout=itemView.findViewById(R.id.item);
        }
    }
}
