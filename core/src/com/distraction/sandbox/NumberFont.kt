package com.distraction.sandbox

import com.badlogic.gdx.graphics.g2d.SpriteBatch

fun Char.intValue(): Int = if (this !in '0'..'9') throw NumberFormatException() else toInt() - '0'.toInt()

class NumberFont(context: Context, var centered: Boolean = false) {
    private val images = Array(10) {
        context.assets.getAtlas().findRegion(it.toString())
    }
    private var length = 0

    var num = 0
    set(value) {
        val s = value.toString()
        length = 0
        for (c in s) {
            val n = c.intValue()
            length += images[n].regionWidth
        }
        field = value
    }

    fun render(sb: SpriteBatch, x: Float, y: Float, num: Int = this.num) {
        val s = num.toString()
        var offset = 0
        if (centered) {
            for (c in s) {
                val n = c.intValue()
                sb.draw(images[n], x + offset - length / 2, y)
                offset += images[n].regionWidth
            }
        } else {
            for (c in s) {
                val n = c.intValue()
                sb.draw(images[n], x + offset, y)
                offset += images[n].regionWidth
            }
        }
    }
}