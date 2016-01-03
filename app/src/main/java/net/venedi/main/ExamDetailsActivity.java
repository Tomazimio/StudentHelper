package net.venedi.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.venedi.data.repository.ExamRepository;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.model.Exam;

public class ExamDetailsActivity extends AppCompatActivity {

    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setLogo(R.mipmap.t_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);


        Bundle bundle = getIntent().getExtras();
        int examId = bundle.getInt(ExamTable.COL_ID);
        ExamRepository repo = new ExamRepository(this);
        exam = repo.find(examId);

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

            case R.id.edit:
                Log.i("ExamEditactivity", "Start edit mode");
                Intent intent = new Intent(getBaseContext(),ExamEditActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra(ExamTable.COL_ID, exam.getExamId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exam_details, menu);
        return true;
    }

}
