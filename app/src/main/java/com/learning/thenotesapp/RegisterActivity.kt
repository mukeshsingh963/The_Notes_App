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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.learning.thenotesapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val sharedPrefFile = "userSharedPref"
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI(){
        val sharedPreferences : SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        val checkBox = binding.showPswd
        checkBox.setOnClickListener{
            if(checkBox.isChecked){
                binding.pswdUser.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                binding.pswdUser.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.loginBtn.setOnClickListener{
            var userEmail = binding.emailUser.text.toString()
            val userPassword = binding.pswdUser.text.toString()
            Log.d("Test", userEmail.toString())
            Log.d("Test", userPassword)
            if (isValidEmail(userEmail.toString())){
                if (userPassword != null && userPassword.length >= 5){

                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("userEmail", userEmail)
                    editor.putString("userPass",userPassword)
                    editor.apply()
                    editor.commit()
                    Toast.makeText(this, "You are Successfully registered in", Toast.LENGTH_SHORT).show()
                    handler.postDelayed({
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 30)
                }else{
                    Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Check Email Format", Toast.LENGTH_SHORT).show()
            }
        }
    }
}