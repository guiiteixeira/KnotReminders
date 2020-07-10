package com.example.knot.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "reminder")
class Reminder : Serializable{

    @PrimaryKey(autoGenerate = true)
    var id: Long

    @ColumnInfo(name = "title")
    var title: String

    @ColumnInfo(name = "resume")
    var resume: String

    @ColumnInfo(name = "description")
    var description: String

    @ColumnInfo(name = "creation_date")
    var creationDate: Date

    @ColumnInfo(name = "frequency")
    var frequency: Frequency

    @ColumnInfo(name = "first_alert_date")
    var firstAlertDate: Date

    constructor(
        id: Long,
        title: String,
        resume: String,
        description: String,
        creationDate: Date,
        frequency: Frequency,
        firstAlertDate: Date
    ) {
        this.id = id
        this.title = title
        this.resume = resume
        this.description = description
        this.creationDate = creationDate
        this.frequency = frequency
        this.firstAlertDate = firstAlertDate
    }
}