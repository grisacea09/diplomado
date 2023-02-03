package com.intercam.folklore.view.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.intercam.folklore.R
import com.intercam.folklore.adapter.VestimentaAdapter
import com.intercam.folklore.companion.Api
import com.intercam.folklore.databinding.ActivityMainBinding
import com.intercam.folklore.model.Ubicacion
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

    companion object {
        var email = ""
        var entidad = arrayOfNulls<String>(13)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        intent.extras?.let {
            if (it.containsKey("mail")) {
              val mail = it.getString("mail")
               email= mail.toString()
                Log.i("main", "el email es :"+email)

            }
        }

        Glide.with(this@MainActivity)
            .load("https://imagenes.elpais.com/resizer/bG7q-LMtX8dla6SqvKvQ0sqBd78=/1960x1470/cloudfront-eu-central-1.images.arcpublishing.com/prisa/DYU3PKV7HJBF7NB7GCJDI5YTHI.jpg")
            .circleCrop()
            .into(binding.profile)

        binding.tvUsuario.text = email

        CoroutineScope(Dispatchers.IO).launch {
            val call =Api.service.getVestimenta()

            call.enqueue(object : Callback<ArrayList<vestimenta>> {
                override fun onResponse(
                    call: Call<ArrayList<vestimenta>>,
                    response: Response<ArrayList<vestimenta>>
                ) {
                    Log.d(TAG, "respuesta del server: ${response.toString()}")
                    Log.d(TAG, "datos del server: ${response.body().toString()}")
                    for(i in 0..12){
                        entidad[i]= response.body()?.get(i)?.estado
                    }

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
                    Log.e(TAG, ""+response.body()?.latitud+ response.body()?.longitud)
                    Log.e(TAG, ""+response.body()?.desc_danza)

                    val intent = Intent(context, DetalleVideo::class.java).apply {
                        putExtra("ID", ves.id)
                        putExtra("ETNIA", ves.etnia)
                        putExtra("DANZA", response.body()?.danza)
                        putExtra("DESC_DANZA", response.body()?.desc_danza)
                        putExtra("VIDEO_DANZA", response.body()?.video_danza)
                        putExtra("VESTIDO", response.body()?.vestido)
                        putExtra("INFORMACION", response.body()?.informacion)
                            putExtra("LATITUD", response.body()?.latitud )
                            putExtra("LONGITUD", response.body()?.longitud )
                        var index= ves.id-1
                        putExtra("ENTIDAD", entidad[index] )


                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
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