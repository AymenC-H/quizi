package beta.project.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Quiz.db";
    private static final String TABLE1 = "score";
    private static final String TABLE1_COL1 = "name_c TEXT";
    private static final String TABLE1_COL2 = "value DOUBLE";
    private static final String TABLE1_COL3 = "id_qu INTEGER";
    private static final String TABLE1_COL4 = "date_score INTEGER";
    private static final String TABLE1_COL5 = "time_taken INTEGER";
    private static final String TABLE2 = "child";
    //private static final String TABLE2_COL1 = "id_c INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TABLE2_COL1 = "name_c TEXT PRIMARY KEY";
    private static final String TABLE3 = "questions";
    private static final String TABLE3_COL1 = "id_q INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TABLE3_COL2 = "name_q TEXT";
    private static final String TABLE3_COL3 = "answer1 TEXT";
    private static final String TABLE3_COL4 = "answer2 TEXT";
    private static final String TABLE3_COL5 = "answer3 TEXT";
    private static final String TABLE3_COL6 = "image TEXT";
    private static final String TABLE4 = "quiz";
    private static final String TABLE4_COL1 = "id_qu INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TABLE4_COL2 = "name_qu TEXT";
    private static final String TABLE4_COL3 = "id_q INTEGER";
    private static final String TABLE4_COL4 = "date_q INTEGER";
    private static final String TABLE4_COL5 = "date_q_updated INTEGER";
    private static final String TABLE4_COL6 = "max_time INTEGER";
    private static final int DATABASE_VERSION = 1;
    Cursor cursorc, cursor, cursorids;


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE2 + " (" + TABLE2_COL1 +");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE3 + " (" + TABLE3_COL1 + ", " + TABLE3_COL2 + ", " + TABLE3_COL3 + ", " + TABLE3_COL4 + "," + TABLE3_COL5 + ", " + TABLE3_COL6 + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE4 + " (" + TABLE4_COL1 + ", " + TABLE4_COL2 + ", " + TABLE4_COL3 + ", " + TABLE4_COL4 +", " + TABLE4_COL5 + ", " + TABLE4_COL6 + ", FOREIGN KEY (" + TABLE4_COL3.split(" ")[0] + ") REFERENCES " + TABLE3 + "(" + TABLE3_COL1.split(" ")[0] + ") ON UPDATE CASCADE ON DELETE CASCADE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE1 + " ( ids INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE1_COL1 + ", " + TABLE1_COL2 + ", " + TABLE1_COL3 + ", " + TABLE1_COL4 + ", " + TABLE1_COL5 +", FOREIGN KEY (" + TABLE1_COL1.split(" ")[0] + ") REFERENCES " + TABLE2 + "(" + TABLE2_COL1.split(" ")[0] + ") ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (" + TABLE1_COL3.split(" ")[0] + ") REFERENCES " + TABLE4 + "(" + TABLE4_COL1.split(" ")[0] + ") ON UPDATE CASCADE ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    void addQuestion(String qu, String ans1, String ans2, String ans3, String img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE3_COL2.split(" ")[0], qu);
        values.put(TABLE3_COL3.split(" ")[0], ans1);
        values.put(TABLE3_COL4.split(" ")[0], ans2);
        values.put(TABLE3_COL5.split(" ")[0], ans3);
        values.put(TABLE3_COL6.split(" ")[0], img);
        db.insert(TABLE3, null, values);
        db.close();
        //return commit != -1;
    }

    void updateQuestion(String qu, String ans1, String ans2, String ans3, int id_q, String img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE3_COL2.split(" ")[0], qu);
        values.put(TABLE3_COL3.split(" ")[0], ans1);
        values.put(TABLE3_COL4.split(" ")[0], ans2);
        values.put(TABLE3_COL5.split(" ")[0], ans3);
        values.put(TABLE3_COL6.split(" ")[0], img);
        db.update(TABLE3, values, TABLE3_COL1.split(" ")[0] + "=" + id_q, null);
        db.close();
    }

    void removeQuestion(List<Integer> ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int id : ids) {
            db.delete(TABLE3, TABLE3_COL1.split(" ")[0] + "=" + id, null);
        }
        db.close();
    }

    void removeQuizz(List<String> ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (String id : ids) {
            db.delete(TABLE4, TABLE4_COL2.split(" ")[0] + " = ?", new String[]{id});
        }
        db.close();
    }

    List<Quest> getQuestions() {
        List<Quest> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE3 + " order by " + TABLE3_COL1.split(" ")[0] + ";", null);//+" WHERE "+TABLE3_COL1+" = ?", new String[]{key});
        int id;
        String qq;
        String ans1;
        String ans2;
        String ans3;
        String img;
        //String [][] res= new String[cursor.getCount()][4];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                qq = cursor.getString(1);
                ans1 = cursor.getString(2);
                ans2 = cursor.getString(3);
                ans3 = cursor.getString(4);
                img = cursor.getString(5);
                res.add(i++, new Quest(id, qq, ans1, ans2, ans3, img));
                //res[i++]=new String[]{qq,ans1,ans2,ans3};
            } while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    Quest getQuestions(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE3 + " WHERE " + TABLE3_COL1.split(" ")[0] + " = " + id + ";", null);
        String qq;
        String ans1;
        String ans2;
        String ans3;
        String url;
        Quest res=null;
        if (cursor.moveToFirst()) {
            qq = cursor.getString(1);
            ans1 = cursor.getString(2);
            ans2 = cursor.getString(3);
            ans3 = cursor.getString(4);
            url = cursor.getString(5);
            res = new Quest(id, qq, ans1, ans2, ans3, url);
            cursor.close();
        }
        return res;
    }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        void addScore(String id_c,int id_qu, Double value,int timetk) {
            Date date = new Date();
            values.put(TABLE1_COL1.split(" ")[0], id_c);
            values.put(TABLE1_COL2.split(" ")[0], value);
            values.put(TABLE1_COL3.split(" ")[0], id_qu);
            values.put(TABLE1_COL4.split(" ")[0], date.getTime() / 1000L);
            if (timetk!=0)values.put(TABLE1_COL5.split(" ")[0], timetk);
            db.insert(TABLE1, null, values);
            db.close();
    }

    void addChild(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE2_COL1.split(" ")[0], name);
        db.insert(TABLE2, null, values);
        db.close();
    }

    boolean existChild(String child) {
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE2_COL1.split(" ")[0] + " FROM " + TABLE2 + " WHERE " + TABLE2_COL1.split(" ")[0] +"= ?;", new String[]{child});
        if (cursor.moveToFirst())return false;
        return true;
    }

    /*String getChild(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE2_COL1.split(" ")[0] + " FROM " + TABLE2 + " WHERE " + TABLE2_COL1.split(" ")[0] + " = ?", new String[]{Integer.toString(id)});
        String res = "";
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            res = cursor.getString(2);
            cursor.close();
        }
        return res;
    }*/
    /*boolean addQuiz(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE4_COL2.split(" ")[0], name);
        try {
            db.insert(TABLE2, null, values);
        } catch (SQLiteConstraintException e) {
            return true;
        }
        db.close();
        return false;
    }*/

    boolean addQuiz(String name, List<Integer> idqs,int time) {
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE4_COL2.split(" ")[0] + " FROM " + TABLE4 + " WHERE " + TABLE4_COL2.split(" ")[0] + "=?", new String[]{name});
        if (cursor.getCount() != 0) return true;
        db = this.getWritableDatabase();
        Date date = new Date();
        for (int id : idqs) {
            ContentValues values = new ContentValues();
            values.put(TABLE4_COL2.split(" ")[0], name);
            values.put(TABLE4_COL3.split(" ")[0], id);
            values.put(TABLE4_COL4.split(" ")[0], date.getTime() / 1000L);
            if (time>=10 & time <=7200)values.put(TABLE4_COL6.split(" ")[0], time);
            try {
                db.insert(TABLE4, null, values);
            } catch (SQLiteConstraintException e) {
                Log.d("addQuiz", "attention: " + e.getMessage());
                return true;
            }
        }
        db.close();
        return false;
    }
    boolean updateQuiz(String name, List<Integer> idqs, int time) {
        Log.d("addQuiz", name + ":" + idqs.size());
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE4_COL2.split(" ")[0] + " FROM " + TABLE4 + " WHERE " + TABLE4_COL2.split(" ")[0] + "=?", new String[]{name});
        if (cursor.getCount() != 0) return true;
        db = this.getWritableDatabase();
        Date date = new Date();
        for (int id : idqs) {
            ContentValues values = new ContentValues();
            values.put(TABLE4_COL2.split(" ")[0], name);
            values.put(TABLE4_COL3.split(" ")[0], id);
            values.put(TABLE4_COL5.split(" ")[0], date.getTime() / 1000L);
            if (time>=10)values.put(TABLE4_COL6.split(" ")[0], time);
            try {
                db.update(TABLE4, values, TABLE4_COL2.split(" ")[0] + " = ? AND " + TABLE4_COL3.split(" ")[0] + " = "+id, new String[]{name});
            } catch (SQLiteConstraintException e) {
                Log.d("addQuiz2", "attention2: " + e.getMessage());
                return true;
            }
        }
        db.close();
        return false;
    }

    /*List<Quizz> getQuiz() {List<Quizz> res=new ArrayList<Quizz>();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT idqu,"+TABLE4_COL2.split(" ")[0]+",count(*) FROM "+TABLE4+";",null);
        //String [] res= new String[cursor.getCount()];
        int i=0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                cursorc = db.rawQuery("SELECT ids,count(*) FROM "+TABLE1+" where "+cursor.getString(0)+"="+TABLE1_COL1.split(" ")[0]+";",null);
                res.add(new Quizz(cursor.getString(1),cursor.getInt(2)));//res[i++]=cursor.getString(0);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }*/
    /*List<Quizz> getQuiz(int id) {
        List<Quizz> res = new ArrayList<Quizz>();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE3_COL2.split(" ")[0] + TABLE3_COL1.split(" ")[0] + " FROM " + TABLE4 + " WHERE " + TABLE3_COL1 + "=" + id + ");", null);
        //String [] res= new String[cursor.getCount()];
        int i = 0;
        int[] idqs = new int[cursor.getCount()];
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            do {
                idqs[i] = cursor.getInt(1);
            } while (cursor.moveToNext());
            res.add(new Quizz(id, name, 0, 0, idqs));
            cursor.close();
        }
        return res;
    }*/
    Quizz getQuiz(String idn) {
        Quizz resqu = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE4_COL2.split(" ")[0] +","+ TABLE4_COL1.split(" ")[0]+ ", "+ TABLE4_COL6.split(" ")[0] +" FROM " + TABLE4 + " WHERE "+ TABLE4_COL2.split(" ")[0] +" = ? ", new String[]{idn});//+" WHERE "+TABLE4_COL1.split(" ")[0]+" = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            cursorids = db.rawQuery("SELECT " + TABLE4_COL3.split(" ")[0] + " FROM " + TABLE4 + " WHERE ? =" + TABLE4_COL2.split(" ")[0], new String[]{cursor.getString(0)});
            int[] idqs = new int[cursorids.getCount()];
            int i = 0;
            if (cursorids != null && cursorids.moveToFirst()) {
                do {
                    idqs[i++] = cursorids.getInt(0);
                } while (cursorids.moveToNext());
                cursorc = db.rawQuery("SELECT count(*) FROM " + TABLE1 + " WHERE " + TABLE4_COL1.split(" ")[0]+" = "+cursor.getInt(1),null);
                resqu=new Quizz(cursor.getInt(1),cursor.getString(0), (cursorc.moveToFirst()?cursorc.getInt(0):0), idqs,cursor.getInt(2));
                }
            //res[i++]=cursor.getString(0);
            cursor.close();
        }
        return resqu;
    }
    List<Quizz> getQuiz() {
        List<Quizz> resqu = new ArrayList<>();int[] idqs;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + TABLE4_COL2.split(" ")[0] + "," + TABLE4_COL1.split(" ")[0]+ "," + TABLE4_COL6.split(" ")[0] + ",count(*) FROM " + TABLE4 + " group by "+ TABLE4_COL2.split(" ")[0], null);//+" WHERE "+TABLE4_COL1.split(" ")[0]+" = ?", new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            do {
                cursorids = db.rawQuery("SELECT " + TABLE4_COL3.split(" ")[0] + " FROM " + TABLE4 + " WHERE ? =" + TABLE4_COL2.split(" ")[0], new String[]{cursor.getString(0)});
                idqs = new int[cursorids.getCount()];
                int i = 0;
                if (cursorids != null && cursorids.moveToFirst()) {
                    do {
                        idqs[i++] = cursorids.getInt(0);
                    } while (cursorids.moveToNext());
                    cursorc = db.rawQuery("SELECT count(*) FROM " + TABLE1 + " WHERE " + TABLE1_COL3.split(" ")[0]+" = ?", new String[]{cursor.getString(0)});
                    resqu.add(new Quizz(cursor.getInt(1),cursor.getString(0), (cursorc.moveToFirst()?cursorc.getInt(0):0), idqs,cursor.getInt(2)));
                }
            }while (cursor.moveToNext());
                    //res[i++]=cursor.getString(0);
                    cursor.close();
                }
            return resqu;
        }
}
