package tk.husseinfo.ficosm.ui

import android.arch.persistence.room.Room
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import butterknife.BindView
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.db.AppDatabase
import tk.husseinfo.ficosm.db.DATABASE_NAME
import tk.husseinfo.ficosm.utils.checkPermissions
import tk.husseinfo.ficosm.utils.getAllMessages

class MainActivity : AppCompatActivity() {

    @BindView(R.id.history_view)
    protected lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MCListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val db = null

    private fun getDatabase(context: Context): AppDatabase {
        return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions(this)
        viewAdapter = MCListAdapter(getDatabase(applicationContext).missedCallDao().getAll())
        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.history_view).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            syncDb()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //                startBroadcastReceiver();
            } else {
                Toast.makeText(this, R.string.request_receive_sms_permission_explanation, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSIONS = 1
    }

    fun syncDb() {
        getDatabase(this).missedCallDao().deleteAll()
        val messages = getAllMessages(this)
        for(message in messages)
            getDatabase(this).missedCallDao().insertOne(message)
        recyclerView.adapter = MCListAdapter(messages)
    }
}
