package com.example.iamhome

import android.content.Context
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient


@Suppress("DEPRECATION")
class Log_in : AppCompatActivity(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var googleApiClient: GoogleApiClient

    private val client = OkHttpClient()
    private var token: String? = null

    companion object{
        const val REQUEST_CODE_GOOGLE_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)
        val imageViewClick = findViewById<ImageView>(R.id.signInWithGmail)

        imageViewClick.setOnClickListener {  signInWithGoogle() }

        if(email.toString().isNotEmpty())
        {
            fieldEmail.text = email.toString()
        }
    }

    private fun signInWithGoogle() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        googleSignInClient.signOut().addOnCompleteListener {
            startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result != null) {
                handleGoogleSignInResult(result)
            }
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount

            val randomIntent = Intent(this, HomePage::class.java)
            startActivity(randomIntent)

            val googleId = account?.id ?: ""
            Log.i("Log_in", "Google id = $googleId")
            val googleFirstName = account?.givenName ?: ""
            Log.i("Log_in", "Google FirstName = $googleFirstName")
            val googleLastName = account?.familyName ?: ""
            Log.i("Log_in", googleLastName)
            val googleEmail = account?.email ?: ""
            Log.i("Log_in", googleEmail)
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Log_in", googleProfilePicURL)
            val googleIdToken = account?.idToken ?: ""
            Log.i("Log_in", googleIdToken)

            // Виконання запиту на сервер для створення аккаунта
            val url = "http://192.168.0.192:8080/api/v1/google?email=$googleEmail"
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            Log.i("Log_in", "request$request")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Обробка помилок під час виконання запиту
                    runOnUiThread {
                        Toast.makeText(this@Log_in, "Failed to create account", Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onResponse(call: Call, response: Response) {
                    // Обробка відповіді від сервера
                    val responseBody = response.body?.string()
                    if (response.isSuccessful) {
                        // Аккаунт успішно створений
                        runOnUiThread {
                            Log.i("Log_in", "$responseBody")
                            val randomIntent = Intent(this@Log_in, HomePage::class.java)
                            startActivity(randomIntent)
                        }
                    } else {
                        // Помилка створення аккаунта
                        runOnUiThread {
                            Toast.makeText(this@Log_in, "Failed to create account", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } else {
            // Sign-in failed, handle accordingly
        }
    }

    //Відкритя форми реєстрації
    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, RegisterWin::class.java)

        startActivity(randomIntent)
    }

    fun readData(view: View)
    {
        val randomIntent = Intent(this, HomePage::class.java)
        val randomIntentVerif = Intent(this, Confirmation_Form::class.java)

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)
        val fieldPassword = findViewById<TextView>(R.id.textPasswordLogIn)

        val userEmail = fieldEmail.text
        val userPassword = fieldPassword.text
        if(userEmail.toString().isNotEmpty() && userPassword.toString().isNotEmpty())
        {
            Log.i("LogIn", "StartReadData")
            val requestBody = FormBody.Builder()
                .add("email", userEmail.toString())
                .add("password", userPassword.toString())
                .build()
            Log.i("LogIn", "StartFeelQueryInfo")
            Log.i("LogIn", "${userEmail.toString()} ${userPassword.toString()}")
            val request = Request.Builder()
                .url("http://192.168.0.192:8080/api/v1/login")
                .post(requestBody)
                .build()

            Log.i("LogIn", "StartQuery")
            Log.i("LogIn", "$request")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@Log_in, "Registration successful", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i("LogIn", "StartonResponse")
                    val body = response.body?.string()
                    Log.i("LogIn", "Body- $body")
                    val json = JSONObject(body)

                    if (json.has("token")) {
                        token = json.getString("token")
                        Log.i("LogIn", "$token")
                        // Збереження токену на пристрої
                        Log.i("LogIn", "SaveToken")

                        // Відкриття іншої активності або виконання іншого коду, що потребує авторизації
                            randomIntent.putExtra("token", token)
                            randomIntent.putExtra("email", userEmail.toString()) // Assuming you have the email variable
                        startActivity(randomIntent)
                    } else {
                        // Обробка помилок
                        val errorMessage = json.getString("message")
                        Log.i("LogIn", "Error message: $errorMessage")
                        if(errorMessage == "Please verify your email before logging in")
                        {
                                randomIntentVerif.putExtra("email", userEmail.toString()) // Assuming you have the email variable
                            startActivity(randomIntentVerif)
                        }
                    }
                }
            })
        } else Toast.makeText(this@Log_in, "Data was incorrect", Toast.LENGTH_SHORT).show()
        }

    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }
}


