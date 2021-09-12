package com.baokiin.mymusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.Utils.ACTION

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent?.getIntExtra(ACTION,-1)
        val intentService = Intent(context,MediaService::class.java)
        intentService.putExtra(ACTION,actionMusic)

        try{
            context?.startService(intentService)
        }catch (e:Exception){

        }

    }
}