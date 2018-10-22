package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Context

abstract class TileObject(protected val context: Context, val tileMap: TileMap) {
    val p = Vector3()
    val pp = Vector3()
    val pdest = Vector3()

    var row = 0
    var col = 0
    var width = 0f
    var height = 0f
    var remove = false

    open fun setTile(row: Int, col: Int) {
        this.row = row
        this.col = col
        tileMap.toPosition(row, col, p)
    }

    fun moveToDest(dist: Float) {
        if (p.x < pdest.x) {
            p.x += dist
            if (p.x > pdest.x) {
                p.x = pdest.x
            }
        }
        if (p.x > pdest.x) {
            p.x -= dist
            if (p.x < pdest.x) {
                p.x = pdest.x
            }
        }
        if (p.y < pdest.y) {
            p.y += dist
            if (p.y > pdest.y) {
                p.y = pdest.y
            }
        }
        if (p.y > pdest.y) {
            p.y -= dist
            if (p.y < pdest.y) {
                p.y = pdest.y
            }
        }
    }

    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
}

