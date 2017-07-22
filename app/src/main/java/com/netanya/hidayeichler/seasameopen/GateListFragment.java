package com.netanya.hidayeichler.seasameopen;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.netanya.hidayeichler.seasameopen.modols.Gate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GateListFragment extends Fragment {
    @BindView(R.id.rvGateList)
    RecyclerView rvUserLists;
    Unbinder unbinder;

    public GateListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gate_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){
            Log.e("Hiday", "Null user");
            return view;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Gates"); //TODO: Handle nulls

        rvUserLists.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserLists.setAdapter(new GateListAdapter(ref, this));

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //2)FirebaseRecyclerAdapter
    public static class GateListAdapter extends FirebaseRecyclerAdapter<Gate, GateListAdapter.GateListViewHolder> {
        Fragment fragment;

        public GateListAdapter(Query query, Fragment fragment) {
            super(Gate.class, R.layout.gate_list_item, GateListViewHolder.class, query);
            this.fragment = fragment;
        }

        @Override
        public GateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GateListViewHolder vh =  super.onCreateViewHolder(parent, viewType);
            vh.gateListFragment = fragment;
            return vh;
        }

        @Override
        protected void populateViewHolder(GateListViewHolder viewHolder, Gate model, int position) {
            viewHolder.tvGateName.setText(model.getName());
            viewHolder.model = model;
        }

            //1)ViewHolder
            public static class GateListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

                //Properties:
                ImageView ivGateProfile;
                TextView tvGateName;
                Button btnDeleteGate;
                Fragment gateListFragment;
                Gate model;

                //Constructor:
                public GateListViewHolder(View itemView) {
                    super(itemView);
                    ivGateProfile = (ImageView) itemView.findViewById(R.id.ivGateProfile);
                    tvGateName = (TextView) itemView.findViewById(R.id.tvGateName);
                    btnDeleteGate = (Button) itemView.findViewById(R.id.btnDeleteGate);
                    btnDeleteGate.setOnClickListener(this);
                    itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                    if (v == btnDeleteGate) {
                        try {
                            //TODO make a dialig
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
}
