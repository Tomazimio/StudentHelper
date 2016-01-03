package net.venedi.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.venedi.main.R;
import net.venedi.model.Course;
import net.venedi.util.DateUtil;

import java.util.List;

public class CourseArrayAdapter extends ArrayAdapter<Course> {

    private Context mContext;
    private int layoutResourseId;
    private List<Course> data = null;

    public CourseArrayAdapter(Context context, int textViewResourceId, List<Course> objects) {
        super(context, textViewResourceId, objects);

        this.mContext = context;
        this.layoutResourseId = textViewResourceId;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

        if(convertView == null){
            //inflate the layout
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourseId, parent, false);
        }

        //object item based on the position
        Course course = data.get(position);

        //get the TextView and the set the the text and tag...
        TextView textViewItem = (TextView)convertView.findViewById(R.id.tvTitle);
        textViewItem.setText(course.getTitle());
        textViewItem.setTag(course.getCourseId());

        TextView tvDescription= (TextView)convertView.findViewById(R.id.tvDescription);
        tvDescription.setText(course.getDescription());

        TextView tvDate= (TextView)convertView.findViewById(R.id.tvDate);
        tvDate.setText("Created: " + DateUtil.parseDate(course.getCreateDatetime(), "yyyy-MM-dd")); //fixme

        return convertView;

    }
}
