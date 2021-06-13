package com.soft.homegardening;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soft.homegardening.activities.DashBoardActivity;
import com.soft.homegardening.fragments.AllFragments;
import com.soft.homegardening.fragments.BaseFragment;

import static java.lang.String.format;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGardenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGardenFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyGardenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyGardenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyGardenFragment newInstance(String param1, String param2) {
        MyGardenFragment fragment = new MyGardenFragment();
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

    @Override
    public AllFragments getFragmentName() {
        return AllFragments.MyGardenFragment;
    }

    //declare variable
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ModelClass> options;
    DatabaseReference databaseReference;
    MyGardenAdaptor adaptorClass;
    TextView count;
    FirebaseAuth firebaseAuth;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_garden, container, false);
        //initialize variable
        recyclerView = view.findViewById(R.id.rv);
        firebaseAuth=FirebaseAuth
                .getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden");

        options = new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReference, ModelClass.class).build();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adaptorClass=new MyGardenAdaptor(options,getContext());
        recyclerView.setAdapter(adaptorClass);
        count = view.findViewById(R.id.numtv);
        floatingActionButton=view.findViewById(R.id.fbtn);
        //click to add plant in my garden
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddButtonActivity.class));
            }
        });
        // Get the amount of plants in My Garden
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adaptorClass.startListening();




        return view;


    }
}