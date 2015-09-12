package se.lth.emelie.mytraveldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Calendar.*;


public class EditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView im1;
    private ImageView im2;
    private ImageView im3;
    private int counter, textcounter;
    private ContentItem content;
    private ArrayList<Bitmap> imList;
    private static String root = null;
    private static String imageFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    private ArrayList<String> textList;
    private Map<Integer, ArrayList<String>> pathList;
    private Button save, cameraButton, gallery, write;
    private PlaceViewFragment placeViewFragment;
    private EditText tt1, tt2, tt3;
    private TextView date, cap;
    private String dateString, capString;
    private String destination;
    private ArrayList<String> images;
    private ArrayList<String> imageNames;

    public static EditFragment newInstance() {
        EditFragment fragment = new EditFragment();

        return fragment;
    }

    public EditFragment() {
        // Required empty public constructor
    }

    public void setPlaceViewFragment(PlaceViewFragment frag) {
        this.placeViewFragment = frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imList = new ArrayList<Bitmap>();
        content = new ContentItem(null, null, null, null);

        textList = new ArrayList<String>();
        counter = 0;
        textcounter = 0;
        dateString = null;
        capString = null;
        destination = getArguments().getString("destination");
        images = new ArrayList<String>();
        imageNames = new ArrayList<String>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.edit_fragment, container, false);

        date = (TextView) view.findViewById(R.id.date);
        cap = (TextView) view.findViewById(R.id.cap);
        tt1 = (EditText) view.findViewById(R.id.text1);
        tt2 = (EditText) view.findViewById(R.id.text2);
        tt3 = (EditText) view.findViewById(R.id.text3);

        im1 = (ImageView) view.findViewById(R.id.image1);
        im2 = (ImageView) view.findViewById(R.id.image2);
        im3 = (ImageView) view.findViewById(R.id.image3);


        /**
         *Open the camera sensor when camera button is pressed
         **/
        cameraButton = (Button) view.findViewById(R.id.camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "text", Toast.LENGTH_SHORT).show();

                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(takePictureIntent, CAMERA_REQUEST);

            }
        });

        /**
         *Go back to placeviewfragment and showing all post.
         * Sends a contentitem back to the addnewcontent methos in placeviewfragment
         **/

//        System.out.println("Imagenames: "+ imageNames.get(0));
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.replace(R.id.fragmentContainer, placeViewFragment, "tag");
                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    placeViewFragment.addNewContent(new ContentItem(date, "Caption", textList, images));



                //placeViewFragment.addNewContent(content);
                Toast.makeText(v.getContext(), "saved", Toast.LENGTH_SHORT).show();

                fragmentTransaction.commit();
            }
        });
        /**
         *Load images from the image gallery on the phone
         **/
        gallery = (Button) view.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        /**
         *Open a dialog so user can put in text.
         **/
        write = (Button) view.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInputDialog();
                //Toast.makeText(v.getContext(), "work", Toast.LENGTH_SHORT).show();

            }
        });


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         *Gets the images taken with the camera, depended on how many pictures were taken, they are saved in images.
         **/
        if (requestCode == CAMERA_REQUEST && resultCode == this.getActivity().RESULT_OK && counter <= 2) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int newWidth = 700;
            int newHeight = 700;

            // calculate the scale - in this case = 0.4f
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight);
            // rotate the Bitmap


            // recreate the new Bitmap
            Bitmap resizedBmp = Bitmap.createBitmap(bmp, 0, 0,
                    width, height, matrix, true);

            switch (counter) {
                case 0:
                    im1.setVisibility(View.VISIBLE);
                    im1.setImageBitmap(resizedBmp);
                   // Log.d(this.getActivity().getPackageName(), bmp != null ? "bmp is not null!" : "bmp is null!");
                    images.add(saveToInternalStorage(resizedBmp));
                    counter++;
                    break;
                case 1:
                    im2.setVisibility(View.VISIBLE);
                    im2.setImageBitmap(resizedBmp);
                    //pathList.add(1,saveToInternalStorage(bmp));
                    images.add(saveToInternalStorage(resizedBmp));

                    counter++;

                    break;
                case 2:
                    im3.setVisibility(View.VISIBLE);
                    im3.setImageBitmap(resizedBmp);
                    images.add(saveToInternalStorage(resizedBmp));

                    counter++;
                    break;
            }



            /**
             * Get images from the gallery on the phone, resize them and save them in the pathlist.
             **/
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == this.getActivity().RESULT_OK && null != data && counter <= 2) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;


            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImage);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int newWidth = 700;
                int newHeight = 700;

                // calculate the scale - in this case = 0.4f
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                // create a matrix for the manipulation
                Matrix matrix = new Matrix();
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight);
                // rotate the Bitmap


                // recreate the new Bitmap
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        width, height, matrix, true);

                switch (counter) {
                    case 0:
                        im1.setVisibility(View.VISIBLE);
                        im1.setImageBitmap(resizedBitmap);
                        Log.d(this.getActivity().getPackageName(), bitmap != null ? "bitmap is not null!" : "bitmap is null!");
                        images.add(saveToInternalStorage(resizedBitmap));


                        counter++;
                        break;
                    case 1:
                        im2.setVisibility(View.VISIBLE);
                        im2.setImageBitmap(resizedBitmap);
                        images.add(saveToInternalStorage(resizedBitmap));

                        counter++;
                        break;
                    case 2:
                        im3.setVisibility(View.VISIBLE);
                        im3.setImageBitmap(resizedBitmap);
                        images.add(saveToInternalStorage(resizedBitmap));

                        counter++;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(this.getActivity(), "Can only have three pictures in one post!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Make the bitmap image into a string filepath
     **/
    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(this.getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = null;
        try {
            mypath = File.createTempFile("image", ".jpg", directory);

        } catch (IOException e) {
            e.printStackTrace();
        }
        imageNames.add(mypath.getName());
        //System.out.println("Imagenames " + imageNames.get(0) + " sholud be same as:  "+ mypath.getName());
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("abs file: "+mypath.getAbsolutePath());

        return mypath.getAbsolutePath();
    }


    /**
     *A dialog where a user writes the texts for the post
     **/
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(EditFragment.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_edit, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditFragment.this.getActivity());
        alertDialogBuilder.setView(promptView);


        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        textList.add(editText.getText().toString());


                        switch (textList.size()) {
                            case 1:
                                tt1.setVisibility(View.VISIBLE);
                                tt1.setText(textList.get(0));

                                break;
                            case 2:
                                tt2.setVisibility(View.VISIBLE);
                                tt2.setText(textList.get(1));

                                break;
                            case 3:
                                tt3.setVisibility(View.VISIBLE);
                                tt3.setText(textList.get(2));

                                break;
                        }

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



}









