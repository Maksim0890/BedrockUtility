package dev.morn.utility.modules

abstract class CheatModule(val name: String) {
    var enabled = false
    fun toggle() { enabled = !enabled }
    open fun onPacket(data: ByteArray, length: Int) {}
    open fun onTick() {}
}

class ModuleManager {
    val modules = listOf(
        KillAura(),
        Speed(),
        VisualOverlayModule()
    )
}

class KillAura : CheatModule("KillAura") {
    override fun onPacket(data: ByteArray, length: Int) {
        // Модификация пакетов атаки
    }
}

class Speed : CheatModule("Speed") {
    override fun onPacket(data: ByteArray, length: Int) {
        // Модификация пакетов движения
    }
}

class VisualOverlayModule : CheatModule("VisualOverlay") {
    // Управление оверлеем
}
