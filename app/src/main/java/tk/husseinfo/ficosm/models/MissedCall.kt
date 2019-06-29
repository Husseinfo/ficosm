package tk.husseinfo.ficosm.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "missed_call")
data class MissedCall(
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "contact_name") val contactName: String?,
        @ColumnInfo(name = "contact_number") val contactNumber: String?,
        @ColumnInfo(name = "mc_count") val count: Int?,
        @ColumnInfo(name = "in_contacts") val inContacts: Boolean = true,
        @ColumnInfo(name = "mc_date") val date: Long?
){
    val time: String
    get() {
        val d = Date()
        d.time = this.date?:0
        return "%d:%d".format(d.hours, d.minutes)
    }

    val day: String
        get() {
            val d = Date()
            d.time = this.date?:0
            return "%d/%d/%d".format(d.day, d.month, d.year)
        }
}
