package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {
    private ArrayList<String> placeList;

    private Activity activity;

    private ArrayAdapter<String> mAdapter;
    private SwipeMenuListView myListView;


    private enum Direction {LEFT, RIGHT;}
    private ArrayList<ContentItem> contentList;
    private String destination;
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    //private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */


    private FloatingActionButton fab;

    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        placeList = new ArrayList<String>();
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        /**
         *Load all destinations and place it in placeList.
         **/
        SharedPreferences pref = this.getActivity().getSharedPreferences("placelist", 0);
        placeList = loadFromStorage();
        contentList = new ArrayList<ContentItem>();


        /**
         * Make floating button clickable, and when pressed a dialog is showing.
         **/
        fab = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"text", Toast.LENGTH_SHORT).show();
                showInputDialog();


            }
        });

        /**
         *Assign the Adapter the right layout to show in listView.
         **/
        myListView = new SwipeMenuListView(activity);
        myListView = (SwipeMenuListView) view.findViewById(R.id.listView1);
       // Log.d(activity.getPackageName(), myListView != null ? "myListView is not null!" : "MyListView is null!");
        mAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.placelist_layout, R.id.placelistlayout, placeList);
        myListView.setAdapter(mAdapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            /**
             *Create the delete view when listitem is swiped to the left
             **/
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        activity.getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                        0x57, 0x22)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        myListView.setMenuCreator(creator);

        /**
         *Delete the listItem when the delete view is clicked
         **/
        myListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                if (!placeList.isEmpty()) placeList.remove(position);
                mAdapter.notifyDataSetChanged();

                return false;
            }
        });

        /**
         *When an listitem is clicked, change fragment to placeviewfragment.
         **/
        myListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                final Fragment placeviewfrag = new PlaceViewFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("destination", placeList.get(i));
                placeviewfrag.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContainer, placeviewfrag, "tag");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();


            }
        });

        // set SwipeListener
        myListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        return view;


    }
    /**
     *Sets the correct title
     **/
    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("My Travel Diary");

    }


    /**
     *Delete the listitem if the deleteview was clicked
     **/
    private void delete(ApplicationInfo item) {
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            Bundle bundle = intent.getExtras();
            String placename = bundle.getString("place");
            placeList.remove(placename);
        } catch (Exception e) {
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
        this.activity = activity;
    }
    /**
     *Saves all destinations in a list.
     **/
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pref = this.getActivity().getSharedPreferences("placelist", 0);
        SharedPreferences.Editor editor = pref.edit();

        Set<String> set = new HashSet<String>();
        for (int i = 0; i < placeList.size(); i++) {
            set.add(placeList.get(i).toString());
        }
        editor.putStringSet("placelist", set);
        editor.commit();

    }


    @Override
    public void onDetach() {
        super.onDetach();


        mListener = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    /**
     *Shows a dialog where a new destination listItem can be created by adding the name of the desitnation and press OK.
     **/
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ItemFragment.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemFragment.this.getActivity());
        alertDialogBuilder.setView(promptView);


        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        placeList.add(editText.getText().toString());


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     *Load all destinations and place them in a list
     **/
    public ArrayList<String> loadFromStorage() {
        SharedPreferences prefs = activity.getSharedPreferences("placelist", 0);

        ArrayList<String> items = new ArrayList<String>();

        Set<String> set = prefs.getStringSet("placelist", null);
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



}
