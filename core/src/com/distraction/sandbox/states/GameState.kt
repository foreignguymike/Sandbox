package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class GameState {
    protected val camera = OrthographicCamera()

    init {
        camera.setToOrtho(false, 1920f / 8, 1080f / 8)
    }

    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
}