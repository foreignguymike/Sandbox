package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.*
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileMapData
import com.distraction.sandbox.tilemap.tileobjects.Player

interface MoveListener {
    fun onMoved()
    fun onIllegal()
}

class PlayState(context: Context, private val level: Int) : GameState(context), MoveListener, ButtonListener {

    private val tileMap = TileMap(context, TileMapData.levelData[level - 1])
    private val player = Player(context, tileMap, this)
    private val bg = Background(context)
    private val bgCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }
    private val tp = Vector3()

    private val hud = HUD(context, this)
    private val cameraOffset = Vector2(-30f, 0f)

    init {
        camera.position.set(0f, 0f, 0f)
        camera.update()
    }

    override fun onMoved() {
        hud.moves++
        if (tileMap.isFinished()) {
            ignoreKeys = true
            if (level == TileMapData.levelData.size - 1) {
                context.gsm.push(TransitionState(context, TitleState(context)))
            } else {
                context.gsm.push(TransitionState(context, PlayState(context, level + 1)))
            }
        }
    }

    override fun onIllegal() {
        if (!tileMap.isFinished() && !ignoreKeys) {
            ignoreKeys = true
            context.gsm.push(TransitionState(context, PlayState(context, level)))
        }
    }

    override fun onButtonPressed(type: ButtonListener.ButtonType) {
        when (type) {
            ButtonListener.ButtonType.UP -> player.moveTile(-1, 0)
            ButtonListener.ButtonType.LEFT -> player.moveTile(0, -1)
            ButtonListener.ButtonType.DOWN -> player.moveTile(1, 0)
            ButtonListener.ButtonType.RIGHT -> player.moveTile(0, 1)
            ButtonListener.ButtonType.RESTART -> onIllegal()
        }
    }

    override fun update(dt: Float) {
        if (!ignoreKeys) {
            hud.update(dt)
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
            camera.position.set(camera.position.lerp(tp.x + cameraOffset.x, tp.y + cameraOffset.y, 0f, 0.1f))
        } else {
            camera.position.set(camera.position.lerp(player.pp.x + cameraOffset.x, player.pp.y + cameraOffset.y, 0f, 0.1f))
        }
        camera.update()

        bg.update(dt)
        tileMap.update(dt)
    }

    override fun render(sb: SpriteBatch) {
        sb.use {
            sb.projectionMatrix = bgCam.combined
            bg.render(sb)

            sb.projectionMatrix = camera.combined
            tileMap.render(sb)
            player.render(sb)
            tileMap.renderOther(sb)

            hud.render(sb)
        }
    }
}