package com.intercam.folklore.view.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.intercam.folklore.adapter.VestimentaAdapter
import com.intercam.folklore.companion.Api
import com.intercam.folklore.databinding.ActivityMainBinding
import com.intercam.folklore.model.detalleVestimenta
import com.intercam.folklore.model.vestimenta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity() : AppCompatActivity() {
    //YouTubeBaseActivity()
    private lateinit var binding: ActivityMainBinding
   // lateinit var  youTubePlayerInit: YouTubePlayer.OnInitializedListener
   private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            val call =Api.service.getVestimenta()

            call.enqueue(object : Callback<ArrayList<vestimenta>> {
                override fun onResponse(
                    call: Call<ArrayList<vestimenta>>,
                    response: Response<ArrayList<vestimenta>>
                ) {
                    Log.d(TAG, "respuesta del server: ${response.toString()}")
                    Log.d(TAG, "datos del server: ${response.body().toString()}")

                    val gametmp: vestimenta


                    binding.pbConexion.visibility = View.GONE
                    binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvMenu.adapter = VestimentaAdapter(this@MainActivity, response.body()!!)
                }

                override fun onFailure(call: Call<ArrayList<vestimenta>>, t: Throwable) {
                    binding.pbConexion.visibility = View.GONE

                    Log.i(TAG,"No hay conexion error: ${t.message}")
                    Toast.makeText(
                        this@MainActivity,
                        "No hay conexion error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }


    }

    fun selectedVestimenta(ves: vestimenta) {
        Log.i(TAG,"seleccionaste?: "+ ves.id)
var context = this.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            val call = Api.service.getVestimentaDetailApiary(ves.id)

            call.enqueue(object : Callback<detalleVestimenta> {
                override fun onResponse(
                    call: Call<detalleVestimenta>,
                    response: Response<detalleVestimenta>
                ) {
                    Log.d(TAG, "respuesta del server: ${response.toString()}")
                    Log.d(TAG, "datos del server: ${response.body().toString()}")



                    val intent = Intent(context, DetalleVideo::class.java).apply {
                        putExtra("ID", ves.id)
                        putExtra("ETNIA", ves.etnia)
                        putExtra("DANZA", response.body()?.danza)
                        putExtra("DESC_DANZA", response.body()?.desc_danza)
                        putExtra("VIDEO_DANZA", response.body()?.video_danza)
                        putExtra("VESTIDO", response.body()?.vestido)
                        putExtra("INFORMACION", response.body()?.informacion)
                        putExtra("UBICACION", response.body()?.ubicacion)
                    }
                    startActivity(intent)
                    finish()

                }

                override fun onFailure(call: Call<detalleVestimenta>, t: Throwable) {
                    Log.i(TAG,"No hay conexion error: ${t.message}")
                    Toast.makeText(
                        this@MainActivity,
                        "No hay conexion error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }


        //hay que ir al otro fragment
        //con fragment no me salio lo del reproductor de youtube :(
        //error al inflar el objeto de youtube :(
        //Hay que llaamar al activity con el intent y pasar dos datos el id y el nombre de la etnia

    }

}