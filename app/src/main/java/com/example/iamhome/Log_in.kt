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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class Log_in : AppCompatActivity() {

    private val client = OkHttpClient()
    private var token: String? = null

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val requestCode = result.data?.extras?.getInt("requestCode") ?: -1 // Replace "requestCode" with the actual key used when starting the sign-in activity
            // Handle the result here
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    // Authentication successful, get information about the Google account
                    val email = account?.email
                    val displayName = account?.displayName

                    Toast.makeText(this, "Welcome, $displayName!", Toast.LENGTH_SHORT).show()
                } catch (e: ApiException) {
                    // Authentication failed, display error message
                    Log.e(TAG, "Authentication error: ${e.statusCode}")
                    Toast.makeText(this, "Authentication error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 123
        private const val TAG = "Log_in"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)
        val imageViewClick = findViewById<ImageView>(R.id.signInWithGmail)

        imageViewClick.setOnClickListener { signInWithGoogle() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        if(email.toString().isNotEmpty())
        {
            fieldEmail.text = email.toString()
        }
    }

    private fun signInWithGoogle() {
        Log.i("Log_in", "Start fun")
        val signInIntent = googleSignInClient.signInIntent
        startActivity(signInIntent);
        signInLauncher.launch(signInIntent)
        Log.i("Log_in", "${signInLauncher.launch(signInIntent)}")
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
    }


