package ur.disorderapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        String text_0 = null, text_1 = null, text_2 = null, text_3 = null;

        //Set the content of fragments
        if (position==0){
            //Food Name
            text_0 = "Candy_Bar";
            text_1 = "Donuts";
            text_2 = "Soda";
            text_3 = "Fruit";
        } else if (position==1) {
            //How many
            text_0 = "1";
            text_1 = "2";
            text_2 = "3";
            text_3 = "4";
        } else if (position==2) {
            //When?
            text_0 = "MORNING";
            text_1 = "NOON";
            text_2 = "NIGHT";
            text_3 = "OTHER";
        } else if (position==3) {
            //WHERE?
            text_0 = "HOME";
            text_1 = "WORK";
            text_2 = "ON_THE_GO";
            text_3 = "OTHER";
        } else if (position==4) {
            //Feeling?
            text_0 = "HUNGARY";
            text_1 = "THIRSTY";
            text_2 = "EXHAUSTED";
            text_3 = "OTHER";
        } else if (position==5) {
            //Situation?
            text_0 = "MEAL";
            text_1 = "FAMILY_MEAL";
            text_2 = "WORK_MEAL";
            text_3 = "OTHER";
        }

        btn_0.setText(text_0);
        btn_1.setText(text_1);
        btn_2.setText(text_2);
        btn_3.setText(text_3);

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

    private void select(Button b)
    {
        b.setBackgroundColor(Color.RED);
    }

    private void deselect(Button b)
    {
        b.setBackgroundResource(android.R.drawable.btn_default);
    }

}
