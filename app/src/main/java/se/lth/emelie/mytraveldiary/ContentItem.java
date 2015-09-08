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


    public ContentItem(String date, String caption, String text1, String text2, String text3, String im1, String im2, String im3) {
        this.date = date;
        this.caption = caption;
        imageList = new ArrayList<String>(3);
        textList = new ArrayList<String>(3);
        //this.imageNames = imageNames;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.im1 = im1;
        this.im2 = im2;
        this.im3 = im3;
        imageList.add(im1);
        imageList.add(im2);
        imageList.add(im3);
        textList.add(text1);
        textList.add(text2);
        textList.add(text3);

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
