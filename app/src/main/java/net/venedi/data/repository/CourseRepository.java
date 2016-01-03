package net.venedi.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.venedi.data.IRepository;
import net.venedi.data.repository.table.CourseTable;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.model.Course;
import net.venedi.model.Exam;
import net.venedi.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository extends BaseRepository implements IRepository<Course> {

    private static String TAG = "CourseRepository";
    private Cursor cursor;

    public CourseRepository(Context context) {
        super(context);
    }

    @Override
    public List<Course> all() {

        List<Course> result = new ArrayList<>();

        cursor = getDb().query(CourseTable.NAME, new String[]{CourseTable.COL_ID, CourseTable.COL_TITLE, CourseTable.COL_DESCRIPTION, CourseTable.COL_CREATE_DATETIME},null,null,null,null,null);
        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {
            Course course = new Course();
            // Take values from the DB
            course.setCourseId(cursor.getInt(0));
            course.setTitle(cursor.getString(1));
            course.setDescription(cursor.getString(2));
            course.setCreateDatetime(DateUtil.parseDate(cursor.getString(3)));
            result.add(course);

            cursor.moveToNext();
        }

        cursor.close();

        return result;
    }

    @Override
    public Course create(Course course) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(CourseTable.COL_TITLE, course.getTitle());
            cv.put(CourseTable.COL_DESCRIPTION, course.getDescription());
            cv.put(CourseTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());

            long row  = getDb().insert(CourseTable.NAME, null, cv);

            //return row == -1 ? false : true;

            //TODO fetch created object by row...

        }catch (Exception e){
            //TODO
        }

        return course;
    }

    @Override
    public boolean update(Course course) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(CourseTable.COL_TITLE, course.getTitle());
            cv.put(CourseTable.COL_DESCRIPTION, course.getDescription());
            cv.put(CourseTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());

            String filter = CourseTable.COL_ID + "=" + course.getCourseId();
            int res = getDb().update(CourseTable.NAME, cv, filter,null);

            return res > 0 ? true : false;

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public Course find(int id) {

        Course course = null;

        String sql = String.format("SELECT * FROM %s WHERE %s = %s",
                CourseTable.NAME,
                CourseTable.COL_ID,
                String.valueOf(id)
        );

        cursor = getDb().rawQuery(sql, null);

        if(cursor != null && cursor.moveToFirst()){
            course = new Course();
            course.setCourseId(cursor.getInt(0));
            course.setTitle(cursor.getString(1));
            course.setDescription(cursor.getString(2));
            course.setCreateDatetime(DateUtil.parseDate(cursor.getString(3)));
        }

        return course;
    }

    @Override
    public boolean delete(Course object) {
        return false;
    }

}
