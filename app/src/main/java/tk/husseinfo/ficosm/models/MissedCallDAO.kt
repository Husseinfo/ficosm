package tk.husseinfo.ficosm.models

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface MissedCallDAO {
    @Query("SELECT * FROM MissedCall")
    fun getAll(): List<MissedCall>

    @Query("DELETE FROM MissedCall")
    fun deleteAll()

    @Insert
    fun insertOne(mc: MissedCall)
}
