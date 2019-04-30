package con.example.interview.gradeup_events_handler

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = [GradeUpEvent::class], version = 1)
abstract class GradeUpEventDatabase : RoomDatabase() {

    abstract internal fun gradeUpEventDao(): GradeUpEventDao

    internal companion object {
        var INSTANCE: GradeUpEventDatabase? = null

        fun getDatabase(context: Context): GradeUpEventDatabase? {
            if (INSTANCE == null) {
                synchronized(GradeUpEventDatabase::class) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext, GradeUpEventDatabase::class.java, "myDB")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}