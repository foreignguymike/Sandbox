package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Constants
import com.distraction.sandbox.Context

abstract class GameState(protected val context: Context) {
    var ignoreInput = false
    protected val camera = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }

    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
}