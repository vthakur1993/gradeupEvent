package com.gradeup.analytics

import android.content.ContentValues
import com.gradeup.analytics.SimpleEventsDatabaseHandler.Companion.EVENT
import com.gradeup.analytics.SimpleEventsDatabaseHandler.Companion.ID
import com.gradeup.analytics.SimpleEventsDatabaseHandler.Companion.TABLE_GRADEUP_EVENT
import java.util.*


class SimpleEventViewModel {

    internal fun addEvent(emp: SimpleEventModel, db: SimpleEventsDatabaseHandler) {
        val contentValues = ContentValues()
        contentValues.put(EVENT, emp.event)
        val insert = db.writableDatabase.insert(TABLE_GRADEUP_EVENT, null, contentValues)
    }

    internal fun deleteGradeUpEventsBeforeId(id: Int, db: SimpleEventsDatabaseHandler) {
        db.writableDatabase.execSQL("Delete FROM $TABLE_GRADEUP_EVENT where ID <= $id")
    }

    internal fun getEvents(db: SimpleEventsDatabaseHandler): ArrayList<SimpleEventModel> {
        val eventsArraylist = ArrayList<SimpleEventModel>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM $TABLE_GRADEUP_EVENT", null)
        if (cursor.count > 0) {
            cursor!!.moveToFirst()
            do {
                val gradeUpEvent = SimpleEventModel(
                    cursor.getInt(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(EVENT))
                )
                eventsArraylist.add(gradeUpEvent)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return eventsArraylist
    }
}