package com.tvz.server.tomislav.qwaiterserver;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Stack;

public class OrderStack extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;
    private Object mObject;
    private DataSnapshot mDataSnapshot;
    private TextView mCafeName;
    private TextView mCafeCategory;
    private ImageView mCafeAvatar;
    private ImageView mCafeBackground;
    private RecyclerView mRootView;


    private static Stack<Order> sOrderStack;
    private View mProfileImage;
    public static OrdersAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stack);
        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);



        //Listener for collapsing toolbar layout
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        sOrderStack =new Stack<>();
        mRootView = (RecyclerView) findViewById(R.id.my_recycler_view);


        sAdapter = new OrdersAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(sAdapter);

        mCafeName = (TextView) findViewById(R.id.cafe_name);
        mCafeCategory=(TextView)findViewById(R.id.cafe_category);
        mCafeAvatar = (ImageView) findViewById(R.id.materialup_profile_image);
        mCafeBackground = (ImageView) findViewById(R.id.materialup_profile_backdrop);
        setImagesAndUserProfile();
    }

    private void setImagesAndUserProfile() {
        mCafeName.setText(MainActivity.sObject.getName());
        mCafeCategory.setText(MainActivity.sObject.getCategory());
        Glide.with(this).load(MainActivity.sObject.getAvatarImage()).into(mCafeAvatar);
        Glide.with(this).load(MainActivity.sObject.getBackgroundImage()).into(mCafeBackground);
    }

    private static void getOrders(){

        DatabaseReference reference = MainActivity.sObject.getReference();

        reference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sOrderStack.push(dataSnapshot.getValue(Order.class));
                sAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Hiding and showing avatar while collapsing toolbar
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    private static class OrdersAdapter extends RecyclerView.Adapter<OrderViewHolder> {
        private Context mContext;
        OrdersAdapter() {
        }

        @Override
        public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            mContext=viewGroup.getContext();
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_card, viewGroup, false);
            return new OrderViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderViewHolder orderViewHolder, int i) {
            Order item = sOrderStack.get(i);
            String text = item.getUserID()+" has ordered:\n";
            for (FoodDrinkModel listItem:item.getOrderList()){
                text+=listItem.getName()+" x"+listItem.getQuantity()+"\n";
            }
            text+="Table No."+item.getTable();
            orderViewHolder.itemName.setText(text);

        }

        @Override
        public int getItemCount() {
            return sOrderStack.size();
        }
    }

    private static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        OrderViewHolder(View itemView) {
            super(itemView);
            itemName=(TextView) itemView.findViewById(R.id.list_item_name);
        }
    }
}
