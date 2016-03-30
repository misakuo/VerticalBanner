package com.moxun.verticalbanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.vbanner.VBAdapter;
import com.moxun.vbanner.VerticalBannerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerticalBannerView banner = (VerticalBannerView) findViewById(R.id.vbanner);
        banner.setVisibleItemCount(3);
        banner.setAutoScrollDelay(3000);
        banner.setSwitchMode(VerticalBannerView.MODE_SCROLL_OUT);
        banner.start();
        VBAdapter<String> adapter = new VBAdapter<String>() {
            List<String> list = new ArrayList<>();

            {
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
                list.add("6");
            }

            @NonNull
            @Override
            public List<String> getDataSet() {
                return list;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(parent.getContext());
                return new ItemHolder(textView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ItemHolder)holder).tv.setText("Item:" + list.get(position));
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class ItemHolder extends RecyclerView.ViewHolder {
                TextView tv;
                public ItemHolder(View itemView) {
                    super(itemView);
                    tv = (TextView) itemView;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("TAG","Clicked.");
                        }
                    });
                }
            }
        };
        banner.setAdapter(adapter);
    }
}
