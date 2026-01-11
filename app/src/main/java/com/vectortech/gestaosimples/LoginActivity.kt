package com.vectortech.gestaosimples

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.provider.Settings
import android.content.ClipData
import android.content.ClipboardManager

class LoginActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        prefsManager = PrefsManager(this)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Se já estiver logado, vai direto para a Main
        if (prefsManager.isLoggedIn()) {
            startMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val deviceIdInput = findViewById<EditText>(R.id.device_id_input)
        val btnCopyId = findViewById<Button>(R.id.btn_copy_deviceid)

        // Detecta ID do dispositivo automaticamente (ANDROID_ID)
        val autoId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        if (!autoId.isNullOrEmpty() && deviceIdInput.text.isNullOrEmpty()) {
            deviceIdInput.setText(autoId)
        }

        btnCopyId.setOnClickListener {
            val id = deviceIdInput.text.toString()
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("deviceId", id))
            Toast.makeText(this, "ID copiado", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Simulação de Autenticação (No futuro, conectar ao Firebase)
            val deviceId = deviceIdInput.text.toString()

            if (email.isEmpty() || password.isEmpty() || deviceId.isEmpty()) {
                Toast.makeText(this, "Preencha email, senha e ID do dispositivo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        prefsManager.setLoggedIn(email)
                        prefsManager.setDeviceId(deviceId)
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        startMainActivity()
                    } else {
                        Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
