package com.netanya.hidayeichler.seasameopen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.netanya.hidayeichler.seasameopen.modols.Gate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GateListFragment extends Fragment {
    @BindView(R.id.rvGateList)
    RecyclerView rvUserLists;
    Unbinder unbinder;
    String s;


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
        Context context;
        private String gateName = null;
        AlertDialog n;


        public GateListAdapter(Query query, Fragment fragment) {
            super(Gate.class, R.layout.gate_list_item, GateListViewHolder.class, query);
            this.fragment = fragment;
        }


        @Override
        public GateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GateListViewHolder vh =  super.onCreateViewHolder(parent, viewType);
            vh.gateListFragment = fragment;
            context = parent.getContext();
            return vh;
        }

        @Override
        protected void populateViewHolder(final GateListViewHolder viewHolder, final Gate model, final int position) {
            viewHolder.tvGateName.setText(model.getName());
            viewHolder.btnAddGate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gateName = viewHolder.tvGateName.getText().toString();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userList = FirebaseDatabase.getInstance().getReference("UserGatesList").child(uid).child(gateName);
                    userList.setValue(model);
                    n = new AlertDialog.Builder(context)
                            .setTitle("Gate added")
                            .setMessage("You have added gate" + gateName + "to your list")
                            .setPositiveButton("Add antehr..", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    n.dismiss();
                                }
                            }).setCancelable(false)
                            .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    n.dismiss();

                                }
                            })
                            .create();
                    n.show();
                }
            });
            viewHolder.model = model;


        }

            //1)ViewHolder
            public static class GateListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


                //Properties:
                ImageView ivGateProfile;
                TextView tvGateName;
                Button btnAddGate;
                Fragment gateListFragment;
                Gate model;

                //Constructor:
                public GateListViewHolder(View itemView) {
                    super(itemView);
                    ivGateProfile = (ImageView) this.itemView.findViewById(R.id.ivGateProfile);
                    tvGateName = (TextView) this.itemView.findViewById(R.id.tvGateName);
                    btnAddGate = (Button) this.itemView.findViewById(R.id.btnAddGate);
                    itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                    if (v == btnAddGate) {
                        try {
                            int adapterPosition = getAdapterPosition();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
}
