package com.ebook.Adapter;

import android.content.Context;
import android.os.Bundle;
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

import com.ebook.Fragment.FragmentPDFViewer;
import com.ebook.Model.PDF;
import com.ebook.R;
import com.ebook.WebService.WebServices;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {


    Context context;
    List<PDF> dataModelList;
    String whichInstance;

    public DataAdapter(Context context, List<PDF> dataModelList, String name) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.whichInstance = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PDF dataModel = dataModelList.get(position);

        holder.PDFName.setText(dataModel.getName());
        holder.PDFDateTime.setText(dataModel.getDateTime());
        holder.BtnPDFView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PDFUrl = WebServices.BASE_URL_PDF + dataModel.getURL();
                FragmentPDFViewer mFragmentPDFViewer = new FragmentPDFViewer();
                Bundle bundle = new Bundle();
                bundle.putString("PDFUrl", PDFUrl);
                mFragmentPDFViewer.setArguments(bundle);
                setFragment(mFragmentPDFViewer);
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

        TextView PDFName;
        TextView PDFDateTime;
        Button BtnPDFView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            PDFName = itemView.findViewById(R.id.PDFName);
            BtnPDFView = itemView.findViewById(R.id.BtnPDFView);
            PDFDateTime = itemView.findViewById(R.id.PDFDateTime);
        }
    }
}
