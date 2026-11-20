package dev.morn.utility

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.morn.utility.modules.ModuleManager
import dev.morn.utility.relay.PacketRelay
import dev.morn.utility.ui.OverlayRenderer

class MainActivity : AppCompatActivity() {

    private lateinit var moduleManager: ModuleManager
    private lateinit var overlay: OverlayRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moduleManager = ModuleManager()
        overlay = OverlayRenderer(this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
        }

        val startBtn = Button(this).apply {
            text = "Запустить релей"
            setOnClickListener { startRelay() }
        }

        val stopBtn = Button(this).apply {
            text = "Остановить"
            setOnClickListener { stopRelay() }
        }

        val overlayBtn = Button(this).apply {
            text = "Оверлей визуализации"
            setOnClickListener {
                if (overlay.isShowing()) overlay.hide() else overlay.show()
            }
        }

        layout.addView(startBtn)
        layout.addView(stopBtn)
        layout.addView(overlayBtn)

        moduleManager.modules.forEach { module ->
            val btn = Button(this).apply {
                text = "${module.name}: ${if (module.enabled) "ON" else "OFF"}"
                setOnClickListener {
                    module.toggle()
                    text = "${module.name}: ${if (module.enabled) "ON" else "OFF"}"
                }
            }
            layout.addView(btn)
        }

        setContentView(layout)
    }

    private fun startRelay() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 100)
        } else {
            startService(Intent(this, PacketRelay::class.java))
            Toast.makeText(this, "Релей запущен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRelay() {
        stopService(Intent(this, PacketRelay::class.java))
        Toast.makeText(this, "Релей остановлен", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            startService(Intent(this, PacketRelay::class.java))
        }
    }
}
