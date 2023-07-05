package com.example.myapplication6.ui.login

import com.example.myapplication6.R
import com.example.myapplication6.databinding.ActivityLoginBinding
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication6.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 계정 생성 버튼
        binding.joinButton.setOnClickListener {
            createAccount(binding.joinEmail.text.toString(),binding.joinPassword.text.toString())
        }
    }

    // 계정 생성
    private fun createAccount(email: String, password: String) {
        val name = binding.joinName.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        val databaseReference =
                            userId?.let {
                                FirebaseDatabase.getInstance().reference.child("users").child(
                                    it
                                )
                            }
                        if (databaseReference != null) {
                           databaseReference.child("name").setValue(name)
                        }
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}