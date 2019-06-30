package tk.husseinfo.ficosm.utils

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AlertDialog
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.ui.MainActivity

fun checkPermissions(activity: MainActivity) {
    val receiveSMSPermissionCheck = activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
    val readSMSPermissionCheck = activity.checkSelfPermission(Manifest.permission.READ_SMS)
    val readContactsPermissionCheck = activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
    if (receiveSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
            || readSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
            || readContactsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)
                || activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)
                || activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setMessage(R.string.request_receive_sms_permission_explanation)
            builder.setPositiveButton(R.string.ok) { dialog, which -> }
            builder.show()
        } else {
            activity.requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS),
                    MainActivity.REQUEST_PERMISSIONS)
        }
    } else {
        //            startBroadcastReceiver();
    }
}
