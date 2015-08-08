package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import java.util.ArrayList;
import java.util.HashSet;
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
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {
    private ArrayList<String> placeList;
    private TextView resultText;
    private TextView emptyText;
    private String place;
    private Activity activity;
    private boolean pressed;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */

    private ListAdapter myAdapter;
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        SharedPreferences pref = this.getActivity().getSharedPreferences("placelist", 0);
        placeList = loadFromStorage();



        resultText = (TextView) view.findViewById(R.id.result);

        emptyText = (TextView)view.findViewById(android.R.id.empty);




        myAdapter = new ArrayAdapter<String>(getActivity(), R.layout.placelist_layout, placeList);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(myAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);




        fab = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"text", Toast.LENGTH_SHORT).show();
                showInputDialog();


                //myAdapter.notifyDataSetChanged();

            }
        });

        return view;
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

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences pref = this.getActivity().getSharedPreferences("placelist", 0);
        SharedPreferences.Editor editor = pref.edit();

        Set<String> set= new HashSet<String>();
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



    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
            inflater.inflate(R.menu.menu_main, menu);
            if(pressed) {
                menu.findItem(R.id.recyclebin).setVisible(true);
                pressed = false;
            }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      pressed = true;

        Intent intent = new Intent(activity, PlaceViewActivity.class);
        String name = myAdapter.getItem(position).toString();
        intent.putExtra("place", name);
        startActivity(intent);
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
        public void onFragmentInteraction(String id);
    }

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

                        System.out.println(placeList.get(placeList.indexOf(editText.getText().toString())).toString());
                        //resultText.setText("Hello, " + editText.getText());
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

    public ArrayList<String> loadFromStorage() {
        SharedPreferences prefs = activity.getSharedPreferences("placelist", 0);

        ArrayList<String> items = new ArrayList<String>();

        Set<String> set = prefs.getStringSet("placelist", null);
        if(set != null && !set.isEmpty()) {
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
