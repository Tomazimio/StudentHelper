package net.venedi.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.venedi.data.IRepository;
import net.venedi.data.repository.table.CourseTable;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.model.Course;
import net.venedi.model.EStatusType;
import net.venedi.model.Exam;
import net.venedi.util.DateUtil;
import net.venedi.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

public class ExamRepository extends BaseRepository implements IRepository<Exam> {

    private Cursor cursor;

    public ExamRepository(Context context) {
        super(context);
    }

    //TODO
    //String groupBy, String having, String orderBy, String limit

    @Override
    public List<Exam> all() {

        List<Exam> result = new ArrayList<>();


//        cursor = getDb().query(ExamTable.NAME, new String[]{ExamTable.COL_ID, ExamTable.COL_DESCRIPTION},null,null,null,null,null);


        String sql = String.format("SELECT * FROM %s AS t1 INNER JOIN %s AS t2 ON t1.%s = t2.%s ",
                ExamTable.NAME,
                CourseTable.NAME,
                ExamTable.COL_COURSE_ID,
                CourseTable.COL_ID
        );
        cursor = getDb().rawQuery(sql, null);

        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {

            Exam exam = parseExam();
            result.add(exam);

            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    private Exam parseExam() {
        Course course = new Course();
        course.setCourseId(cursor.getInt(8));
        course.setTitle(cursor.getString(9));
        course.setDescription(cursor.getString(10));
        course.setCreateDatetime(DateUtil.parseDate(cursor.getString(11)));

        Exam exam = new Exam();
        // Take values from the DB
        exam.setExamId(cursor.getInt(0));
        exam.setCourse(course);
        exam.setDescription(cursor.getString(2));
        exam.setNote(cursor.getString(3));
        exam.setTaken(EStatusType.getById(cursor.getInt(4)));//TODO cursor.getString(4)
        exam.setExamDate(DateUtil.parseDate(cursor.getString(5)));
        exam.setExamResult(cursor.getString(6));
        exam.setCreateDatetime(DateUtil.parseDate(cursor.getString(7)));
        return exam;
    }

    @Override
    public Exam create(Exam exam) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(ExamTable.COL_COURSE_ID, exam.getCourse().getCourseId());
            cv.put(ExamTable.COL_DESCRIPTION, exam.getDescription());
            cv.put(ExamTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());
            cv.put(ExamTable.COL_EXAM_DATE, DateUtil.parseDate(exam.getExamDate()));
            cv.put(ExamTable.COL_NOTE, exam.getNote());
            cv.put(ExamTable.COL_TAKEN, exam.getTaken().getTypeId());
            long row = getDb().insert(ExamTable.NAME, null, cv);

            //return row == -1 ? false : true;

            //TODO fetch created object by row...

        }catch (Exception e){
            //TODO
        }

        return exam;
    }

    @Override
    public boolean update(Exam exam) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(ExamTable.COL_COURSE_ID, exam.getCourse().getCourseId());
            cv.put(ExamTable.COL_DESCRIPTION, exam.getDescription());
            cv.put(ExamTable.COL_EXAM_DATE, DateUtil.parseDate(exam.getExamDate()));
            cv.put(ExamTable.COL_NOTE, exam.getNote());
            cv.put(ExamTable.COL_TAKEN, exam.getTaken().getTypeId());
            cv.put(ExamTable.COL_EXAM_RESULT, exam.getExamResult());

            String filter = ExamTable.COL_ID + "=" + exam.getExamId();
            int res = getDb().update(ExamTable.NAME, cv, filter,null);

            return res > 0 ? true : false;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Exam find(int id) {

        Exam exam = null;

        String sql = String.format("SELECT * FROM %s AS t1 INNER JOIN %s AS t2 ON t1.%s = t2.%s WHERE t1.%s = %s",
                ExamTable.NAME,
                CourseTable.NAME,
                ExamTable.COL_COURSE_ID,
                CourseTable.COL_ID,
                ExamTable.COL_ID,
                String.valueOf(id)
        );
        cursor = getDb().rawQuery(sql, null);
        if(cursor != null && cursor.moveToFirst()){
            exam = parseExam();
        }

        return exam;
    }

    @Override
    public boolean delete(Exam exam) {
        String filter = ExamTable.COL_ID + "=" + exam.getExamId();
        int res = getDb().delete(ExamTable.NAME, filter, null);
        return res > 0 ? true : false;
    }

}