package tk.husseinfo.ficosm.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import tk.husseinfo.ficosm.models.MissedCall
import tk.husseinfo.ficosm.models.MissedCallDAO

const val DATABASE_NAME = "mc_db"

@Database(entities = [MissedCall::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun missedCallDao(): MissedCallDAO
}
