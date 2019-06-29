package tk.husseinfo.ficosm.models

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface MissedCallDAO {
    @Query("SELECT * FROM missed_call ORDER BY mc_date DESC")
    fun getAll(): List<MissedCall>

    @Query("DELETE FROM missed_call")
    fun deleteAll()

    @Insert
    fun insertOne(mc: MissedCall)
}
