package ur.disorderapp;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ur.disorderapp.model.DataPiece;


public class SlideFragment extends Fragment
{
    //For sending data out
    public interface OnDataPass {
        public void onDataPass(DataPiece data);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        try {
            dataPasser = (OnDataPass) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() +
                    " must implement OnArticleSelectedListener");
        }
    }

    public static final String KEY = "key";

    public SlideFragment()
    {
        // Required empty public constructor
    }

    public static SlideFragment newInstance(int position)
    {
        //A Factory Method creating new instance
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_rootview, container, false);

        Bundle args = getArguments();
        final int position = args.getInt(KEY);

        final Button btn_0 = (Button) rootView.findViewById(R.id.frag_btn_0);
        final Button btn_1 = (Button) rootView.findViewById(R.id.frag_btn_1);
        final Button btn_2 = (Button) rootView.findViewById(R.id.frag_btn_2);
        final Button btn_3 = (Button) rootView.findViewById(R.id.frag_btn_3);

        final ImageView image_1 = (ImageView) rootView.findViewById(R.id.image_1);
        final ImageView image_2 = (ImageView) rootView.findViewById(R.id.image_2);
        final ImageView image_3 = (ImageView) rootView.findViewById(R.id.image_3);
        final ImageView image_4 = (ImageView) rootView.findViewById(R.id.image_4);


        String  text_0 = null,
                text_1 = null,
                text_2 = null,
                text_3 = null;

        int     image_1_source = 0,
                image_2_source = 0,
                image_3_source = 0,
                image_4_source = 0;

        //Set the content of fragments
        if (position==0){
            //Food Name
            text_0 = "Candy_Bar";
            text_1 = "Donuts";
            text_2 = "Soda";
            text_3 = "Fruit";

            image_1_source = R.drawable.snicker;
            image_2_source = R.drawable.donut;
            image_3_source = R.drawable.soda;
            image_4_source = R.drawable.fruit;

        } else if (position==1) {
            //How many
            text_0 = "1";
            text_1 = "2";
            text_2 = "3";
            text_3 = "4";

            image_1_source = R.drawable.one;
            image_2_source = R.drawable.two;
            image_3_source = R.drawable.three;
            image_4_source = R.drawable.four;

        } else if (position==2) {
            //When?
            text_0 = "MORNING";
            text_1 = "NOON";
            text_2 = "NIGHT";
            text_3 = "OTHER";

            image_1_source = R.drawable.morning;
            image_2_source = R.drawable.afternoon;
            image_3_source = R.drawable.night;
            image_4_source = R.drawable.other;

        } else if (position==3) {
            //WHERE?
            text_0 = "HOME";
            text_1 = "WORK";
            text_2 = "ON_THE_GO";
            text_3 = "OTHER";

            image_1_source = R.drawable.house;
            image_2_source = R.drawable.farm;
            image_3_source = R.drawable.truck;
            image_4_source = R.drawable.other;


        } else if (position==4) {
            //Feeling?
            text_0 = "HUNGARY";
            text_1 = "THIRSTY";
            text_2 = "EXHAUSTED";
            text_3 = "OTHER";

            image_1_source = R.drawable.hungry;
            image_2_source = R.drawable.thirsty;
            image_3_source = R.drawable.exhausted;
            image_4_source = R.drawable.other;

        } else if (position==5) {
            //Situation?
            text_0 = "ALONE";
            text_1 = "FAMILY";
            text_2 = "COLLEAGUE";
            text_3 = "OTHER";

            image_1_source = R.drawable.alone;
            image_2_source = R.drawable.fam;
            image_3_source = R.drawable.colleague;
            image_4_source = R.drawable.other;

        }

        btn_0.setText(text_0);
        btn_1.setText(text_1);
        btn_2.setText(text_2);
        btn_3.setText(text_3);

//        image_1.setImageResource(image_1_source);
//        image_2.setImageResource(image_2_source);
//        image_3.setImageResource(image_3_source);
//        image_4.setImageResource(image_4_source);

        image_1.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(),
                        image_1_source, 100, 100));

        image_2.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(),
                        image_2_source, 100, 100));

        image_3.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(),
                        image_3_source, 100, 100));

        image_4.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(),
                        image_4_source, 100, 100));


        //onClick Listeners
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(btn_0);
                deselect(btn_1);
                deselect(btn_2);
                deselect(btn_3);
                String data = (String)btn_0.getText();
                DataPiece dataPiece = new DataPiece(position,data);
                dataPasser.onDataPass(dataPiece);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselect(btn_0);
                select(btn_1);
                deselect(btn_2);
                deselect(btn_3);
                String data = (String)btn_1.getText();
                DataPiece dataPiece = new DataPiece(position,data);
                dataPasser.onDataPass(dataPiece);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselect(btn_0);
                deselect(btn_1);
                select(btn_2);
                deselect(btn_3);
                String data = (String)btn_2.getText();
                DataPiece dataPiece = new DataPiece(position,data);
                dataPasser.onDataPass(dataPiece);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselect(btn_0);
                deselect(btn_1);
                deselect(btn_2);
                select(btn_3);
                String data = (String)btn_3.getText();
                DataPiece dataPiece = new DataPiece(position,data);
                dataPasser.onDataPass(dataPiece);
            }
        });


        return rootView;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private void select(Button b)
    {
        b.setBackgroundColor(Color.RED);
    }

    private void deselect(Button b)
    {
        b.setBackgroundResource(android.R.drawable.btn_default);
    }

}
