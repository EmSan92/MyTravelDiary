package se.lth.emelie.mytraveldiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Emelie on 2015-map-09.
 */
public class MyAdapter extends BaseAdapter {

    private List<ContentItem> items;
    private LayoutInflater mInflater;
    private int resource;
    private ContentItem p;

    public MyAdapter(Context context, int resource, List<ContentItem> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
        this.resource = resource;


    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ContentItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;

        if (v == null) {

            v = mInflater.inflate(R.layout.view_layout, parent, false);
            holder = new ViewHolder();
            holder.date = (TextView) v.findViewById(R.id.dateview);
            holder.cap = (TextView) v.findViewById(R.id.caption);
            holder.tt1 = (TextView) v.findViewById(R.id.textview1);
            holder.tt2 = (TextView) v.findViewById(R.id.textview2);
            holder.tt3 = (TextView) v.findViewById(R.id.textview3);
            holder.im1 = (ImageView) v.findViewById(R.id.image1);
            holder.im2 = (ImageView) v.findViewById(R.id.image2);
            holder.im3 = (ImageView) v.findViewById(R.id.image3);

            v.setTag(holder);



        } else {
            v = convertView;
            holder = (ViewHolder) v.getTag();
        }

        ContentItem p = getItem(position);



        holder.date.setText(p.getDate());
        holder.cap.setText(p.getCaption());

        System.out.println("textlist size " + p.getTexts().size());
        for(String s:p.getTexts()){
            System.out.println("textlist innehåll: "+s);
        }

        if (p.getTexts().size() > 0 && p.getTexts().get(0) != null) {
            holder.tt1.setText(p.getTexts().get(0));
            holder.tt1.setVisibility(View.VISIBLE);
        }if (p.getTexts().size() > 1 && p.getTexts().get(1) != null) {
            holder.tt2.setText(p.getTexts().get(1));
            holder.tt2.setVisibility(View.VISIBLE);
        }if (p.getTexts().size() > 2 && p.getTexts().get(2) != null) {
            holder.tt3.setText(p.getTexts().get(2));
            holder.tt3.setVisibility(View.VISIBLE);
        }

        System.out.println("size "+p.getImages().size());
        if (!p.getImages().isEmpty() && p.getImages().get(0) != null) {
            System.out.println("image 1 "+p.getImages().get(0));
            holder.im1.setImageBitmap(loadImageFromStorage(p.getImages().get(0)));
            holder.im1.setVisibility(View.VISIBLE);
        } if (p.getImages().size() > 1 && p.getImages().get(1) != null) {

            System.out.println("image 2 "+p.getImages().get(1));
            holder.im2.setImageBitmap(loadImageFromStorage(p.getImages().get(1)));
            holder.im2.setVisibility(View.VISIBLE);
        } if (p.getImages().size() > 2 && p.getImages().get(2) != null) {
            System.out.println("image 3 "+p.getImages().get(2));
            holder.im3.setImageBitmap(loadImageFromStorage(p.getImages().get(2)));
            holder.im3.setVisibility(View.VISIBLE);
        }


        return v;
    }

    private class ViewHolder {
        public ImageView im1, im2, im3;
        public TextView date, cap, tt1, tt2, tt3;
    }

    private Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        System.out.println("path:  "+path);
       // Log.d("The Path...", path);

        /*File f =  new File(path);
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        b = BitmapFactory.decodeFile(path);

       // Log.d("Bitmap image " ,b.toString());
        return b;
    }

}



