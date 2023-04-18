package com.product.reserve;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class TableConnector {

    private static ArrayList<TextView> partyList = new ArrayList<TextView>();
    private static ArrayList<ImageView> tableList = new ArrayList<ImageView>();
    public static Dictionary<ImageView, TextView> dict = new Hashtable<>();
    public static Enumeration<ImageView> keys = dict.keys();
    public static Enumeration<TextView> values = dict.elements();
    public static ImageView currentTable;
    public static ImageView previousTable;

    public static void partyAdder(TextView textView){
        partyList.add(textView);
    }
    public static void partySubtracter(TextView textView){
        int textId = textView.getId();
        for (int i = 0; i < partyList.size(); i++){
            TextView tempObj = partyList.get(i);
            if (textId == tempObj.getId()){
                partyList.remove(tempObj);
            }
        }

    }

    public static void tableAdder(ImageView imageView){
        tableList.add(imageView);
    }
    public static void tableSubtracter(ImageView imageView){
        int imageId = imageView.getId();
        for (int i = 0; i < tableList.size(); i++){
            ImageView tempObj = tableList.get(i);
            if (imageId == tempObj.getId()){
                partyList.remove(tempObj);
            }
        }

    }
    public static void addPartyTableAssociation(TextView party){
        dict.put(currentTable, party);
    }

    public static void subtractPartyTableAssociation(ImageView table){
            dict.remove(table);
        }

    public static void putCurrentTable(ImageView imageView){
        currentTable = imageView;
    }
}


