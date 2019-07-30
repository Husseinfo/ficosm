package tk.husseinfo.ficosm.ui

import android.arch.persistence.room.Room
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
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
import android.app.ProgressDialog


class MainActivity : AppCompatActivity() {

    @BindView(R.id.history_view)
    protected lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MCListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val db = null

    protected fun getDatabase(context: Context): AppDatabase {
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
            Synchronizer(this).execute()
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

    private class Synchronizer(private val activity: MainActivity) : AsyncTask<Void?, Void?, Void?>() {
        private lateinit var progressDialog: ProgressDialog
        override fun doInBackground(vararg params: Void?): Void? {
            activity.getDatabase(activity).missedCallDao().deleteAll()
            val messages = getAllMessages(activity)
            for (message in messages)
                activity.getDatabase(activity).missedCallDao().insertOne(message)
            activity.runOnUiThread(Runnable {
                activity.recyclerView.adapter = MCListAdapter(messages)
            })
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("Loading...")
            progressDialog.isIndeterminate = false
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
        }
    }
}
