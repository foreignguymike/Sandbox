package com.distraction.sandbox.tilemap.tileobjects


import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileObject
import com.distraction.sandbox.tilemap.tileobjects.Player.Direction.*

class Arrow(context: Context, tileMap: TileMap, row: Int, col: Int, val direction: Player.Direction) : TileObject(context, tileMap) {
    private val image = context.assets.getAtlas().findRegion("arrow")

    init {
        setTile(row, col)
    }

    override fun update(dt: Float) {

    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        when (direction) {
            RIGHT -> sb.draw(image, pp.x - image.regionWidth / 2 - 2, pp.y - image.regionHeight / 2 - 2)
            LEFT -> sb.draw(image, pp.x + image.regionWidth / 2 + 3, pp.y + image.regionHeight / 2 - 3, -image.regionWidth * 1f, -image.regionHeight * 1f)
            UP -> sb.draw(image, pp.x - image.regionWidth / 2 - 2, pp.y + image.regionHeight / 2 - 3, image.regionWidth * 1f, -image.regionHeight * 1f)
            DOWN -> sb.draw(image, pp.x + image.regionWidth / 2 + 3, pp.y - image.regionHeight / 2 - 2, -image.regionWidth * 1f, image.regionHeight * 1f)
        }
    }
}