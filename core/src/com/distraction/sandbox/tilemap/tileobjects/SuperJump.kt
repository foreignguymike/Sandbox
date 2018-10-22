package com.distraction.sandbox.tilemap.tileobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileObject

class SuperJumpLight(context: Context, tileMap: TileMap, row: Int, col: Int) : TileObject(context, tileMap) {
    private val image = context.assets.getAtlas().findRegion("superjump")
    private val speed = 10f
    private val duration = 1f
    private var time = 0f

    init {
        setTile(row, col)
        p.z = -2f
    }

    override fun update(dt: Float) {
        time += dt
        p.z += speed * dt
        if (time > duration) {
            remove = true
        }
    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        sb.draw(image, pp.x - image.regionWidth / 2, pp.y - image.regionHeight / 2 + p.z)
    }
}

class SuperJump(context: Context, tileMap: TileMap, row: Int, col: Int) : TileObject(context, tileMap) {
    private val interval = 0.4f
    private var time = interval

    init {
        setTile(row, col)
    }

    override fun update(dt: Float) {
        time += dt
        while (time > interval) {
            time -= interval
            tileMap.otherObjects.add(SuperJumpLight(context, tileMap, row, col))
        }
    }

    override fun render(sb: SpriteBatch) {

    }
}