package com.vectortech.gestaosimples

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LockActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        
        prefsManager = PrefsManager(this)

        val unlockButton = findViewById<Button>(R.id.unlock_button)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        unlockButton.setOnClickListener {
            val password = passwordInput.text.toString()
            val correctPassword = prefsManager.getChildPassword()

            if (password == correctPassword) {
                finish() // Fecha a tela de bloqueio e libera o uso
            } else {
                Toast.makeText(this, "Senha Incorreta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Não faz nada para impedir sair
    }
}
