package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.tilemap.TileObjectType.*

class Tile(val context: Context, val value: Int, var active: Boolean = false) {
    val objects = arrayListOf<TileObject>()
    fun toggleActive() {
        active = !active
    }

    fun getImage() = if (active) context.assets.getAtlas().findRegion("tilea") else context.assets.getAtlas().findRegion("tile")
}

class TileLight(context: Context, tileMap: TileMap, row: Int, col: Int) : TileObject(context, tileMap) {
    private val image = context.assets.getAtlas().findRegion("tilelight")
    private val color = Color.WHITE
    private val maxHeight = 32f
    private val speed = 32f * 12f
    private val duration = 0.5f
    private var time = 0f

    init {
        setTile(row, col)
    }

    override fun update(dt: Float) {
        height += speed * dt
        if (height > maxHeight) {
            height = maxHeight
        }
        time += dt
        color.a = 1f - time / duration
        if (time > duration) {
            remove = true
        }
    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        val c = sb.color
        sb.color = color
        sb.draw(image, pp.x - image.regionWidth / 2, pp.y - image.regionHeight / 2 + 5, 1f * image.regionWidth, height)
        sb.color = c
    }
}

class TileMap(private val context: Context, val levelData: TileMapDataModel) {

    companion object {
        const val TILE_WIDTH = 32f
        const val TILE_IWIDTH = 16f
        const val TILE_IHEIGHT = 8f
    }

    private val p = Vector3()
    private val grid = Array(levelData.grid.size) {
        Tile(context, levelData.grid[it])
    }
    val otherObjects = arrayListOf<TileObject>()

    init {
        levelData.objs.forEach {
            val tile = getTile(it.row, it.col)
            when (it.type) {
                ARROW_RIGHT -> tile.objects.add(Arrow(context, this, it.row, it.col, Player.Direction.RIGHT))
                ARROW_LEFT -> tile.objects.add(Arrow(context, this, it.row, it.col, Player.Direction.LEFT))
                ARROW_DOWN -> tile.objects.add(Arrow(context, this, it.row, it.col, Player.Direction.DOWN))
                ARROW_UP -> tile.objects.add(Arrow(context, this, it.row, it.col, Player.Direction.UP))
            }

        }
    }

    fun update(dt: Float) {
        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                val tile = getTile(row, col)
                tile.objects.forEach {
                    it.update(dt)
                }
            }
        }
        otherObjects.forEach {
            it.update(dt)
        }
        otherObjects.removeAll {
            it.remove
        }
    }

    fun render(sb: SpriteBatch) {
        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                val tile = getTile(row, col)
                if (tile.value == 1) {
                    toIsometric(col * TileMap.TILE_WIDTH, row * TileMap.TILE_WIDTH, p)
                    sb.draw(tile.getImage(), p.x - TILE_WIDTH / 2, p.y - TILE_IHEIGHT * 2)
                }
                tile.objects.forEach {
                    it.render(sb)
                }
            }
        }
    }

    fun renderOther(sb: SpriteBatch) {
        otherObjects.forEach {
            it.render(sb)
        }
    }

    fun toPosition(row: Int, col: Int, p: Vector3) {
        p.x = col * TILE_WIDTH
        p.y = row * TILE_WIDTH
    }

    fun toIsometric(x: Float, y: Float, p: Vector3) {
        val xo = x / TileMap.TILE_WIDTH
        val yo = y / TileMap.TILE_WIDTH
        p.x = (xo - yo) * TileMap.TILE_IWIDTH
        p.y = (-xo - yo) * TileMap.TILE_IHEIGHT
    }

    fun isValidTile(row: Int, col: Int): Boolean {
        if (row < 0 || row >= levelData.numRows || col < 0 || col >= levelData.numCols) return false
        return levelData.grid[row * levelData.numCols + col] == 1
    }

    fun getTile(row: Int, col: Int) = grid[row * levelData.numCols + col]

    fun isFinished(): Boolean {
        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                val tile = getTile(row, col)
                if (tile.value == 1 && !tile.active) {
                    return false
                }
            }
        }
        return true
    }

}