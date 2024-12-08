package com.soroush.eskandarie.musicplayer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission()
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.Blue
//                ) {
//                    Column {
//                        var progress by remember { mutableStateOf(0f) }
//
//                        ProfileHeader(progress = progress)
//                        Spacer(modifier = Modifier.height(32.dp))
//                        Slider(
//                            value = progress,
//                            onValueChange = { progress = it },
//                            modifier = Modifier.padding(horizontal = 32.dp)
//                        )
//                    }
//                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showNotification()
            } else {
                // Handle the case when permission is not granted
                // You can show a message to the user explaining why it's needed
            }
        }
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun showNotification() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, "1")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Hello, Notification!")
            .setContentText("This is your first notification.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(12, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Simple Notification Channel"
            val descriptionText = "Channel for simple notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
