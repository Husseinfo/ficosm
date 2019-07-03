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
) {
    val time: String
        get() {
            val cal = Calendar.getInstance()
            cal.timeInMillis = this.date ?: 0
            return "%d:%d".format(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))
        }

    val day: String
        get() {
            val cal = Calendar.getInstance()
            cal.timeInMillis = this.date ?: 0
            return "%d/%d/%d".format(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
        }
}
