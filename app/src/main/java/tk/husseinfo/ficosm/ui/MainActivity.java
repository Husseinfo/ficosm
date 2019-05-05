package tk.husseinfo.ficosm.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import tk.husseinfo.ficosm.R;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int receiveSMSPermissionCheck = this.checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        int readSMSPermissionCheck = this.checkSelfPermission(Manifest.permission.READ_SMS);
        int readContactsPermissionCheck = this.checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (receiveSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
                || readSMSPermissionCheck != PackageManager.PERMISSION_GRANTED
                || readContactsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)
                    || this.shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)
                    || this.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage(R.string.request_receive_sms_permission_explanation);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            } else {
                this.requestPermissions(new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_CONTACTS},
                        REQUEST_PERMISSIONS);
            }
        } else {
//            startBroadcastReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startBroadcastReceiver();
            } else {
                Toast.makeText(this, R.string.request_receive_sms_permission_explanation, Toast.LENGTH_LONG).show();
            }
        }
    }

}
