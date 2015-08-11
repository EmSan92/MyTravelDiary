package se.lth.emelie.mytraveldiary;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emelie on 2015-08-10.
 */
public class ContentItem {
    private String date,caption,text1,text2,text3;
    private Bitmap im1,im2,im3;
    private ArrayList<Bitmap> imageList;
    private ArrayList<String> textList;

    public ContentItem(String date, String caption){
        this.date = date;
        this.caption = caption;


    }

    public String getDate(){
        return date;
    }
    public String getCaption(){
        return caption;
    }


    public ArrayList<String> getTexts(){
        return textList;
    }

    public ArrayList<Bitmap> getImages(){
        return imageList;

    }

    public void setImage(Bitmap image){
        imageList.add(image);

    }

    public void setText(String text){
        textList.add(text);

    }
}
