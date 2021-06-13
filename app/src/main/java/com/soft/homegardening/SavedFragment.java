package com.soft.homegardening;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soft.homegardening.fragments.AllFragments;
import com.soft.homegardening.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //declare variable
    RecyclerView recyclerView;
    SavedAdpatorClass savedAdpatorClass;
    FirebaseRecyclerOptions<ModelClass> options;
    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_saved, container, false);
        //initialize variable
        recyclerView=view.findViewById(R.id.rv);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("Save Data");
        options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReference, ModelClass.class).build();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        savedAdpatorClass=new SavedAdpatorClass(options,getActivity());
        recyclerView.setAdapter(savedAdpatorClass);
        savedAdpatorClass.startListening();
        floatingActionButton=view.findViewById(R.id.fbtn);
        //move to save data detail activity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SavedDataDetailActivity.class));
            }
        });
        return view;
    }

    @Override
    public AllFragments getFragmentName() {
        {
            return AllFragments.SavedFragment;
        }
    }
}