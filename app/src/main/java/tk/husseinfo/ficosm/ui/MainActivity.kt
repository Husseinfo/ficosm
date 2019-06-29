package tk.husseinfo.ficosm.ui

import android.Manifest
import android.arch.persistence.room.Room
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import butterknife.BindView
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.db.AppDatabase
import tk.husseinfo.ficosm.db.DATABASE_NAME

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

        val receiveSMSPermissionCheck = this.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
        val readSMSPermissionCheck = this.checkSelfPermission(Manifest.permission.READ_SMS)
        val readContactsPermissionCheck = this.checkSelfPermission(Manifest.permission.READ_CONTACTS)
        if (receiveSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
                || readSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
                || readContactsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)
                    || this.shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)
                    || this.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setMessage(R.string.request_receive_sms_permission_explanation)
                builder.setPositiveButton(R.string.ok) { dialog, which -> }
                builder.show()
            } else {
                this.requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS),
                        REQUEST_PERMISSIONS)
            }
        } else {
            //            startBroadcastReceiver();
        }


        viewAdapter = MCListAdapter(getDatabase(applicationContext).missedCallDao().getAll())
        recyclerView = findViewById<RecyclerView>(R.id.history_view).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //                startBroadcastReceiver();
            } else {
                Toast.makeText(this, R.string.request_receive_sms_permission_explanation, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {

        private const val REQUEST_PERMISSIONS = 1
    }

}
