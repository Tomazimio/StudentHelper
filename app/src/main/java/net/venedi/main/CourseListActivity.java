package net.venedi.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import net.venedi.data.repository.CourseRepository;
import net.venedi.main.adapter.CourseArrayAdapter;
import net.venedi.model.Course;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private static final String TAG = "CourseListActivity";
    private boolean isSelectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CourseRepository repo = new CourseRepository(this);
        final List<Course> courseList = repo.all();

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        SwipeMenuCreator creator = getSwipeMenuCreator();
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        CourseArrayAdapter adapter = new CourseArrayAdapter(this, R.layout.list_view_row_course, courseList);
        listView.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        isSelectMode = bundle.getBoolean("selectMode");

        if(isSelectMode) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final Course course = courseList.get(position);

                    //Select course and back it.. some how !!
                    Log.i(TAG, "select course...");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("countryId", course.getCourseId());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //swipe menu listener
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                final Course course = courseList.get(position);

                switch (index) {
                    case 0:
                        //edit

                        final View view = getLayoutInflater().inflate(
                                R.layout.content_edit_course,null,false);

                        final EditText courseTitle = (EditText)view.findViewById(R.id.etCourseTitle);
                        final EditText courseDescription = (EditText)view.findViewById(R.id.etCourseDescription);
                        courseTitle.setText(course.getTitle());
                        courseDescription.setText(course.getDescription());

                        new AlertDialog.Builder(CourseListActivity.this)
                                .setTitle("Edit Course")
                                        //.setMessage("Please select some..")
                                .setView(view)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //update...
                                        CourseRepository repo = new CourseRepository(CourseListActivity.this);
                                        course.setTitle(String.valueOf(courseTitle.getText()));
                                        course.setDescription(String.valueOf(courseDescription.getText()));
                                        repo.update(course);
                                        refresh();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private SwipeMenuCreator getSwipeMenuCreator() {
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                if(!isSelectMode) {
                    SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                    editItem.setBackground(new ColorDrawable(Color.rgb(20, 20, 20)));
                    editItem.setWidth(dp2px(80));
                    editItem.setTitle("Edit");
                    editItem.setTitleSize(18);
                    editItem.setTitleColor(Color.WHITE);
                    menu.addMenuItem(editItem);
                }

            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_list, menu);
        if(isSelectMode) {
            MenuItem menuItem = (MenuItem)menu.findItem(R.id.add);
            menuItem.setEnabled(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){

            case R.id.add:

                final View view = getLayoutInflater().inflate(
                        R.layout.content_edit_course,null,false);

                final EditText courseTitle = (EditText)view.findViewById(R.id.etCourseTitle);
                final EditText courseDescription = (EditText)view.findViewById(R.id.etCourseDescription);

                AlertDialog alert = new AlertDialog.Builder(CourseListActivity.this)
                        .setTitle("Add Course")
                                //.setMessage("Please select some..")
                        .setView(view)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save...
                                CourseRepository repo = new CourseRepository(CourseListActivity.this);
                                Course course = new Course();
                                course.setTitle(String.valueOf(courseTitle.getText()));
                                course.setDescription(String.valueOf(courseDescription.getText()));
                                repo.create(course);
                                refresh();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void refresh(){
        //TODO must refresh listView some how!??

    }

}
