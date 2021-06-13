package com.soft.homegardening;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotesAdaptorClass extends FirebaseRecyclerAdapter<NotesModelClass,NotesAdaptorClass.NotesRecyclerView> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Context context;
    String name;
    public NotesAdaptorClass(@NonNull FirebaseRecyclerOptions<NotesModelClass> options, String name, Context context) {
        super(options);

        this.context=context;
        this.name=name;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesRecyclerView notesRecyclerView, int i, @NonNull NotesModelClass notesModelClass) {
        notesRecyclerView.notesTV.setText(notesModelClass.getNotes());
        String key=getRef(i).getKey();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
       notesRecyclerView.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               AlertDialog.Builder builder=new AlertDialog.Builder(context);
               builder.setTitle("Do you Want to delete Notes?");

               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       databaseReference=FirebaseDatabase.getInstance().getReference("Member")
                               .child(firebaseAuth.getUid()).child("My Garden").child(name).child("My Notes")
                               .child(key);
                       databaseReference.removeValue();
                   }
               });
               builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
               builder.show();
               return true;
           }
       });

    }

    @NonNull
    @Override
    public NotesRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note,parent,false);
        NotesRecyclerView notesRecyclerView=new NotesRecyclerView(view);
        return notesRecyclerView;
    }

    class NotesRecyclerView extends RecyclerView.ViewHolder {
        TextView notesTV;
        public NotesRecyclerView(@NonNull View itemView) {
            super(itemView);
            notesTV=itemView.findViewById(R.id.single_note_TV);
        }
    }
}
