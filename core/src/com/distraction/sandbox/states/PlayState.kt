package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.Context
import com.distraction.sandbox.tilemap.Player
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileMapData

class PlayState(context: Context, level: Int) : GameState(context) {

    private var tileMap = TileMap(context, TileMapData.levelData[level - 1])
    private var player = Player(context, tileMap)

    init {
        camera.position.set(0f, 0f, 0f)
        camera.update()
    }

    override fun update(dt: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> player.moveTile(0, 1)
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> player.moveTile(0, -1)
            Gdx.input.isKeyPressed(Input.Keys.UP) -> player.moveTile(-1, 0)
            Gdx.input.isKeyPressed(Input.Keys.DOWN) -> player.moveTile(1, 0)
        }

        player.update(dt)

        camera.position.set(camera.position.lerp(player.pp, 0.1f))
        camera.update()
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = camera.combined
        sb.begin()
        tileMap.render(sb)
        player.render(sb)
        sb.end()
    }
}