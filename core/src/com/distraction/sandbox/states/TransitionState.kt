package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Constants
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.use

class TransitionState(context: Context, val nextState: GameState) : GameState(context) {

    private val dot = context.assets.getAtlas().findRegion("dot")
    private val duration = 0.5f
    private var time = 0f
    private var next = false

    init {
        context.gsm.rdepth = 2
        context.gsm.udepth = 2
    }

    override fun update(dt: Float) {
        time += dt
        if (!next && time > duration / 2) {
            next = true
            nextState.ignoreKeys = true
            context.gsm.pop()
            context.gsm.replace(nextState)
            context.gsm.push(this)
        }
        if (time > duration) {
            context.gsm.rdepth = 1
            context.gsm.udepth = 1
            context.gsm.pop()
            nextState.ignoreKeys = false
        }
    }

    override fun render(sb: SpriteBatch) {
        val interp = time / duration
        val perc = if (interp < 0.5f) interp * 2f else 1f - (time - duration / 2) / duration * 2
        val c = sb.color
        sb.color = Color.BLACK
        sb.projectionMatrix = camera.combined
        sb.use {
            sb.draw(dot, 0f, Constants.HEIGHT, 1f * Constants.WIDTH, -perc * Constants.HEIGHT / 2)
            sb.draw(dot, 0f, 0f, 1f * Constants.WIDTH, perc * Constants.HEIGHT / 2)
        }
        sb.color = c
    }
}