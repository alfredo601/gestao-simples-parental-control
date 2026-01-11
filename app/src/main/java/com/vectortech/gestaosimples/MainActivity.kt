package com.vectortech.gestaosimples

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        val btnEnableAdmin = findViewById<Button>(R.id.btn_enable_admin)
        val btnEnableAccessibility = findViewById<Button>(R.id.btn_enable_accessibility)
        val btnOpenAppSettings = findViewById<Button>(R.id.btn_open_app_settings)
        val btnDisableBlocking = findViewById<Button>(R.id.btn_disable_blocking)
        val statusText = findViewById<TextView>(R.id.status_text)

        btnEnableAdmin.setOnClickListener {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Necessário para impedir desinstalação.")
            startActivity(intent)
        }

        btnEnableAccessibility.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "Ative o serviço 'Gestão Simples'", Toast.LENGTH_LONG).show()
        }

        btnOpenAppSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + packageName)
            startActivity(intent)
            Toast.makeText(this, "Na tela do app, habilite 'Permitir configurações restritas' e volte para Acessibilidade", Toast.LENGTH_LONG).show()
        }

        btnDisableBlocking.setOnClickListener {
            PrefsManager(this).disableBlockingForMinutes(15)
            Toast.makeText(this, "Bloqueio desativado por 15 minutos", Toast.LENGTH_LONG).show()
            updateStatus(statusText)
        }

        FirebaseApp.initializeApp(this)
        RemoteConfigSync(PrefsManager(this)).start()

        updateStatus(statusText)
    }

    override fun onResume() {
        super.onResume()
        val statusText = findViewById<TextView>(R.id.status_text)
        updateStatus(statusText)
    }

    private fun updateStatus(statusText: TextView) {
        val prefs = PrefsManager(this)
        val id = prefs.getDeviceId()
        val pwd = prefs.getChildPassword()
        val apps = prefs.getBlockedApps()
        val shortPwd = if (pwd.length > 12) pwd.take(12) + "…" else pwd
        statusText.text = "Status:\nID: $id\nSenha do App: $shortPwd\nApps bloqueados: ${apps.size}"
    }
}
