package com.example.musicplay

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var totalduration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        while(nid.isClickable)
//        {
//            mp = MediaPlayer.create(this, R.raw.music)
//        }
//        while(bid.isClickable){
//            mp = MediaPlayer.create(this, R.raw.music)
//        }

        mp = MediaPlayer.create(this, R.raw.music)  //variable for play music
        mp.isLooping = true         //for pause and start button to play
        mp.setVolume(0.5f, 0.5f)  //If you want to set the volume to no sound then pass (0f,0f)

                                                        //If you want to set the volume to full sound then pass (1f,1f)
        totalduration = mp.duration                 //.duration is the id of seekbar duration of the music

        sound.setOnSeekBarChangeListener(           //when click button start or pause we implement 3 override fun
            object : SeekBar.OnSeekBarChangeListener
            {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, change: Boolean) {  //I change p0 to seekbar, p1 to progress

                    if(change)    //if we click s
                    {
                        var volume = progress/100.0f  //declare volume to half when we open our app first time
                        mp.setVolume(volume, volume)  //set volume
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}

            }
        )

        duration.max = totalduration            //maximum = totalduration (simple) depend on the source
        duration.setOnSeekBarChangeListener(       //when we hold the seekbar of the duration of music
            object : SeekBar.OnSeekBarChangeListener{  //implement 3 override fun
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, change: Boolean) {  //change same as the code above

                    if(change)  //when we hold to move seekbar
                    {
                        mp.seekTo(progress)   //it will be going to the duration and being fine
                    }

                }

                override fun onStartTrackingTouch(p0: SeekBar?) {} //empty

                override fun onStopTrackingTouch(p0: SeekBar?) {}  //empty

            }
        )

        Thread(Runnable {    //The  explicit version is passing an anonymous implementation of Runnable
            while(mp != null)
            {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }catch (e: InterruptedException){}
            }
        }).start()

    }

    var handler = @SuppressLint("HandlerLeak")  //for handler above
    object : Handler()
    {
        override fun handleMessage(msg: Message) {
            var currentP = msg.what

            duration.progress = currentP

            var timeN = createTime(currentP)
            time.text = timeN    //return the duration of the song 00:00

            var timeN1 = createTime(totalduration - currentP )
            time1.text = "-$timeN1"  //opposite of the code above

        }
    }

    fun createTime(timeN : Int): String  //code above
    {
        var tm = ""  //empty string
        var mt = timeN / 1000 / 60
        var sec = timeN / 1000 % 60

        tm = "$mt:"  //tm = mt that we calculate
        if (sec < 10) tm += "0" //0:00
        tm += sec

        return tm
    }

    fun start(v:View)
    {
        if(mp.isPlaying)
        {
            mp.pause()  //can pause
            ss.setBackgroundResource(R.drawable.music)
        }
        else
        {
            mp.start() //can start
            ss.setBackgroundResource(R.drawable.music)
        }
    }
}
