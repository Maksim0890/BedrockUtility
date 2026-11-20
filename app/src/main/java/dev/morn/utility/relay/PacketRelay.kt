package dev.morn.utility.relay

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class PacketRelay : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var job: Job? = null
    private val modifier = PacketModifier()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        val builder = Builder()
            .setSession("BedrockUtility")
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
            .setMtu(1500)

        vpnInterface = builder.establish()

        job = CoroutineScope(Dispatchers.IO).launch {
            val input = FileInputStream(vpnInterface!!.fileDescriptor)
            val output = FileOutputStream(vpnInterface!!.fileDescriptor)
            val buffer = ByteBuffer.allocate(32767)

            while (isActive) {
                buffer.clear()
                val length = input.channel.read(buffer)
                if (length > 0) {
                    buffer.flip()
                    val modified = modifier.process(buffer.array(), length)
                    if (modified != null) {
                        output.write(modified)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        vpnInterface?.close()
        super.onDestroy()
    }
}
