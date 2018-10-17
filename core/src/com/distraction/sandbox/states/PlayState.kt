package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileMapData

class PlayState(level: Int) : GameState() {

    private var tileMap = TileMap(TileMapData.levelData[level - 1])

    override fun update(dt: Float) {
        camera.position.set(0f, 0f, 0f)
        camera.update()
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = camera.combined
        sb.begin()
        tileMap.render(sb)
        sb.end()
    }
}