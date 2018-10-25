package com.distraction.sandbox.tilemap.tileobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.log
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileObject

class TeleportLight(context: Context, tileMap: TileMap, row: Int, col: Int) : TileObject(context, tileMap) {
    private val image = context.assets.getAtlas().findRegion("teleport")

    init {
        setTile(row, col)
        p.z = -2f
    }

    override fun update(dt: Float) {

    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        sb.draw(image, pp.x - image.regionWidth / 2, pp.y - image.regionHeight / 2 + 5 + p.z)
    }
}

class Teleport(context: Context, tileMap: TileMap, row: Int, col: Int, val row2: Int, val col2: Int) : TileObject(context, tileMap) {

    private val image = context.assets.getAtlas().findRegion("teleport")
    private val p2 = Vector3()

    init {
        setTile(row, col)
        p.z = -2f
        tileMap.toPosition(row2, col2, p2)
        tileMap.otherObjects.add(TeleportLight(context, tileMap, row, col))
        tileMap.otherObjects.add(TeleportLight(context, tileMap, row2, col2))
    }

    override fun update(dt: Float) {

    }

    override fun render(sb: SpriteBatch) {

    }
}