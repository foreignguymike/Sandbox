package com.distraction.sandbox.tilemap.tileobjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileObject

class TileLight(context: Context, tileMap: TileMap, row: Int, col: Int) : TileObject(context, tileMap) {
    private val image = context.assets.getAtlas().findRegion("tilelight")
    private val color = Color(1f, 1f, 1f, 1f)
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