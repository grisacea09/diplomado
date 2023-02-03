package com.intercam.folklore.view.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.firebase.auth.FirebaseAuth
import com.intercam.folklore.databinding.ActivityLoginBinding
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.intercam.folklore.R
import java.io.IOException
import java.security.GeneralSecurityException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    //Shared preferences encriptadas
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var encryptedSharedPrefsEditor: SharedPreferences.Editor

    //Para las shared preferences
    private var usuarioSp: String? = ""
    private var contraseniaSp: String? = ""

    //Lo que ingresa el usuario
    private var email = ""
    private var contrasenia = ""


    val TAG = LoginActivity::class.java.getSimpleName()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toda la logica para autenticarnos con firebase y el correo
        firebaseAuth = FirebaseAuth.getInstance()


        try {
            //Creando la llave para encriptar
            val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedSharedPreferences = EncryptedSharedPreferences
                .create(
                    this,
                    "account",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences
        }catch(e: GeneralSecurityException){
            e.printStackTrace()
            Log.d(TAG, "Error: ${e.message}")
        }catch (e: IOException){
            e.printStackTrace()
            Log.d(TAG, "Error: ${e.message}")
        }

        encryptedSharedPrefsEditor = encryptedSharedPreferences.edit()
        usuarioSp = encryptedSharedPreferences.getString("usuarioSp", "0")
        contraseniaSp = encryptedSharedPreferences.getString("contraseniaSp", "0")



    binding.btnLogin.setOnClickListener {
        if(!validaCampos()) return@setOnClickListener

        binding.progressBar.visibility = View.VISIBLE

        autenticaUsuario(email, contrasenia)
    }

        binding.btnRegistrarse.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    //Enviar correo para verificación de email
                    var user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        showErrorAlert("El correo de verificación ha sido enviado")
                       // Toast.makeText(this, "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        showErrorAlert("No se pudo enviar el correo de verificación")
                        //Toast.makeText(this, "No se pudo enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("psw", contrasenia)
                    intent.putExtra("mail", email)
                    startActivity(intent)
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    finish()


                }else{
                    binding.progressBar.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

        binding.tvRestablecerPassword.setOnClickListener {
            val resetMail = EditText(it.context)

            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            val passwordResetDialog = AlertDialog.Builder(it.context)
            passwordResetDialog.setTitle("Restablecer contraseña")
            passwordResetDialog.setMessage("Ingrese su correo para recibir el enlace para restablecer")
            passwordResetDialog.setView(resetMail)

            passwordResetDialog.setPositiveButton(
                "Enviar",
                DialogInterface.OnClickListener { dialog, which ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "El enlace para restablecer la contraseña ha sido enviado a su correo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "El enlace no se ha podido enviar: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Favor de ingresar la dirección de correo",
                            Toast.LENGTH_SHORT
                        ).show() //it tiene la excepción
                    }
                })

            passwordResetDialog.setNegativeButton(
                "Cancelar",
                DialogInterface.OnClickListener { dialog, which ->

                })

            passwordResetDialog.create().show()


        }

    }//fin oncreate

    private fun validaCampos(): Boolean{
        email = binding.tietEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = "Se requiere el correo"
            binding.tietEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContrasenia.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }


    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            errorCode = "NO_NETWORK"
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, "Error: El correo electrónico no tiene un formato correcto", Toast.LENGTH_SHORT).show()
                binding.tietEmail.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = "La contraseña no es válida"
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }



            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                showErrorAlert("Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso")
                Toast.makeText(this, "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.tietEmail.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                showErrorAlert("La sesión ha expirado. Favor de ingresar nuevamente")
                //Toast.makeText(this, "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                showErrorAlert("Error: No existe el usuario con la información proporcionada.")
                   // Toast.makeText(this, "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.tietContrasenia.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContrasenia.requestFocus()
            }
            "NO_NETWORK" -> {
                showErrorAlert( "Red no disponible o se interrumpió la conexión")

            }
            else -> {
                showErrorAlert( "No se pudo autenticar exitosamente.")
               // Toast.makeText(this, "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun autenticaUsuario(usr: String, psw: String){

        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if(authResult.isSuccessful){
                //Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, MainActivity::class.java))
                //finish()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("psw", psw)
                intent.putExtra("mail", email)
                startActivity(intent)
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish()

            }else{
                binding.progressBar.visibility = View.GONE
                manejaErrores(authResult)
            }
        }
    }


    private fun showErrorAlert( msg: String){
        android.app.AlertDialog.Builder(this).setTitle("Error")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Aceptar", null)
            .show()
    }

}