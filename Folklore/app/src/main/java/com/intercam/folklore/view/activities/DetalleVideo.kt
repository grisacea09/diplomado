package com.intercam.folklore.view.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.intercam.folklore.databinding.ActivityDetalleVideoBinding


class DetalleVideo : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var binding: ActivityDetalleVideoBinding
private lateinit var yp: YouTubePlayer
    private val TAG = "DetalleVideo"

    companion object{

        val api_key = ""
        var id_etnia = 0
        var etnia = ""
        var danza = ""
        var desc_danza = ""
        var informacion = ""
        var ubicacion= ""
        var video_danza = ""
        var vestido = ""


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)




        intent.extras?.let {
            if (it.containsKey("ID")) {
                val id = it.getInt("ID")
                id_etnia = id
            }
            if (it.containsKey("ETNIA")) {
                val et = it.getString("ETNIA")
                etnia = et.toString()
            }
            if (it.containsKey("DANZA")) {
                val et = it.getString("DANZA")
                danza = et.toString()
            }
            if (it.containsKey("DESC_DANZA")) {
                val et = it.getString("DESC_DANZA")
                desc_danza = et.toString()
            }
            if (it.containsKey("VIDEO_DANZA")) {
                val et = it.getString("VIDEO_DANZA")
                video_danza = et.toString()
            }

            if (it.containsKey("UBICACION")) {
                val et = it.getString("UBICACION")
                ubicacion = et.toString()
            }

            if (it.containsKey("INFORMACION")) {
                val et = it.getString("INFORMACION")
                informacion = et.toString()
            }
            if (it.containsKey("VESTIDO")) {
                val et = it.getString("VESTIDO")
                vestido = et.toString()
            }

        }


        binding.tvTitle.text = etnia
        binding.tvdanza.text = danza
        binding.tvDescdanza.text = desc_danza

        Glide.with(this@DetalleVideo)
            .load(vestido)
            .circleCrop()
            .into(binding.image)

        binding.info.text= informacion
        Glide.with(this@DetalleVideo)
            .load(ubicacion)
            .into(binding.mapaUbicacion)

            binding.vvVideo.initialize(api_key, this)

        binding.atras.setOnClickListener{
            Log.i("detalle", "atras")
            val intent = Intent(this@DetalleVideo, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (p1 != null) {
            p1.loadVideo(video_danza)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.i("error DetalleVideo", p1.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}


