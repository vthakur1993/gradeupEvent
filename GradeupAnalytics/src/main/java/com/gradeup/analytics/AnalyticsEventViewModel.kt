package com.gradeup.analytics

import android.content.ContentValues
import com.gradeup.analytics.AnalyticsEventsDatabaseHandler.Companion.EVENT
import com.gradeup.analytics.AnalyticsEventsDatabaseHandler.Companion.ID
import com.gradeup.analytics.AnalyticsEventsDatabaseHandler.Companion.TABLE_GRADEUP_EVENT
import java.util.*


class AnalyticsEventViewModel {

    internal fun addEvent(emp: AnalyticsEventModel, db: AnalyticsEventsDatabaseHandler) {
        val contentValues = ContentValues()
        contentValues.put(EVENT, emp.event)
        val insert = db.writableDatabase.insert(TABLE_GRADEUP_EVENT, null, contentValues)
    }

    internal fun deleteGradeUpEventsBeforeId(id: Int, db: AnalyticsEventsDatabaseHandler) {
        db.writableDatabase.execSQL("Delete FROM $TABLE_GRADEUP_EVENT where ID <= $id")
    }

    internal fun getEvents(db: AnalyticsEventsDatabaseHandler): ArrayList<AnalyticsEventModel> {
        val eventsArraylist = ArrayList<AnalyticsEventModel>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM $TABLE_GRADEUP_EVENT", null)
        if (cursor.count > 0) {
            cursor!!.moveToFirst()
            do {
                val gradeUpEvent = AnalyticsEventModel(
                    cursor.getInt(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(EVENT))
                )
                eventsArraylist.add(gradeUpEvent)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return eventsArraylist
    }
}