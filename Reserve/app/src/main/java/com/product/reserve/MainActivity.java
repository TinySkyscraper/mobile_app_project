package com.product.reserve;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.PathParser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Insets;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private int mTableToPlace = -1;
    private int myX;
    private int myY;
    private Bitmap bitmap;
    private SharedPreferences sharePref;

    public static TableConnector tableConnector = new TableConnector();
    public View parentLayout;
    public FrameLayout frameLayout;
    public static View reserveLayout;
    public static FrameLayout reserveFrame;
    public int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = getLayoutInflater().inflate(R.layout.activity_main, null);
        reserveLayout = getLayoutInflater().inflate(R.layout.activity_reserve, null);
        frameLayout = parentLayout.findViewById(R.id.frame_group);
        reserveFrame = reserveLayout.findViewById(R.id.reverve_frame_group);
        sharePref = this.getPreferences(Context.MODE_PRIVATE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.base_image);
        addTable();
    }

    public void addTable(){

        ViewGroup frameGroup = findViewById(R.id.frame_group);

        ImageButton reuse_layout = findViewById(R.id.reuse_layout);
        reuse_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                setPreviousBackgroundImage();
                getSerializableObject();
                setContentView(parentLayout);
                addTable();
            }
        });

        ImageButton save_layout = findViewById(R.id.save_layout);
        save_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){createSerializableObject();}
        });

        frameGroup.setOnTouchListener((v, event) -> {
            int size = 80;
            Drawable image = getDrawable(R.drawable.ic_baseline_circle_25);

            switch (mTableToPlace){
                case R.id.small_circle_table:
                    image = getDrawable(R.drawable.ic_baseline_circle_25);
                    break;
                case R.id.medium_circle_table:
                    image = getDrawable(R.drawable.ic_baseline_circle_35);
                    break;
                case R.id.large_circle_table:
                    image = getDrawable(R.drawable.ic_baseline_circle_45);
                    break;
                case R.id.small_square_table:
                    image = getDrawable(R.drawable.ic_baseline_square_25);
                    break;
                case R.id.medium_square_table:
                    image = getDrawable(R.drawable.ic_baseline_square_35);
                    break;
                case R.id.large_square_table:
                    image = getDrawable(R.drawable.ic_baseline_square_45);
                    break;
                case R.id.small_rectangle_table:
                    image = getDrawable(R.drawable.ic_baseline_rectangle_25_50);
                    break;
                case R.id.medium_rectangle_table:
                    image = getDrawable(R.drawable.ic_baseline_rectangle_40_80);
                    break;
                case R.id.large_rectangle_table:
                    image = getDrawable(R.drawable.ic_baseline_rectangle_40_100);
                    break;
                case R.id.small_verticle_rectangle_table:
                    image = getDrawable(R.drawable.ic_baseline_verticle_rectangle_50_25);
                    break;
                case R.id.medium_verticle_rectangle_table:
                    image = getDrawable(R.drawable.ic_baseline_verticle_rectangle_80_40);
                    break;
            }

            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    myX = (int) event.getX();
                    myY = (int) event.getY();

                    ImageView imageView = new ImageView(this);
                    imageView.setId(index);
                    imageView.setTag(index);
                    imageView.setImageDrawable(image);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                            );
                    params.setMargins(myX-35,myY-35,0,0);
                    imageView.setLayoutParams(params);

                    ImageView imageView2 = new ImageView(this);
                    imageView2.setId(100 + index);
                    imageView2.setTag(100 + index);
                    imageView2.setImageDrawable(image);
                    FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    params2.setMargins(myX-35,myY-35,0,0);
                    imageView2.setLayoutParams(params2);

                    frameLayout.addView(imageView);
                    reserveFrame.addView(imageView2);
                    tableConnector.tableAdder(imageView2);

                    setContentView(parentLayout);
                    addTable();
                    index++;
                    return true;
            }
            return false;
        });
    }

    public void removeTable(View v){
        if (index > 0){
            ImageView imageView = findViewById(index-1);
            ImageView imageView2 = reserveFrame.findViewById(100 + index - 1);
            frameLayout.removeView(imageView);
            ((ViewGroup) imageView2.getParent()).removeView(imageView2);
            tableConnector.tableSubtracter(imageView2);
            setContentView(parentLayout);
            index--;
            addTable();
            }
    }

    public void tableChooser(View v){
        mTableToPlace = v.getId();
    }

    public void addImage(View v){
        mGetImageContent.launch("image/*");
    }

    private final ActivityResultLauncher<String> mGetImageContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }

                    ImageView imageView = parentLayout.findViewById(R.id.restuarant_view);
                    ImageView reserveImageView = reserveLayout.findViewById(R.id.reserve_restuarant_view);
                    imageView.setImageBitmap(bitmap);
                    reserveImageView.setImageBitmap(bitmap);
                    setContentView(parentLayout);

                    addTable();
                } catch (Exception e) {
                    setContentView(parentLayout);
                    addTable();
                }
            });

    public void nextPage(View view){
        Intent intent = new Intent(this, reserve.class);
        startActivity(intent);
    }

    public void setPreviousBackgroundImage(){
        String previousEncodedImage = sharePref.getString("image", "");

        if (previousEncodedImage != ""){
            byte[] b = Base64.decode(previousEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ImageView imageView = parentLayout.findViewById(R.id.restuarant_view);
            ImageView reserveImageView = reserveLayout.findViewById(R.id.reserve_restuarant_view);
            imageView.setImageBitmap(bitmap);
            reserveImageView.setImageBitmap(bitmap);
        }
    }

    public void createSerializableObject(){
        Log.e("MAIN", "Serializing object!!");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("image", encodedImage);
        editor.apply();

        ArrayList<Integer>  listHolder = new ArrayList<Integer>();
        if (frameLayout.getChildCount() > 1) {
            for (int i = 1; i < frameLayout.getChildCount(); i++) {
                ImageView tmpTable = (ImageView) frameLayout.getChildAt(i);

                Integer tmpId = (Integer) tmpTable.getId();
                Integer myX = (Integer) Math.round(tmpTable.getX());
                Integer myY = (Integer) Math.round(tmpTable.getY());
                Integer imageId = (Integer) R.drawable.ic_baseline_circle_25;
                Drawable.ConstantState tableImage = tmpTable.getDrawable().getConstantState();

                if (tableImage == getDrawable(R.drawable.ic_baseline_circle_25).getConstantState()) {
                    imageId = R.drawable.ic_baseline_circle_25;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_circle_35).getConstantState()) {
                    imageId = R.drawable.ic_baseline_circle_35;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_circle_45).getConstantState()) {
                    imageId = R.drawable.ic_baseline_circle_45;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_square_25).getConstantState()) {
                    imageId = R.drawable.ic_baseline_square_25;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_square_35).getConstantState()) {
                    imageId = R.drawable.ic_baseline_square_35;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_square_45).getConstantState()) {
                    imageId = R.drawable.ic_baseline_square_45;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_rectangle_25_50).getConstantState()) {
                    imageId = R.drawable.ic_baseline_rectangle_25_50;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_rectangle_40_80).getConstantState()) {
                    imageId = R.drawable.ic_baseline_rectangle_40_80;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_rectangle_40_100).getConstantState()) {
                    imageId = R.drawable.ic_baseline_rectangle_40_100;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_verticle_rectangle_50_25).getConstantState()) {
                    imageId = R.drawable.ic_baseline_verticle_rectangle_50_25;
                } else if (tableImage == getDrawable(R.drawable.ic_baseline_verticle_rectangle_80_40).getConstantState()) {
                    imageId = R.drawable.ic_baseline_verticle_rectangle_80_40;
                }

                listHolder.add(tmpId);
                listHolder.add(myX);
                listHolder.add(myY);
                listHolder.add(imageId);
            }
            Gson gson = new Gson();
            String json = gson.toJson(listHolder);
            editor.putString("ArrayObj", json);
            editor.apply();
        }
        }

        public void getSerializableObject(){
        Log.e("MAIN", "getting serializable object!!");
            ArrayList<Double> listHolder;

            Gson gson = new Gson();
            String json = sharePref.getString("ArrayObj", "");
            listHolder = gson.fromJson(json, ArrayList.class);

            int tmpId = 0;

            for (int i=0; i<listHolder.size(); i=i+4){

                tmpId = (int) Math.round(listHolder.get(i+0));
                int myX = (int) Math.round(listHolder.get(i+1));
                int myY = (int) Math.round(listHolder.get(i+2));
                int imageId = (int) Math.round(listHolder.get(i+3));

                ImageView imageView = new ImageView(this);
                imageView.setId(tmpId + index);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(myX, myY,0,0);
                imageView.setLayoutParams(params);
                imageView.setImageDrawable(getDrawable(imageId));

                ImageView imageView2 = new ImageView(this);
                imageView2.setId(100 + tmpId + index);
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                params2.setMargins(myX, myY,0,0);
                imageView2.setLayoutParams(params);
                imageView2.setImageDrawable(getDrawable(imageId));

                frameLayout.addView(imageView);
                reserveFrame.addView(imageView2);
                tableConnector.tableAdder(imageView2);
                setContentView(parentLayout);
                addTable();
                }
            index = tmpId + index + 1;
            }

        }