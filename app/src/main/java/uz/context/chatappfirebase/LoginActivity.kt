package uz.context.chatappfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import uz.context.chatappfirebase.databinding.ActivityLoginBinding
import uz.context.chatappfirebase.utils.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }
    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                toast("Please enter anything!")
            }
        }
    }
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    toast("Log in success")
                } else {
                    toast("User does not exists!")
                }
            }
    }
}