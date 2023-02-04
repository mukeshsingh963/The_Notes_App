package com.learning.thenotesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.learning.thenotesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val handler = Handler(Looper.getMainLooper())
    private val sharedPrefFile = "userSharedPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        val checkBox = binding.showPswd
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                binding.pswdUser.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.pswdUser.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.loginBtn.setOnClickListener {
            var userEmail = binding.emailUser.text.toString()
            val userPassword = binding.pswdUser.text.toString()
            val sharedEmailValue = sharedPreferences.getString("userEmail", "default")
            val sharedPassValue = sharedPreferences.getString("userPass", "default")
            Log.d("Test", userEmail.toString())
            Log.d("Test", userPassword)
            if (isValidEmail(userEmail.toString())) {
                if (userPassword != null && userPassword.length >= 5) {
                    if (userEmail == sharedEmailValue && userPassword == sharedPassValue) {
                        Toast.makeText(this, "You are Successfully logged in", Toast.LENGTH_SHORT)
                            .show()
                        handler.postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 3)
                    }else{
                        Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Check Email Format", Toast.LENGTH_SHORT).show()
            }
        }

        binding.userRegister.setOnClickListener{
            handler.postDelayed({
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            },1)
        }
    }

}


