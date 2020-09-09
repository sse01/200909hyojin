package org.techtown.puppydiary.accountmenu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoneyDBHelper extends SQLiteOpenHelper {
    Cursor cursor = null;
    int pos = 0;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public MoneyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS mt4 (pos INTEGER, year INTEGER, month INTEGER, day INTEGER, price TEXT, memo TEXT);");

    }

    public void insert(int year, int month, int day, String price, String memo) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO mt4(year, month, day, price, memo) VALUES(" + year + ", " + month + ", " + day + ", '" + price + "', '" + memo + "');");

        cursor = db.rawQuery("SELECT * FROM mt4 WHERE year = " + year + " and month = " + month + " and day = " + day + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                pos = cursor.getCount();
            } while (cursor.moveToNext());
        }
        db.execSQL("UPDATE mt4 SET pos = " + pos + " WHERE year = " + year + " and month = " + month + " and day = " + day + " and price = '" + price + "' and memo = '" + memo + "';");
        db.close();
    }


    public void update(int pos, int year, int month, int day, String price, String memo){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        System.out.println("IN DB : " + price + ", " + memo);

        //db.execSQL("DELETE FROM mt4 WHERE pos = " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";");
        //db.execSQL("INSERT OR REPLACE INTO mt4(pos, year, month, day, price, memo) VALUES(" + pos + ", " + year + ", " + month + ", " + day + ", '" + price + "', '" + memo + "');");
        db.execSQL("UPDATE mt4 SET price = '" + price + "' and memo = '" + memo + "' WHERE pos = " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";");
        db.close();
    }

    public void delete(int pos, int year, int month, int day) {

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM mt4 WHERE pos = " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";");
        db.execSQL("UPDATE mt4 SET pos = pos-1 WHERE pos > " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";");
        db.close();
    }

    //같은 날짜에 같은 항목 있는지 체크
    public int check(int year, int month, int day, String price, String memo) {
        SQLiteDatabase db = getReadableDatabase();
        int check = 0; //중복 item 체크, 1이면 중복
        cursor = db.rawQuery("SELECT pos from mt4 WHERE year = " + year + " and month = " + month + " and day = " + day + " and price = '" + price + "' and memo = '" + memo + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getString(cursor.getColumnIndex("pos")) != null) {
                check = 1;
            }
        }
        return check;
    }

    public String memo(int pos, int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        String memo = "";

        cursor = db.rawQuery("SELECT memo from mt4 WHERE pos = " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                memo = cursor.getString(cursor.getColumnIndex("memo"));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return memo;
    }

    public String price(int pos, int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        String price = "";

        cursor = db.rawQuery("SELECT price from mt4 WHERE pos = " + pos + " and year = " + year + " and month = " + month + " and day = " + day + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                price = cursor.getString(cursor.getColumnIndex("price"));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return price;
    }

    public int getSum(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        int sum = 0;

        cursor = db.rawQuery("SELECT price from mt4 WHERE year = " + year + " and month = " + month + " and day = " + day + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                sum += cursor.getInt(cursor.getColumnIndex("price"));
            } while (cursor.moveToNext());
        }

        return sum;
    }

    public Cursor getResult(int year, int month, int day) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용
        cursor = db.rawQuery("SELECT * from mt4 WHERE year = " + year + " and month = " + month + " and day = " + day + ";", null);

        return cursor;
        /*if(cursor != null && cursor.moveToFirst()){
            do{
                result.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;*/
    }
}


