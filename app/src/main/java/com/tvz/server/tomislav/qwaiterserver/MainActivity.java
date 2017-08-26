package com.tvz.server.tomislav.qwaiterserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
         {

    private RecyclerView mRootView;
    public static MainAdapter mAdapter;
    public static List<Object> sObjects;
    public static List<Object> sObjects1;
    public static  Object sObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sObjects=new ArrayList<>();
        sObjects1=new ArrayList<>();


        mRootView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mAdapter = new MainAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(mAdapter);
        mRootView.addOnItemTouchListener(new RecyclerTouchListener(this, mRootView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                sObject = sObjects.get(position);
                Intent intent = new Intent(getBaseContext(),OrderStack.class);
                startActivity(intent);
            }
        }) );

        getPlaces();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private static class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {
        private Context mContext;
        MainAdapter() {
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            mContext=viewGroup.getContext();
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_place, viewGroup, false);
            return new MainViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MainViewHolder mainViewHolder, int i) {
            Object item = sObjects.get(i);

            mainViewHolder.placeItemName.setText(item.getName());
            Glide.with(mContext).load(item.getBackgroundImage()).into(mainViewHolder.placeImage);
        }

        @Override
        public int getItemCount() {
            return sObjects.size();
        }
    }

    private static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView placeItemName;
        ImageView placeImage;
        MainViewHolder(View itemView) {
            super(itemView);
           placeItemName=(TextView) itemView.findViewById(R.id.list_item_place_name);
            placeImage=(ImageView) itemView.findViewById(R.id.list_item_image);
        }
    }

    public  void getPlaces(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("bars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Object tempObject=new Object();
                    tempObject.setName(snapshot.child("meta-data").child("name").getValue(String.class));
                    tempObject.setCategory(snapshot.child("meta-data").child("objectCategory").getValue(String.class));
                    tempObject.setAvatarImage(snapshot.child("meta-data").child("objectAvatarImageURL").getValue(String.class));
                    tempObject.setBackgroundImage(snapshot.child("meta-data").child("objectBackgroundImageURL").getValue(String.class));
                    tempObject.setReference(FirebaseDatabase.getInstance().getReference().child("bars").child(snapshot.getKey()).child("orders"));
                    sObjects.add(tempObject);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Object tempObject=new Object();
                    tempObject.setName(snapshot.child("meta-data").child("name").getValue(String.class));
                    tempObject.setCategory(snapshot.child("meta-data").child("objectCategory").getValue(String.class));
                    tempObject.setAvatarImage(snapshot.child("meta-data").child("objectAvatarImageURL").getValue(String.class));
                    tempObject.setBackgroundImage(snapshot.child("meta-data").child("objectBackgroundImageURL").getValue(String.class));
                    tempObject.setReference(FirebaseDatabase.getInstance().getReference().child("bars").child(snapshot.getKey()).child("orders"));
                    sObjects.add(tempObject);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
