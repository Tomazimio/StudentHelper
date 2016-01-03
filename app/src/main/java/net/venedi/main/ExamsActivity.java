package net.venedi.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.venedi.data.repository.ExamRepository;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.main.adapter.ExamArrayAdapter;
import net.venedi.model.Exam;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ExamsActivity extends AppCompatActivity {

    static Logger logger = Logger.getLogger("Exams");

    private ExamArrayAdapter adapter;
    private List<Exam> examList;
    private SwipeMenuListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exams);

        //config slide menu
//        SlideMenu slideMenu = new SlideMenu(this);
//        setContentView(slideMenu);
//
//        // Setup the content
//        View contentView = getCurrentFocus();
//        slideMenu.addView(contentView, new SlideMenu.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, LayoutParams.ROLE_CONTENT));
//
//        // Setup the primary menu
//        View primaryMenu = new View(this);
//        slideMenu.addView(primaryMenu, new SlideMenu.LayoutParams(300, LayoutParams.MATCH_PARENT, LayoutParams.ROLE_PRIMARY_MENU));
//
//        // Setup the secondary menu
//        View secondaryMenu = new View(this);
//        slideMenu.addView(secondaryMenu, new SlideMenu.LayoutParams(300, LayoutParams.MATCH_PARENT, LayoutParams.ROLE_SECONDARY_MENU));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add new discipline
                logger.log(Level.INFO, "Add new discipline ");

                //Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(),ExamEditActivity.class);
                intent.putExtra("isNew",true);
                startActivity(intent);

                //Snackbar.make(view, "Add new discipline... ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        listview = (SwipeMenuListView) findViewById(R.id.listView);

        //fetch data
        final ExamRepository examData = new ExamRepository(this);
        examList = examData.all();

        SwipeMenuCreator creator = getSwipeMenuCreator();

        listview.setMenuCreator(creator);
        listview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        adapter = new ExamArrayAdapter(this, R.layout.list_view_row_exam, examList);
        listview.setAdapter(adapter);

        //swipe menu listener
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                Exam exam = examList.get(position);

                switch (index) {
                    case 0:
                        //edit
//                        logger.log(Level.INFO, "Select Item with title: " + menu.getMenuItem(index).getTitle());
                        Intent intent = new Intent(ExamsActivity.this.getBaseContext(),ExamEditActivity.class);
                        intent.putExtra("isNew",false);
                        intent.putExtra(ExamTable.COL_ID, exam.getExamId());
                        startActivity(intent);
                        break;
                    case 1:
                        //delete
                        //logger.log(Level.INFO, "Select Item with title: " + menu.getMenuItem(index).getTitle());
                        examData.delete(exam);

                        //TODO must refresh listView some how!??
                        finish();
                        Intent sintent = new Intent(ExamsActivity.this.getBaseContext(), ExamsActivity.class);
                        startActivity(sintent);

                        break;
                }
                //false : close the menu;
                //true : no close the menu


                reloadAllData();

                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Context context = view.getContext();
//                TextView textViewItem = ((TextView) view.findViewById(R.id.tvTitle));
//                String listItemText = textViewItem.getText().toString();
//                String listItemId = textViewItem.getTag().toString();
                Exam exam = examList.get(position);
                Intent intent = new Intent(getBaseContext(),ExamDetailsActivity.class);
                intent.putExtra(ExamTable.COL_ID, exam.getExamId());
                startActivity(intent);

            }
        });

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                logger.log(Level.INFO,"click on Item:" + item);
//
////                final Context context = ExamsActivity.this.getBaseContext();
//                Intent intent = new Intent(getBaseContext(),ExamDetailsActivity.class);
//                startActivity(intent);
//                //finish();
//            }
//        });
    }

    private void reloadAllData(){

        adapter.notifyDataSetChanged();

//        adapter.clear();
//        adapter.addAll(examList);
//
//        // fire the event
//        adapter.notifyDataSetChanged();
    }

    private SwipeMenuCreator getSwipeMenuCreator() {

        return new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {

//                    SwipeMenuItem actionItem = new SwipeMenuItem(getApplicationContext());
//                    // set item background
//                    actionItem.setBackground(new ColorDrawable(Color.rgb(10, 220, 10)));
//                    // set item width
//                    actionItem.setWidth(dp2px(80));
//                    // set item title
//                    actionItem.setTitle("Action");
//                    // set item title fontsize
//                    actionItem.setTitleSize(18);
//                    // set item title font color
//                    actionItem.setTitleColor(Color.WHITE);
//                    // add to menu
//                    menu.addMenuItem(actionItem);

                    SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());

                    // set item background
                    editItem.setBackground(new ColorDrawable(Color.rgb(20, 20, 20)));
                    // set item width
                    editItem.setWidth(dp2px(80));
                    // set item title
                    editItem.setTitle("Edit");
                    // set item title fontsize
                    editItem.setTitleSize(18);
                    // set item title font color
                    editItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(editItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(dp2px(80));
                    // set item title
                    deleteItem.setTitle("Delete");
                    // set item title fontsize
                    deleteItem.setTitleSize(18);
                    // set item title font color
                    deleteItem.setTitleColor(Color.WHITE);
                    // set a icon
    //                deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){

            case R.id.action_settings:
                return true;
            case R.id.courses:
                Intent intent = new Intent(getBaseContext(), CourseListActivity.class);
                intent.putExtra("selectMode",false);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static int dp2px(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
