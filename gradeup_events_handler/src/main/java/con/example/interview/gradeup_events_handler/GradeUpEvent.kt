package con.example.interview.gradeup_events_handler

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
internal data class GradeUpEvent(@PrimaryKey(autoGenerate = true) val id: Int? = null, val event: String)