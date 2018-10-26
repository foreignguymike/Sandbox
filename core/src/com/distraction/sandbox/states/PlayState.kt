package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Background
import com.distraction.sandbox.Constants
import com.distraction.sandbox.Context
import com.distraction.sandbox.log
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileMapData
import com.distraction.sandbox.tilemap.tileobjects.Player
import com.distraction.sandbox.tilemap.tileobjects.TileLight

interface MoveListener {
    fun onMoved()
    fun onIllegal()
}

class PlayState(context: Context, private val level: Int) : GameState(context), MoveListener {

    private val tileMap = TileMap(context, TileMapData.levelData[level - 1])
    private val player = Player(context, tileMap, this)
    private val bg = Background(context)
    private val bgCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }
    private val tp = Vector3()

    init {
        camera.position.set(0f, 000f, 0f)
        camera.update()
    }

    override fun onMoved() {
        if (tileMap.isFinished()) {
            context.gsm.push(TransitionState(context, PlayState(context, level + 1)))
        }
    }

    override fun onIllegal() {
        context.gsm.push(TransitionState(context, PlayState(context, level)))
    }

    override fun update(dt: Float) {
        if (!ignoreKeys) {
            when {
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> player.moveTile(0, 1)
                Gdx.input.isKeyPressed(Input.Keys.LEFT) -> player.moveTile(0, -1)
                Gdx.input.isKeyPressed(Input.Keys.UP) -> player.moveTile(-1, 0)
                Gdx.input.isKeyPressed(Input.Keys.DOWN) -> player.moveTile(1, 0)
                Gdx.input.isKeyJustPressed(Input.Keys.R) -> onIllegal()
            }
        }

        player.update(dt)

        if (player.teleporting) {
            tileMap.toIsometric(player.pdest.x, player.pdest.y, tp)
            camera.position.set(camera.position.lerp(tp, 0.1f))
        } else {
            camera.position.set(camera.position.lerp(player.pp, 0.1f))
        }
        camera.update()

        bg.update(dt)
        tileMap.update(dt)
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = bgCam.combined
        sb.begin()
        bg.render(sb)
        sb.end()

        sb.projectionMatrix = camera.combined
        sb.begin()
        tileMap.render(sb)
        player.render(sb)
        tileMap.renderOther(sb)
        sb.end()
    }
}