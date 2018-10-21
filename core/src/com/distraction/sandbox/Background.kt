package com.distraction.sandbox

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Background(val context: Context) {

    private val dot = context.assets.getAtlas().findRegion("dot")
    private val color = Color.valueOf("95DB9D")

    fun update(dt: Float) {

    }

    fun render(sb: SpriteBatch) {
        val c = sb.color
        sb.color = color
        sb.draw(dot, 0f, 0f, Constants.WIDTH, Constants.HEIGHT)
        sb.color = c
    }

}