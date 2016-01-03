package net.venedi.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import net.venedi.data.repository.CourseRepository;
import net.venedi.data.repository.ExamRepository;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.model.Course;
import net.venedi.model.EStatusType;
import net.venedi.model.Exam;
import net.venedi.util.DateUtil;
import net.venedi.util.ObjectUtil;

import java.util.Date;

public class ExamEditActivity extends AppCompatActivity {

    private boolean isNew;
    private EditText etCourse, etDate, etTime, etMark, etNote, etDescription;
    private CheckBox cbExamFailed;
    private Exam exam;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCourse = (EditText)findViewById(R.id.etCourse);
        etDate = (EditText)findViewById(R.id.etDate);
        etDescription = (EditText)findViewById(R.id.etDescription);
        etTime = (EditText)findViewById(R.id.etTime);
        etMark = (EditText)findViewById(R.id.etMark);
        etNote = (EditText)findViewById(R.id.etNote);

        cbExamFailed = (CheckBox)findViewById(R.id.cbExamFailed);

        cbExamFailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    exam.setTaken(EStatusType.FAIL);
                    etMark.setText("");
                    exam.setExamResult("");
                }else {
                    exam.setTaken(etMark.getText().length()>0 ? EStatusType.SUCCESS : EStatusType.PENDING);
                }
            }
        });

        etMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    exam.setTaken(EStatusType.SUCCESS);
                    cbExamFailed.setChecked(false);
                }else{
                    exam.setTaken(EStatusType.PENDING);
                }
            }
        });

        etCourse.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CourseListActivity.class);
                intent.putExtra("selectMode", true);
                startActivityForResult(intent, 1, null);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = getLayoutInflater().inflate(R.layout.content_select_date, null, false);
                final DatePicker datePicker = (DatePicker)view.findViewById(R.id.datePicker);
                datePicker.setSpinnersShown(true);
                datePicker.setCalendarViewShown(false);
//                datePicker.setFirstDayOfWeek(1);
//                datePicker.setMinDate();

                new DatePickerDialog.Builder(ExamEditActivity.this)
                        .setTitle("Pick Date")
//                        .setMessage("Please pickup date..")
                        .setView(view)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int day = datePicker.getDayOfMonth();
                                int mount = datePicker.getMonth();
                                int year = datePicker.getYear();

                                Date selectedDate = DateUtil.getDate(day, mount, year);

                                //TODO merge time and date...

                                exam.setExamDate(selectedDate);
                                etDate.setText(DateUtil.parseDate(exam.getExamDate(), "yyyy-MM-dd"));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = getLayoutInflater().inflate(R.layout.content_select_time,null,false);
                final TimePicker timePicker = (TimePicker)view.findViewById(R.id.timePicker);

                new AlertDialog.Builder(ExamEditActivity.this)
                        .setTitle("Pick Time")
//                        .setMessage("Please pickup date..")
                        .setView(view)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                int hour = timePicker.getHour();
//                                int minute = timePicker.getMinute();
                                Log.i("D","Debug");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });


        Bundle bundle = getIntent().getExtras();
        isNew = bundle.getBoolean("isNew");

        if(!isNew) {
            //edit mode
            int examId = bundle.getInt(ExamTable.COL_ID);

            //fetch exam data
            ExamRepository repo = new ExamRepository(this);
            exam = repo.find(examId);

            //parse data from exam
            etCourse.setText(exam.getCourse().getTitle());
            etDescription.setText(exam.getDescription());
            if(exam.getExamDate()!= null) {
                etDate.setText(DateUtil.parseDate(exam.getExamDate(), "yyyy-MM-dd"));
                etTime.setText(DateUtil.parseDate(exam.getExamDate(), "HH:mm"));
            }
            if(!ObjectUtil.isEmptyOrNull(exam.getExamResult())){
                etMark.setText(exam.getExamResult());
            }
            if(!ObjectUtil.isEmptyOrNull(exam.getNote())){
                etNote.setText(exam.getNote());
            }

        }else{
            //create mode
            exam = new Exam();
            exam.setTaken(EStatusType.PENDING);

            //TODO create course with separate view

        }

        cbExamFailed.setChecked(exam.getTaken() == EStatusType.FAIL);

        //sendNotification("This is test maina");

    }


    //TODO rework me using alaram manager to schedule notify on date...
    private void sendNotification(String msg) {

        Log.d("D", "Preparing to send notification...: " + msg);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, ExamsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.status_ok)
                .setContentTitle("Student Helper")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);

        Integer NOTIFICATION_ID = 7777;

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d("D", "Notification sent successfully.");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;

            case R.id.saveExam:
                Log.i("ExamEditactivity","Saving... ");

                saveExam();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveExam() {

        ExamRepository repo = new ExamRepository(this);

        exam.setNote(String.valueOf(etNote.getText()));
        exam.setExamResult(String.valueOf(etMark.getText()));
        exam.setDescription(String.valueOf(etDescription.getText()));

        if(isNew){
            exam = repo.create(exam);
        }else{
            repo.update(exam);
        }

        //TODO show some mesage ...
        NavUtils.navigateUpFromSameTask(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exam_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuItem = menu.findItem(R.id.saveExam);
        menuItem.setTitle(isNew? "Save":"Update"); //TODO
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                int res = data.getIntExtra("countryId", 0);
                if(res > 0) {
                    CourseRepository cRep = new CourseRepository(this);
                    Course selectedCourse = cRep.find(res);
                    if(selectedCourse != null) {
                        exam.setCourse(selectedCourse);
                        etCourse.setText(selectedCourse.getTitle());
                    }
                }
            }
//            if(resultCode == Activity.RESULT_CANCELED){
//
//            }

        }

        //super.onActivityResult(requestCode, resultCode, data);
    }
}
