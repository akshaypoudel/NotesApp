package com.example.notes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class AkkuAdapter extends ArrayAdapter<CreateNotes>
{
    private ArrayList<CreateNotes> arr;
    private MainActivity activity;
    private boolean isActionMode = false;
    private ArrayList<Integer> selectedItems = new ArrayList<>();

    Context context;

    public AkkuAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CreateNotes> arr)
    {
        super(context, resource,arr);
        this.arr=arr;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView= LayoutInflater.from(getContext()).inflate(R.layout.akku_layout,parent,false);

        TextView t1=convertView.findViewById(R.id.titleTextView);
        TextView t2=convertView.findViewById(R.id.contentTextView);
        TextView t3=convertView.findViewById(R.id.dateTextView);

        String s1 =arr.get(position).getTitle();
        int pos;
        if(s1.length()>16)
        {
            pos=16;
            if(s1.contains("\n"))
            {
                pos= s1.indexOf("\n");
            }
            String finalString1 = s1.substring(0,pos)+"...";
            t1.setText(finalString1);
        }
        else
            t1.setText(s1);

        String s =arr.get(position).getContent();
        int pos11;
        if(s.length()>=35)
        {
            pos11=35;
            if(s.contains("\n"))
            {
                pos11=s.indexOf("\n");
            }
            String finalString = s.substring(0,pos11)+"...";
            t2.setText(finalString);
        }
        else
            t2.setText(s);

        t3.setText(arr.get(position).getDate());


        convertView.setOnClickListener(v -> {
            activity=(MainActivity) getContext();
            activity.OnNotesClicked(position);
        });
        convertView.setOnLongClickListener(v -> {
            activity=(MainActivity)getContext();
            activity.ShowDeleteDialogBox(position);
            return true;
        });
        return convertView;

    }

    private void ToggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
        } else {
            selectedItems.add(position);
        }
        notifyDataSetChanged();
    }

    // Method to clear item selection
    public void ClearSelection() {
        isActionMode = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    // Method to get the selected items
    public ArrayList<Integer> getSelectedItems() {
        return selectedItems;
    }
}
