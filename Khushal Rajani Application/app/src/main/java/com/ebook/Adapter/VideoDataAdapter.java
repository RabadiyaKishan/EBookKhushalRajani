package com.ebook.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ebook.Model.Video;
import com.ebook.R;

import java.util.List;

public class VideoDataAdapter extends RecyclerView.Adapter<VideoDataAdapter.MyViewHolder> {


    Context context;
    List<Video> dataModelList;
    String whichInstance;

    public VideoDataAdapter(Context context, List<Video> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_data, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Video dataModel = dataModelList.get(position);

        holder.VideoName.setText(dataModel.getName());
        holder.VideoDateTime.setText(dataModel.getDateTime());
        holder.BtnVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String VideoURL = dataModel.getURL();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VideoURL));
                    context.startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView VideoName;
        TextView VideoDateTime;
        Button BtnVideoView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            VideoName = itemView.findViewById(R.id.VideoName);
            BtnVideoView = itemView.findViewById(R.id.BtnVideoView);
            VideoDateTime = itemView.findViewById(R.id.VideoDateTime);
        }
    }
}
