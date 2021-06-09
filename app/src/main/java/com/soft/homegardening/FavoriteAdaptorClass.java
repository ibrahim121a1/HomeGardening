package com.soft.homegardening;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FavoriteAdaptorClass extends FirebaseRecyclerAdapter<ModelClass,FavoriteAdaptorClass.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public FavoriteAdaptorClass(@NonNull FirebaseRecyclerOptions<ModelClass> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ModelClass modelClass) {
        String key=getRef(i).getKey();
       Glide.with(context).load(modelClass.getUrl()).into(viewHolder.plantImage);
       viewHolder.plantName.setText(modelClass.getName());
       viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context,ViewFavPlantActivity.class);
               intent.putExtra("key",key);
               intent.putExtra("name","Favourite");
               Toast.makeText(context, "Hello!"+key, Toast.LENGTH_SHORT).show();
               context.startActivity(intent);

           }
       });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView plantName;
        ImageView plantImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage=itemView.findViewById(R.id.imageplantIV);
            plantName=itemView.findViewById(R.id.plantTV);
        }
    }
}
