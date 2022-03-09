package com.ebook.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ebook.Fragment.FragmentHome;
import com.ebook.Fragment.FragmentPDFList;
import com.ebook.Fragment.FragmentSubject;
import com.ebook.Fragment.FragmentVideoList;
import com.ebook.R;


public class HomeAdapter extends BaseAdapter {

    private final String[] values;
    private final int[] images;
    Context context;
    Activity mActivity;
    LayoutInflater layoutInflater;

    public HomeAdapter(Activity activity, Context context, String[] values, int[] images) {
        this.context = context;
        this.mActivity = activity;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View rowView;

        rowView = layoutInflater.inflate(R.layout.home, null);
        holder.tv = rowView.findViewById(R.id.Menu_Name);
        holder.img = rowView.findViewById(R.id.Menu_Logo);

        holder.tv.setText(values[position]);
        holder.img.setImageResource(images[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    FragmentHome mFragmentHome = new FragmentHome();
                    setFragment(mFragmentHome);
                } else if (position == 1) {
                    FragmentSubject mFragmentSubject = new FragmentSubject();
                    String name = FragmentPDFList.class.getSimpleName();
                    Bundle bundle = new Bundle();
                    bundle.putString("name",name);
                    mFragmentSubject.setArguments(bundle);
                    setFragment(mFragmentSubject);
                }
                else if (position == 2) {
                    FragmentSubject mFragmentSubject = new FragmentSubject();
                    String name = FragmentVideoList.class.getSimpleName();
                    Bundle bundle = new Bundle();
                    bundle.putString("name",name);
                    mFragmentSubject.setArguments(bundle);
                    setFragment(mFragmentSubject);
                }
                else if (position == 3) {
                    FragmentSubject mFragmentSubject = new FragmentSubject();
                    String name = FragmentVideoList.class.getSimpleName();
                    setFragment(mFragmentSubject);
                }
                else if (position == 4) {

                }
                else if (position == 5) {

                }
            }
        });
        return rowView;
    }


    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

}