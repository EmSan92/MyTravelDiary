package se.lth.emelie.mytraveldiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Emelie on 2015-08-09.
 */
public class MyAdapter extends ArrayAdapter<ContentItem> {

    public MyAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MyAdapter(Context context, int resource, List<ContentItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.view_layout, null);
        }

        ContentItem p = getItem(position);

        if (p != null) {
            TextView date = (TextView) v.findViewById(R.id.dateview);
            TextView cap = (TextView) v.findViewById(R.id.caption);
            TextView tt1 = (TextView) v.findViewById(R.id.textview1);
            TextView tt2 = (TextView) v.findViewById(R.id.textview2);
            TextView tt3 = (TextView) v.findViewById(R.id.textview3);

            ImageView im1 = (ImageView) v.findViewById(R.id.image1);
            ImageView im2 = (ImageView) v.findViewById(R.id.image2);
            ImageView im3 = (ImageView) v.findViewById(R.id.image3);

            if (date != null) {
                date.setText(p.getDate());
            }
            if (cap != null){
                cap.setText(p.getCaption());
            }
                if (tt1 != null) {
                    tt1.setText(p.getTexts().get(0));
                    tt1.setVisibility(View.VISIBLE);
                }

                if (tt2 != null) {
                    tt2.setText(p.getTexts().get(1));
                    tt2.setVisibility(View.VISIBLE);
                }

                if (tt3 != null) {
                    tt3.setText(p.getTexts().get(2));
                    tt3.setVisibility(View.VISIBLE);
                }

                if (im1 != null) {
                    im1.setImageDrawable(p.getImages().get(0));
                    im1.setVisibility(View.VISIBLE);
                }
                if (im2 != null) {
                    im2.setImageDrawable(p.getImages().get(1));
                    im2.setVisibility(View.VISIBLE);
                }
                if (im3 != null) {
                    im3.setImageDrawable(p.getImages().get(2));
                    im3.setVisibility(View.VISIBLE);
                }



        }
        return v;
    }
}



