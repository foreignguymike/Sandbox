package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Utils {
    companion object {
        fun abs(f: Float) = if (f < 0) f * -1 else f
        fun dist(x1: Float, y1: Float, x2: Float, y2: Float) = Math.sqrt(1.0 * (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toFloat()
        fun max(f1: Float, f2: Float) = if (f2 > f1) f2 else f1
    }
}

fun AssetManager.getAtlas(str: String = "sandboxpack.atlas") = get(str, TextureAtlas::class.java)

fun log(str: String) = Gdx.app.log("tag", str)