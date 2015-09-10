package se.lth.emelie.mytraveldiary;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emelie on 2015-map-10.
 */
public class ContentItem {
    private String date, caption, text1, text2, text3;
    private String im1, im2, im3;
    private ArrayList<String> imageList, imageNames;
    private ArrayList<String> textList;


    public ContentItem(String date, String caption, ArrayList<String> textList, ArrayList<String> images) {
        this.date = date;
        this.caption = caption;


        this.textList = textList;
        imageList = images;


    }

    public String getDate() {
        return date;
    }

    public String getCaption() {
        return caption;
    }


    public ArrayList<String> getTexts() {
        return textList;
    }

    public ArrayList<String> getImages() {
        return imageList;

    }
/*
    public ArrayList<String> getImageNames() {
        return imageNames;

    }
*/
}
