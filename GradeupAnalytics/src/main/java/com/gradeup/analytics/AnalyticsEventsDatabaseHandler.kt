package com.gradeup.analytics

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SimpleEventsDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_GRADEUP_EVENT + "("
                + ID + " INTEGER PRIMARY KEY autoincrement," + EVENT + " TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADEUP_EVENT)
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "GradeupDatabase"
        val TABLE_GRADEUP_EVENT = "GradeupEventTable"
        val ID = "id"
        val EVENT = "event"
    }
}