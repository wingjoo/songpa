package com.example.wing.workingsongpa.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wing on 2016-12-26.
 */

public class DBmanager extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.wing.workingsongpa/databases/";
    private static String DB_NAME = "working_songpa";
    private SQLiteDatabase myDataBase;
    private final Context myContext;



    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBmanager(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }


    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();

        if (dbExist)
        {
            // do someting
        }else
        {
            //이부분에서 뻑나는것으로 보임
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e)
            {
                throw  new Error("Error copying DB");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e)
        {

        }

        if (checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException
    {
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int lenght;
        while ((lenght = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, lenght);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close(){
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //public Method

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);

        //Cursor c = db.query("student", null, null, null, null, null, null);
        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원 "
                    + cursor.getString(3)
                    + "\n";
        }

        return result;
    }

}
