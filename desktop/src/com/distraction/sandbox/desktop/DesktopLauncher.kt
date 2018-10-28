package com.distraction.sandbox.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.distraction.sandbox.Constants
import com.distraction.sandbox.MainGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = Constants.DESKTOP_WIDTH
        config.height = Constants.DESKTOP_HEIGHT
        LwjglApplication(MainGame(), config)
    }
}
