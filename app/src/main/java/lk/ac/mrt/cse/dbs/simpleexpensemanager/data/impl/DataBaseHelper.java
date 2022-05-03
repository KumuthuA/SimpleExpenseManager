package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper{
    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String COLUMN_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String COLUMN_ACCOUNT_NO = "ACCOUNT_NO";
    public static final String COLUMN_BANK = "BANK";
    public static final String COLUMN_ACCOUNT_HOLDER = "ACCOUNT_HOLDER";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String COLUMN_LOG_NUMBER = "LOG_NUMBER";
    public static final String LOGS_TABLE = "LOGS_TABLE";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_EXPENSE_TYPE = "EXPENSE_TYPE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "SimpleExpense.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to store account information
        String createTableStatement1 = "CREATE TABLE " + ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_NUMBER + " TEXT PRIMARY KEY, " + COLUMN_BANK + " TEXT, " + COLUMN_ACCOUNT_HOLDER + " TEXT, " + COLUMN_BALANCE + " REAL)";
        // Create a table to store transaction log information
        String createTableStatement2 = "CREATE TABLE " + LOGS_TABLE + " (" + COLUMN_LOG_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT," +  COLUMN_DATE + " TEXT, " + COLUMN_ACCOUNT_NO + " TEXT, "  + COLUMN_AMOUNT + " FLOAT, " + COLUMN_EXPENSE_TYPE + " TEXT)";
        //Execute SQL queries
        db.execSQL(createTableStatement1);
        db.execSQL(createTableStatement2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
