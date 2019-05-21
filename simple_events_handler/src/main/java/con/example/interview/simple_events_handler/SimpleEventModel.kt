package con.example.interview.simple_events_handler

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
internal data class SimpleEventModel(@PrimaryKey(autoGenerate = true) val id: Int? = null, val event: String)