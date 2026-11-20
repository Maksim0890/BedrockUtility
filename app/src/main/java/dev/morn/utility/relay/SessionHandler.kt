package dev.morn.utility.relay

class SessionHandler {
    var localPlayerRuntimeId: Long = 0
    val entities = mutableMapOf<Long, EntityData>()

    data class EntityData(
        var x: Float = 0f,
        var y: Float = 0f,
        var z: Float = 0f,
        var yaw: Float = 0f,
        var pitch: Float = 0f,
        var isPlayer: Boolean = false
    )
}
