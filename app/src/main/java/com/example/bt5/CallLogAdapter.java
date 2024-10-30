package com.example.bt5;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import android.provider.CallLog;

public class CallLogAdapter extends ArrayAdapter<CallLogClass> {
    int resource;
    private List<CallLogClass> contacts;

    public CallLogAdapter(Context context, int resource, List<CallLogClass> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(this.resource, null);
        }
        CallLogClass s = getItem(position);

        if (s != null) {
            ImageView calllogImage = (ImageView) v.findViewById(R.id.callLogTypeImage);
            TextView callLogNumber = (TextView) v.findViewById(R.id.callLogNumber);
            TextView callLogTime = (TextView) v.findViewById(R.id.callLogTime);

            if (calllogImage != null) {
                switch (s.getType()) {
                    case CallLog.Calls.INCOMING_TYPE:
                        // Set an image resource for incoming calls
                        calllogImage.setImageResource(R.drawable.in_coming_call);
                        break;

                    case CallLog.Calls.OUTGOING_TYPE:
                        // Set an image resource for outgoing calls
                        calllogImage.setImageResource(R.drawable.out_going_call);
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        // Set an image resource for missed calls
                        calllogImage.setImageResource(R.drawable.missed_call);
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        // Set an image resource for rejected calls
                        calllogImage.setImageResource(R.drawable.rejected_call);
                        break;
                    default:
                        // Set a default icon if none of the above match
                        break;

                }
            }
            if (callLogNumber != null) {
                callLogNumber.setText(s.getNumber());
            }
            if (callLogTime != null) {
                callLogTime.setText(s.getTime());
            }

        }
        return v;
    }

}