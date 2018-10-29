package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class Utils {
    companion object {
        fun abs(f: Float) = if (f < 0) f * -1 else f
        fun dist(x1: Float, y1: Float, x2: Float, y2: Float) = Math.sqrt(1.0 * (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toFloat()
        fun max(f1: Float, f2: Float) = if (f2 > f1) f2 else f1
    }
}

fun AssetManager.getAtlas(str: String = "sandboxpack.atlas") = get(str, TextureAtlas::class.java)

fun log(str: String) = Gdx.app.log("tag", str)

inline fun SpriteBatch.use(action: () -> Unit): Unit {
    begin()
    action()
    end()
}

fun SpriteBatch.drawRotated(region: TextureRegion, x: Float, y: Float, rotation: Float) {
    draw(region, x, y, region.regionWidth / 2f, region.regionHeight / 2f, 1f * region.regionWidth, 1f * region.regionHeight, 1f, 1f, rotation)
}

fun clearScreen(r: Int = 255, g: Int = 255, b: Int = 255, a: Int = 255) {
    Gdx.gl.glClearColor(r / 255f, g / 255f, b / 255f, a / 255f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
}

fun Vector3.lerp(x: Float, y: Float, z: Float, amount: Float): Vector3 {
    this.x += amount * (x - this.x)
    this.y += amount * (y - this.y)
    this.z += amount * (z - this.z)
    return this
}

fun Rectangle.contains(v: Vector3) = contains(v.x, v.y)