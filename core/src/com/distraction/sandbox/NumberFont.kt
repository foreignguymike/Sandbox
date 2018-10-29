package com.distraction.sandbox

import com.badlogic.gdx.graphics.g2d.SpriteBatch

fun Char.intValue(): Int = if (this !in '0'..'9') throw NumberFormatException() else toInt() - '0'.toInt()

class NumberFont(context: Context) {
    private val images = Array(10) {
        context.assets.getAtlas().findRegion(it.toString())
    }

    fun render(sb: SpriteBatch, num: Int, x: Float, y: Float) {
        val s = num.toString()
        var offset = 0
        for (c in s) {
            val n = c.intValue()
            sb.draw(images[n], x + offset, y)
            offset += images[n].regionWidth
        }
    }
}