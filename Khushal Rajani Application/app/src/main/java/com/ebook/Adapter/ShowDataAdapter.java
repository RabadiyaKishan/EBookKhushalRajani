package com.ebook.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ebook.Fragment.FragmentPDFList;
import com.ebook.Fragment.FragmentVideoList;
import com.ebook.Model.Subjects;
import com.ebook.R;
import com.ebook.WebService.WebServices;

import java.util.List;

public class ShowDataAdapter extends RecyclerView.Adapter<ShowDataAdapter.MyViewHolder> {


    Context context;
    List<Subjects> dataModelList;
    String whichInstance;

    public ShowDataAdapter(Context context, List<Subjects> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Subjects dataModel = dataModelList.get(position);

        Glide.with(context)
                .load(WebServices.BASE_URL_IMAGE + dataModel.getImage())
                .placeholder(R.drawable.app_logo)
                .into(holder.SubjectImage);

        holder.SubjectName.setText(dataModel.getName());
        holder.SubjectDateTime.setText(dataModel.getDateTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SubjectID = dataModel.getID();
                if (whichInstance.equals("FragmentPDFList")) {
                    FragmentPDFList mFragmentPDFList = new FragmentPDFList();
                    Bundle bundle = new Bundle();
                    bundle.putString("SubjectID", SubjectID);
                    mFragmentPDFList.setArguments(bundle);
                    setFragment(mFragmentPDFList);
                } else if (whichInstance.equals("FragmentVideoList")) {
                    FragmentVideoList mFragmentVideoList = new FragmentVideoList();
                    Bundle bundle = new Bundle();
                    bundle.putString("SubjectID", SubjectID);
                    mFragmentVideoList.setArguments(bundle);
                    setFragment(mFragmentVideoList);
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

        ImageView SubjectImage;
        TextView SubjectName;
        TextView SubjectDateTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            SubjectImage = itemView.findViewById(R.id.SubjectImage);
            SubjectName = itemView.findViewById(R.id.SubjectName);
            SubjectDateTime = itemView.findViewById(R.id.SubjectDateTime);
        }
    }
}
