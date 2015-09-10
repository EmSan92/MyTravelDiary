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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PlaceViewFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private MyAdapter myAdapter;
    private ArrayList<ContentItem> contentList;
    private FloatingActionButton fab2;
    private String date, capture;
    private Activity activity;
    private String desination;
    private Button deletePost;
    private ListView listView;

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
        activity = this.getActivity();

        desination = getArguments().getString("destination");

        loadInfo();

        listView = (ListView) view.findViewById(R.id.listView2);

        // get data from the table by the ListAdapter
        myAdapter = new MyAdapter(this.getActivity(), R.layout.view_layout, contentList);
        listView.setAdapter(myAdapter);



        /**
         *When click on the floating action button, this code will change fragment to editfragment.
         *
         **/
        fab2 = (FloatingActionButton) view.findViewById(R.id.fabBtn2);
        final EditFragment editfrag = new EditFragment();
        editfrag.setPlaceViewFragment(this);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragmentContainer, editfrag, "tag");
                Bundle args = new Bundle();
                args.putString("destination", desination);
                editfrag.setArguments(args);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });

/**
 *Remove a contentitem from the contentList.
 * Does not work!!!!!!!!!!!!!!!
 **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contentList.remove(position);

                myAdapter.notifyDataSetChanged();

            }
        });


        return view;
    }


    /**
     * Change the Title to the destination
     **/
    public void onResume() {
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle(desination);

    }


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
     * Save all contentitems in the contentlist.
     **/

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = activity.getSharedPreferences(desination, 0);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contentList);

        prefsEditor.putString(desination, json);

        prefsEditor.commit();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    /**
     * Gets a contentItem from the EditFragment and adds it to a contentList.
     * The contentList is saved to sharedpreferences using Gson and Json.
     **/
    public void addNewContent(ContentItem contentItem) {
        contentList.add(contentItem);
        System.out.println("Storlek på contentlist: " + contentList.size());
        myAdapter.notifyDataSetChanged();

        SharedPreferences prefs = activity.getSharedPreferences(desination, 0);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contentList);

        prefsEditor.putString(desination, json);

        prefsEditor.commit();

    }

    private void loadInfo(){
        /**
         *Load contentItems using Json.
         **/
        SharedPreferences prefs = activity.getSharedPreferences(desination, 0);
        Gson gson = new Gson();
        String json = prefs.getString(desination, null);
        Map<String, ?> keys = prefs.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }

        Type type = new TypeToken<ArrayList<ContentItem>>() {
        }.getType();
        contentList = gson.fromJson(json, type);
        // Log.d("Contentlist 1", contentList.get(0).getImages().get(0).toString());
        if (contentList == null) {
            contentList = new ArrayList<ContentItem>();
        }
    }




}





