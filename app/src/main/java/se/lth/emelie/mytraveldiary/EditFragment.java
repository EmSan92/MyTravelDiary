package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button cameraButton;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView im1;
    private ImageView im2;
    private ImageView im3;
    private int counter;
    private ContentItem content;

    public static EditFragment newInstance() {
        EditFragment fragment = new EditFragment();

        return fragment;
    }

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        counter = 0;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.edit_fragment, container, false);
        TextView date = (TextView) view.findViewById(R.id.dateview);
        TextView cap = (TextView) view.findViewById(R.id.caption);
        EditText tt1 = (EditText) view.findViewById(R.id.text1);
        EditText tt2 = (EditText) view.findViewById(R.id.text2);
        EditText tt3 = (EditText) view.findViewById(R.id.text3);

        im1 = (ImageView) view.findViewById(R.id.image1);
        im2 = (ImageView) view.findViewById(R.id.image2);
        im3 = (ImageView) view.findViewById(R.id.image3);


        cameraButton = (Button) view.findViewById(R.id.camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "text", Toast.LENGTH_SHORT).show();

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

/**
 * Fixa GSon så att man kan spara object i shared preferences!!!!!! TO DO!!!!!!
 * */
        return view;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == this.getActivity().RESULT_OK && counter <=2) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");

            switch (counter) {
                case 0:
                    im1.setVisibility(View.VISIBLE);
                    im1.setImageBitmap(bmp);
                    content.setImage(bmp);
                    counter++;
                    break;
                case 1:
                    im2.setVisibility(View.VISIBLE);
                    im2.setImageBitmap(bmp);
                    content.setImage(bmp);
                    counter++;
                    break;
                case 2:
                    im3.setVisibility(View.VISIBLE);
                    im3.setImageBitmap(bmp);
                    content.setImage(bmp);
                    counter++;
                    break;
            }
        }else{
            Toast.makeText(this.getActivity(), "Can only have three pictures in one post!", Toast.LENGTH_SHORT).show();
        }


            }
        }









