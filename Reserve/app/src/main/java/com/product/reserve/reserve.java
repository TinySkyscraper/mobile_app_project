package com.product.reserve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

public class reserve extends AppCompatActivity {

    private EditText mPartyEditText;
    private LinearLayout mPartyList;
    private LinearLayout mPartyListDuplicate;
    private View reserveLayout;
    private FrameLayout reserveFrame;
    private ImageButton mAddPartyButton;
    private int index = 0;
    private int mPartyIndex = 0;
    private TableConnector tableConnector = MainActivity.tableConnector;
    private Map<TextView, ImageView> hmParty = tableConnector.hmParty;
    private Map<ImageView, TextView> hmTable = tableConnector.hmTable;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reserveLayout = MainActivity.reserveLayout;
        reserveFrame = reserveLayout.findViewById(R.id.reverve_frame_group);
        setContentView(reserveLayout);

        mPartyEditText = findViewById(R.id.party_edit_text);
        mPartyList = reserveLayout.findViewById(R.id.scroll_linear_layout);
        mPartyListDuplicate = reserveLayout.findViewById(R.id.scroll_linear_layout_duplicate);

        mAddPartyButton = findViewById(R.id.add_party_button);
        mAddPartyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                addParty(view);
            }
        });

        for (int i=0; i < reserveFrame.getChildCount(); i++){
            if (i != 0){
                reserveFrame.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event){
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                view.setBackground(getResources().getDrawable(R.drawable.shape5));
                                reserveLayout.findViewById(R.id.reserve_scroll_group_duplicate)
                                        .setVisibility(View.VISIBLE);
                                reserveLayout.findViewById(R.id.remove_party_button)
                                        .setVisibility(View.VISIBLE);
                                setContentView(reserveLayout);

                                ImageView table = (ImageView) view;
                                tableConnector.putCurrentTable(table);

                                TextView party = null;
                                try{
                                    if (hmTable.containsKey(table)){
                                        party = hmTable.get(table);
                                    }


                                    /*TextView party = dict.get(table);*/
                                    String name = party.getText().toString();
                                    displayToast("Associated Party: " + name);
                                }catch(Exception e){
                                    Log.i("reserve", "no elements yet");
                                }
                                return true;
                            case MotionEvent.ACTION_UP:
                                view.setBackgroundResource(0);
                                return true;
                        }
                        return true;
                    }
                });
            }
        }
        ImageView mSubtractPartyButton = reserveLayout.findViewById(R.id.remove_party_button);
        mSubtractPartyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                ImageView removeTbl = tableConnector.currentTable;
                hmParty.remove(hmTable.get(removeTbl));
                hmTable.remove(removeTbl);

                mSubtractPartyButton.setVisibility(View.GONE);
                reserveLayout.findViewById(R.id.reserve_scroll_group_duplicate)
                        .setVisibility(View.GONE);

                removeTbl.clearColorFilter();
            }
        });
    }

    @Override
    protected void onDestroy(){
        Log.e("MAIN", "On Destroy was called!!");
        ((ViewGroup) MainActivity.reserveLayout.getParent()).removeView(MainActivity.reserveLayout);
        hmParty.clear();
        hmTable.clear();
        mPartyList.removeAllViews();
        mPartyListDuplicate.removeAllViews();
        super.onDestroy();

    }


    public void addParty(View view){
        String partyData = mPartyEditText.getText().toString();
        TextView party = new TextView(this);
        party.setText(partyData);
        party.setId(200 + index);
        party.setTextColor(getResources().getColor(R.color.white));
        party.setGravity(Gravity.CENTER_HORIZONTAL);
        party.setElevation(0);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,0,15);
        party.setLayoutParams(params);
        party.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackground(getResources().getDrawable(R.drawable.shape3));

                        mTimer = new CountDownTimer(1000, 100) {
                            public void onTick(long millisUntilFinished) {
                                }

                            public void onFinish() {
                                view.setBackground(null);
                            }
                        }.start();


                return true;
                    case MotionEvent.ACTION_UP:
                        view.setBackground(null);
                        registerForContextMenu(view);
                        openContextMenu(view);
                        return true;
                }
                return true;
            }
        });

        TextView party2 = new TextView(this);
        party2.setText(partyData);
        party2.setId(300 + index);
        party2.setTextColor(getResources().getColor(R.color.white));
        party2.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins(0,0,0,15);
        party2.setLayoutParams(params2);
        party2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackground(getResources().getDrawable(R.drawable.shape3));

                        mTimer = new CountDownTimer(1000, 100) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                view.setBackground(null);
                            }
                        }.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.setBackground(null);
                        tableConnector.addPartyTableAssociation((TextView) view);
                        reserveLayout.findViewById(R.id.reserve_scroll_group_duplicate)
                                .setVisibility(View.GONE);
                        reserveLayout.findViewById(R.id.remove_party_button)
                                .setVisibility(View.GONE);
                        ImageView tmpTable = tableConnector.currentTable;

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        matrix.setScale(0.5f, 0.5f, 0.5f, 1f);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        tmpTable.setColorFilter(filter);

                        setContentView(reserveLayout);
                        return true;
                }
                return true;
            }
        });

        mPartyList.addView(party);
        mPartyListDuplicate.addView(party2);
        tableConnector.partyAdder(party);
        setContentView(reserveLayout);

        index++;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View view,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        mPartyIndex = view.getId();
        Log.i("reserve", "onCreateContextMenu has been called!");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        TextView party = reserveLayout.findViewById(mPartyIndex);
        TextView party2 = reserveLayout.findViewById(mPartyIndex + 100);
        mPartyList.removeView(party);
        mPartyListDuplicate.removeView(party2);
        tableConnector.partySubtracter(party);
        setContentView(reserveLayout);
        return super.onContextItemSelected(item);
    }

    public void displayToast(String message){
        Toast toast = new Toast(reserve.this);
        TextView textView = new TextView(reserve.this);
        textView.setText(message);
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 100);
        toast.show();
    }
}