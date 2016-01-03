package net.venedi.main.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.venedi.main.R;
import net.venedi.model.EStatusType;
import net.venedi.model.Exam;
import net.venedi.util.DateUtil;

import java.util.List;

public class ExamArrayAdapter extends ArrayAdapter<Exam> {

    private Context mContext;
    private int layoutResourseId;
    private List<Exam> data = null;

    public ExamArrayAdapter(Context context, int textViewResourceId, List<Exam> objects) {
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
        Exam exam = data.get(position);

        //get the TextView and the set the the text and tag...
        TextView textViewItem = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView tvCourse = (TextView)convertView.findViewById(R.id.tvCourse);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        ImageView ivStatus = (ImageView)convertView.findViewById(R.id.ivStatus);
        TextView tvMark = (TextView) convertView.findViewById(R.id.tvMark);

        TextView tvTime = (TextView)convertView.findViewById(R.id.tvTime);
        textViewItem.setText(exam.getDescription());
        textViewItem.setTag(exam.getExamId());

        tvCourse.setText(exam.getCourse().getTitle());

        String date = DateUtil.parseDate(exam.getExamDate(),"dd MMM");
        tvDate.setText(date);

        String time = DateUtil.parseDate(exam.getExamDate(),"HH:mm");;
        tvTime.setText(time);

        factorStatusImage(exam, ivStatus);

        tvMark.setText(exam.getExamResult());

        return convertView;

    }

    private void factorStatusImage(Exam exam, ImageView ivStatus) {
        EStatusType status = exam.getTaken();
        switch (status){
            case PENDING:
                ivStatus.setImageResource(R.drawable.status_pending);
                break;
            case SUCCESS:
                ivStatus.setImageResource(R.drawable.status_ok);
                break;
            case FAIL:
                ivStatus.setImageResource(R.drawable.status_fail);
                break;
        }
    }
}
