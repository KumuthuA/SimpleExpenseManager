package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DataBaseHelper implements TransactionDAO {
    public PersistentTransactionDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO, accountNo);
        cv.put(COLUMN_DATE, date.toString());
        cv.put(COLUMN_EXPENSE_TYPE, expenseType.toString());
        cv.put(COLUMN_AMOUNT, amount);

        db.insert(LOGS_TABLE,null,cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List <Transaction> transactionList = new ArrayList<>();
        String queryString = "SELECT * FROM " + LOGS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do{
                String dateInString = cursor.getString(1);
                String accountNumber = cursor.getString(2);
                String expenseType = cursor.getString(4);
                double amount = cursor.getDouble(3);

                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = null;
                try{
                    date = formatter.parse(dateInString);
                }catch (Exception e){ e.printStackTrace(); }

                ExpenseType expenseType1 = expenseType.equals("EXPENSE")? ExpenseType.EXPENSE :ExpenseType.INCOME;

                Transaction newTransaction = new Transaction(date, accountNumber, expenseType1, amount);
                transactionList.add(newTransaction);

            }while (cursor.moveToNext());
        }
        else{
            //failure. nothing to add
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List <Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);

    }
}
