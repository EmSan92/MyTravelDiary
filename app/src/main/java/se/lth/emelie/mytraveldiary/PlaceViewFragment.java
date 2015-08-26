package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class PlaceViewFragment extends Fragment {

/**
 * Att GÖRA!!:
 * Spara inlägg, dvs spara alla bilder och text till varje inlägg.
 * Kunna ladda in alla inlägg med rätt bilder och text.
 * Fixa så att vid fler bilder än en i ett inlägg så ska inte bara den sista tilllagda bilden visas flera gånger.
 * Fixa så man kan fylla i datum och caption.
 * Fixa små buggar, text vad som händer om man inte fyllt i nån text mm...
 * Ev fixa popupp rutor, tex vid edit mode om man inte fyllt i nått.
 **/


    private OnFragmentInteractionListener mListener;
    private MyAdapter myAdapter;
    private ArrayList<ContentItem> contentList;
    private FloatingActionButton fab2;
    private String date, capture;

    private ArrayList<String> pathList;

    public static PlaceViewFragment newInstance() {
        PlaceViewFragment fragment = new PlaceViewFragment();

        return fragment;
    }

    public PlaceViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_view, container, false);
        contentList = new ArrayList<ContentItem>();
        pathList = new ArrayList<String>();
        pathList = loadFromStorage();


/*
        ContentItem con = new ContentItem("2015-map-09","Forbidden City",null,null,null,null,null,null);
        con.setText("Hejlalalalala");
        con.setText("Hejlalalalala");
        con.setText("Hejlalalalala");
        for (int i =0;i<pathList.size();i++){
            if(!pathList.isEmpty()) {
                con.setImage1(loadImageFromStorage(pathList.get(0)));
            }
        }*/
        //Bitmap b = loadImageFromStorage(pathList.get(0));
        //contentList.add(con);

        ListView listView = (ListView) view.findViewById(R.id.listView2);

// get data from the table by the ListAdapter


        listView.setAdapter(myAdapter);
        myAdapter = new MyAdapter(this.getActivity(), R.layout.view_layout, contentList);
        //ContentItem con =  new ContentItem("hej",null,null,null,null,null,null,null);

        fab2 = (FloatingActionButton) view.findViewById(R.id.fabBtn2);
        final EditFragment frag = new EditFragment();
        frag.setPlaceViewFragment(this);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //contentList.add(new ContentItem(date, capture));
                final FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragmentPlaceViewContainer, frag, "NewFragmentTag");
                ft.commit();
            }
        });


        SharedPreferences pref = this.getActivity().getSharedPreferences("placelist", 0);
        pathList = loadFromStorage();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        try {
            File f = new File(path, "image.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)this.getActivity().findViewById(R.id.imgPicker);
            // img.setImageBitmap(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }


    public ArrayList<String> loadFromStorage() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("pathlist", 0);

        ArrayList<String> items = new ArrayList<String>();

        Set<String> set = prefs.getStringSet("pathlist", null);
        if (set != null && !set.isEmpty()) {
            for (String s : set) {
                try {
                    String string = s;
                    items.add(string);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return items;
    }

    public void addNewContent(ContentItem contentItem) {
        contentList.add(contentItem);
        System.out.println("Storlek på contentlist: " + contentList.size());
        myAdapter.notifyDataSetChanged();

    }




    }





