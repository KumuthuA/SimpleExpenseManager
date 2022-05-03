package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DataBaseHelper implements AccountDAO{
    public PersistentAccountDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List <String> accountNumbersList = new ArrayList<>();
        String queryString = "SELECT " + COLUMN_ACCOUNT_NUMBER + " FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do{
                String accountNumber = cursor.getString(0);
                accountNumbersList.add(accountNumber);
            }while (cursor.moveToNext());
        }
        else{
            //failure. nothing to add
        }
        cursor.close();
        db.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List <Account> accountList = new ArrayList<>();
        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do{
                String accountNumber = cursor.getString(0);
                String bank = cursor.getString(1);
                String accountHolder = cursor.getString(2);
                double balance = cursor.getFloat(3);

                Account newAccount = new Account(accountNumber,bank,accountHolder,balance);
                accountList.add(newAccount);

            }while (cursor.moveToNext())
;        }
        else{
            //failure. nothing to add
        }
        cursor.close();
        db.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = " SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NUMBER + " =\""  + accountNo+"\";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        Account account = null;
        if (cursor.moveToFirst()){
            String accountNumber = cursor.getString(0);
            String bank = cursor.getString(1);
            String accountHolder = cursor.getString(2);
            double balance = cursor.getFloat(3);
            cursor.close();
            db.close();
            account = new Account(accountNumber,bank,accountHolder,balance);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NUMBER, account.getAccountNo());
        cv.put(COLUMN_BANK, account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, account.getBalance());

        db.insert(ACCOUNT_TABLE,null,cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NUMBER + " = \"" + accountNo + "\";";

        Cursor cursor = db.rawQuery(queryString,null);
//        if (!cursor.moveToFirst()) {
//            String msg = "Account " + accountNo + " is invalid.";
//            throw new InvalidAccountException(msg);
//        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        double balance = account.getBalance();

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                balance = balance - amount;
                break;
            case INCOME:
                balance = balance + amount;
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + ACCOUNT_TABLE + " SET " + COLUMN_BALANCE + " = " + balance + " WHERE " + COLUMN_ACCOUNT_NUMBER + " =\"" + accountNo +"\";";
        Cursor cursor = db.rawQuery(queryString,null);
//        if (!cursor.moveToFirst()) {
//            String msg = "Account " + accountNo + " is invalid.";
//            throw new InvalidAccountException(msg);
 //       }
    }
}
