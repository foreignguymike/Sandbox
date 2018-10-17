package com.distraction.sandbox.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.distraction.sandbox.MainGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = 1920 / 2
        config.height = 1080 / 2
        LwjglApplication(MainGame(), config)
    }
}
