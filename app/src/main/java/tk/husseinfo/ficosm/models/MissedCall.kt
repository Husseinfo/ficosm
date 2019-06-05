package tk.husseinfo.ficosm.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date


@Entity(tableName = "missed_call")
data class MissedCall(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "contact_name") val contactName: String?,
        @ColumnInfo(name = "contact_number") val contactNumber: String?,
        @ColumnInfo(name = "count") val count: Int?,
        @ColumnInfo(name = "date") val date: Date?
)
