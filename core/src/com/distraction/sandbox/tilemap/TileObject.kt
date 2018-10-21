package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Context

abstract class TileObject(protected val context: Context, val tileMap: TileMap) {
    var p = Vector3()
    var pp = Vector3()
    var row = 0
    var col = 0
    var width = 0f
    var height = 0f

    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
}

