package com.soroush.eskandarie.musicplayer.framework.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.soroush.eskandarie.musicplayer.presentation.ui.page.lockscreen.LockScreenActivity

class LockScreenReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        when( intent?.action ){
            Intent.ACTION_SCREEN_ON ->{
                Log.e("13245", "I am awake")
                val intent = Intent(context, LockScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context?.startActivity(intent)
            }
            Intent.ACTION_SCREEN_OFF ->{

                Log.e("13245", "I am awake")
            }
            Intent.ACTION_USER_PRESENT ->{

            Log.e("13245", "I am awake")
        }
        }
    }

}