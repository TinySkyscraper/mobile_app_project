package com.product.reserve;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TableConnector {

    public static ArrayList<TextView> partyList = new ArrayList<TextView>();
    private static ArrayList<ImageView> tableList = new ArrayList<ImageView>();
    public static Map<TextView, ImageView> hmParty = new HashMap<TextView, ImageView>();
    public static Map<ImageView, TextView> hmTable = new HashMap<ImageView, TextView>();
    public static ImageView currentTable;

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
        hmParty.put(party, currentTable);
        hmTable.put(currentTable, party);
    }

    public static void subtractPartyTableAssociation(ImageView removeTbl){
        hmParty.remove(hmTable.get(removeTbl));
        hmTable.remove(removeTbl);
        }

    public static void putCurrentTable(ImageView imageView){
        currentTable = imageView;
    }
}


