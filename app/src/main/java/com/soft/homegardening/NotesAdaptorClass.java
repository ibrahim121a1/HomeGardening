package com.soft.homegardening;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotesAdaptorClass extends FirebaseRecyclerAdapter<NotesModelClass,NotesAdaptorClass.NotesRecyclerView> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesAdaptorClass(@NonNull FirebaseRecyclerOptions<NotesModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesRecyclerView notesRecyclerView, int i, @NonNull NotesModelClass notesModelClass) {
        notesRecyclerView.notesTV.setText(notesModelClass.getNotes());
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
