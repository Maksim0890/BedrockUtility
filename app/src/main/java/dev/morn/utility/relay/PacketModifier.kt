package dev.morn.utility.relay

import dev.morn.utility.modules.ModuleManager

class PacketModifier {
    private val modules = ModuleManager()

    fun process(data: ByteArray, length: Int): ByteArray? {
        modules.modules.filter { it.enabled }.forEach { module ->
            module.onPacket(data, length)
        }
        return data.copyOf(length)
    }
}
