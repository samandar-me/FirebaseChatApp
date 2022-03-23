package uz.context.chatappfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.context.chatappfirebase.databinding.ActivitySignUpBinding
import uz.context.chatappfirebase.model.User
import uz.context.chatappfirebase.utils.toast

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        binding.btnSignUp.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val name = binding.editName.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signUp(name, email, password)
            } else {
                toast("Please enter anything!")
            }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    toast("Sign up success")
                } else {
                    toast("Some error occurred")
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDBRef = FirebaseDatabase.getInstance().reference
        mDBRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}