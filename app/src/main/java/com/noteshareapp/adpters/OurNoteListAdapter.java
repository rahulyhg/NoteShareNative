package com.noteshareapp.adpters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noteshareapp.noteshare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OurNoteListAdapter extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    String listview = "";
    Activity activity;
    TextView noteDesc;

    public OurNoteListAdapter(Activity activity, ArrayList<HashMap<String,String>> list, String listview){
        super();
        this.activity = activity;
        this.list = list;
        this.listview = listview;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        //all the fields in layout specified
        TextView txtNoteName,txtNoteDesc,txtNoteDate, tvIdHidden;
        ImageButton btnLock, btnTimebomb, btnMove, btnDelete, btnShare, btnRemind, btnOption;
        ImageView isLockedIcon;
        LinearLayout linearLayout;
        LinearLayout main;
        Button btn3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Typeface faceBold = Typeface.createFromAsset(activity.getAssets(), "fonts/AgendaBold.ttf");
        Typeface faceMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/Agenda-Medium.ttf");


        LayoutInflater inflater=activity.getLayoutInflater();
        if(convertView == null){
            holder = new ViewHolder();
            if(listview == "DETAIL")
                convertView = inflater.inflate(R.layout.our_note_list_detail,null); //change the name of the layout
            if(listview == "LIST")
                convertView = inflater.inflate(R.layout.our_note_list_list,null); //change the name of the layout
            if(listview == "GRID")
                convertView = inflater.inflate(R.layout.our_note_list_grid,null); //change the name of the layout         }


            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.front);
            holder.txtNoteName = (TextView) convertView.findViewById(R.id.tvNoteName); //find the different Views
            holder.txtNoteDesc = (TextView) convertView.findViewById(R.id.tvNoteDesc);
            holder.txtNoteDate = (TextView) convertView.findViewById(R.id.tvNoteDate);
            //holder.isLockedIcon = (ImageView) convertView.findViewById(R.id.isLockedIcon);

            //holder.btn3 = (Button) convertView.findViewById(R.id.btn3); // remove this

            holder.main = (LinearLayout) convertView.findViewById(R.id.noteMain);

            if(listview == "LIST") {
            }
            if(listview == "DETAIL"){
                holder.isLockedIcon = (ImageView) convertView.findViewById(R.id.isLockedIcon);
            }

            if(listview != "GRID") {
                holder.tvIdHidden = (TextView) convertView.findViewById(R.id.tvIdHidden);
                holder.btnLock = (ImageButton) convertView.findViewById(R.id.btnlock);
                holder.btnTimebomb = (ImageButton) convertView.findViewById(R.id.btntimebomb);
                holder.btnMove = (ImageButton) convertView.findViewById(R.id.btnmove);
                holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btndelete);
                holder.btnShare = (ImageButton) convertView.findViewById(R.id.btnshare);
                holder.btnRemind = (ImageButton) convertView.findViewById(R.id.btnRemind);
            }

            if(listview == "GRID"){
                holder.btnOption = (ImageButton) convertView.findViewById(R.id.optionInGrid);
                holder.btnShare = (ImageButton) convertView.findViewById(R.id.shareInGrid);
            }
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        HashMap<String,String> map = list.get(position);
        holder.txtNoteName.setText(map.get("noteName")); //set the hash maps
        holder.txtNoteName.setTypeface(faceBold);

        holder.txtNoteDesc.setText(map.get("noteDesc"));
        holder.txtNoteDesc.setTypeface(faceMedium);

        holder.txtNoteDate.setText(dbToAdapterDate(map.get("noteDate")));
        holder.txtNoteDate.setTypeface(faceMedium);

        if(map.get("noteBgColor").toString().equals("#FFFFFF") && listview == "GRID")
            holder.linearLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
        else
            holder.linearLayout.setBackgroundColor(Color.parseColor(map.get("noteBgColor")));


        if(map.get("noteLock").equalsIgnoreCase("1") && listview=="DETAIL"){
            holder.btnLock.setImageResource(R.drawable.ic_note_unlock);
            holder.isLockedIcon.setVisibility(View.VISIBLE);
        }

        if(map.get("noteLock").equalsIgnoreCase("1") && listview=="LIST"){
            holder.btnLock.setImageResource(R.drawable.ic_note_unlock);
        }

        if(listview != "GRID") {
            holder.tvIdHidden.setText(map.get("noteId"));
            holder.btnLock.setTag(map.get("noteId"));
            holder.btnTimebomb.setTag(map.get("noteId"));
            holder.btnMove.setTag(map.get("noteId"));
            holder.btnDelete.setTag(map.get("noteId"));
            holder.btnShare.setTag(map.get("noteId"));
            holder.btnRemind.setTag(map.get("noteId"));
        }else{
            //holder.btnDelete.setTag(map.get("noteId"));
            holder.btnOption.setTag(map.get("noteId"));
            holder.btnShare.setTag(map.get("noteId"));
        }

        return convertView;
    }

    public String dbToAdapterDate(String date){

        SimpleDateFormat originalDateFormat  = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
        //SimpleDateFormat requiredDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        SimpleDateFormat requiredDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");

        try {
            date = requiredDateFormat.format(originalDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}