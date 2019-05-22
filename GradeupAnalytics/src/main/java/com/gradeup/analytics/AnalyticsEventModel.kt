package com.gradeup.analytics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
internal data class AnalyticsEventModel(@PrimaryKey(autoGenerate = true) val id: Int? = null, val event: String)