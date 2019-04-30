package con.example.interview.gradeup_events_handler

import android.arch.persistence.room.*

@Dao
internal interface GradeUpEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gradeUpEvent: GradeUpEvent)

    @Delete
    fun delete(gradeUpEvent: GradeUpEvent)

    @Query("SELECT * FROM GradeUpEvent order by id")
    fun getGradeUpEvents(): List<GradeUpEvent>

    @Query("Delete FROM GradeUpEvent where id <= :id")
    fun deleteGradeUpEventsBeforeId(id: Int)

}