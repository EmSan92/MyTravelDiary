package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class PlaceViewFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private MyAdapter myAdapter;
    private ArrayList<ContentItem> contentList;
    private FloatingActionButton fab2;

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



        ContentItem con = new ContentItem("2015-08-09","Forbidden City");
        contentList.add(con);

        ListView listView = (ListView) view.findViewById(R.id.listView2);

// get data from the table by the ListAdapter
        myAdapter = new MyAdapter(this.getActivity(), R.layout.view_layout, contentList);

        listView.setAdapter(myAdapter);



        fab2 = (FloatingActionButton) view.findViewById(R.id.fabBtn2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("work");
                Toast.makeText(v.getContext(), "text", Toast.LENGTH_SHORT).show();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentPlaceViewContainer, new EditFragment(), "NewFragmentTag");
                ft.commit();
            }
        });


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

}
