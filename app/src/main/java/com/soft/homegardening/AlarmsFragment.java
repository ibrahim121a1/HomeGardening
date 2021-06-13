package com.soft.homegardening;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soft.homegardening.fragments.AllFragments;
import com.soft.homegardening.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmsFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlarmsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmsFragment newInstance(String param1, String param2) {
        AlarmsFragment fragment = new AlarmsFragment();
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
    // declare variable
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<AlarmModelClass> options;
    FirebaseAuth auth;
    AlarmAdaptorClass alarmAdaptorClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_alarms, container, false);
        //initialize
        recyclerView=view.findViewById(R.id.alarm_rv);
        floatingActionButton=view.findViewById(R.id.add_new_alarm);
        auth=FirebaseAuth.getInstance();
        //show all alarm set by user
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(auth.getUid()).child("Alarm");
        options=new FirebaseRecyclerOptions.Builder<AlarmModelClass>().setQuery(databaseReference,AlarmModelClass.class).build();
        alarmAdaptorClass=new AlarmAdaptorClass(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(alarmAdaptorClass);
        alarmAdaptorClass.startListening();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddNewAlarmActivity.class));
            }
        });
        return view;

    }

    @Override
    public AllFragments getFragmentName() {
        return AllFragments.AlarmsFragment;
    }
}