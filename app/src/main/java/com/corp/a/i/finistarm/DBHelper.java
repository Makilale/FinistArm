/*package com.corp.a.i.finistarm;*/
package com.corp.a.i.finistarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by GordeevMaxim on 13.04.2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    static final String dbName = "HookahArm";
    static final int dbVersion = 1;

    public DBHelper(Context context) {
        super(context, dbName , null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `Category` ( `idParent` INTEGER NOT NULL, `name` INTEGER NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE \"DeskProduct\" ( `idDesk` INTEGER NOT NULL, `idProduct` INTEGER NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE \"Desks\" ( `idHall` INTEGER NOT NULL, `name` TEXT NOT NULL,`isActual` INTEGER NOT NULL, `dateBegin` TEXT NOT NULL, `dateEnd` TEXT, `idPayment` INTEGER )");
        db.execSQL("CREATE TABLE \"Payment\" ( `name` TEXT NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE \"GroupLogin\" (`name` TEXT NOT NULL,`isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `Halls` ( `name` TEXT NOT NULL,`isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `LogChange` (`idLogin` INTEGER NOT NULL, `dateBegin` TEXT NOT NULL, `dateEnd` TEXT, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `Login` ( `idGroup` INTEGER NOT NULL, `login` TEXT NOT NULL, `pass` TEXT NOT NULL, `name` TEXT NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE \"Products\" ( `idCategory` INTEGER NOT NULL, `name` TEXT NOT NULL, `price` INTEGER NOT NULL, `isActual` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE \"EnterLog\" ( `idLogin` INTEGER NOT NULL, `dateBegin` TEXT NOT NULL, `dateEnd` TEXT)");
        db.execSQL("CREATE TABLE \"Stock\" (`idCategory` INTEGER NOT NULL, `name` TEXT NOT NULL, `amount` INTEGER NOT NULL, `idValue` INTEGER NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `StockCategory` ( `idParent` INTEGER NOT NULL, `name` TEXT NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `Technology` ( `idProduct` INTEGER NOT NULL, `idProductStock` INTEGER, `weight` INTEGER NOT NULL, `isActual` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `Value` ( `name` TEXT NOT NULL, `isActual` INTEGER NOT NULL )");

        db.execSQL("INSERT INTO \"GroupLogin\" VALUES (\"Программист\", \"1\")");
        db.execSQL("INSERT INTO \"Login\" VALUES (\"1\",\"Godho\", \"2121123q\", \"Programmer\", \"1\")");
        db.execSQL("INSERT INTO `Category` VALUES (\"0\",\"Кальяны\", \"1\")");
        db.execSQL("INSERT INTO `Category` VALUES (\"0\",\"Бар\",\"1\" )");
        db.execSQL("INSERT INTO \"Stock\" VALUES (\"1\",\"Dark Side\",\"5000\",\"1\",\"1\")");
        db.execSQL("INSERT INTO \"Stock\" VALUES (\"1\",\"Daily Hookah\",\"5000\",\"1\",\"1\")");
        db.execSQL("INSERT INTO \"Stock\" VALUES (\"1\",\"Tangiers\",\"5000\",\"1\",\"1\")");
        db.execSQL("INSERT INTO `Value` VALUES (\"Гр.\",\"1\")");
        db.execSQL("INSERT INTO `Value` VALUES (\"Мл.\",\"1\")");
        db.execSQL("INSERT INTO `Value` VALUES (\"Шт.\", \"1\")");
        db.execSQL("INSERT INTO `Halls` VALUES (\"Зал №1\", \"1\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS `DateBegin`");
        db.execSQL("DROP TABLE IF EXISTS DeskProduct");
        db.execSQL("DROP TABLE IF EXISTS Desks");
        db.execSQL("DROP TABLE IF EXISTS GroupLogin");
        db.execSQL("DROP TABLE IF EXISTS Halls");
        db.execSQL("DROP TABLE IF EXISTS Login");
        db.execSQL("DROP TABLE IF EXISTS Log");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Report");
        db.execSQL("DROP TABLE IF EXISTS ReportDolg");
        db.execSQL("DROP TABLE IF EXISTS Stock");
        db.execSQL("DROP TABLE IF EXISTS StockCategory");
        db.execSQL("DROP TABLE IF EXISTS Technology");
        db.execSQL("DROP TABLE IF EXISTS Value");
        onCreate(db);
    }
}
