package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.distraction.sandbox.*
import com.distraction.sandbox.tilemap.TileMap
import com.distraction.sandbox.tilemap.TileMapDataModel
import com.distraction.sandbox.tilemap.tileobjects.Player

class TitleState(context: Context) : GameState(context), MoveListener {
    private val tileMap = TileMap(context, TileMapDataModel(3, 3, IntArray(9) { 1 }))
    private val player = Player(context, tileMap, this)
    private val title = context.assets.getAtlas().findRegion("title")
    private val hudCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }

    init {
        hudCam.position.set(Constants.WIDTH / 2f, -100f, 0f)
        hudCam.update()
        camera.position.set(0f, 100f, 0f)
        camera.update()
    }

    override fun onMoved() {

    }

    override fun onIllegal() {

    }

    override fun update(dt: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            context.gsm.push(TransitionState(context, PlayState(context, 1)))
        }

        player.update(dt)

        hudCam.position.set(hudCam.position.lerp(Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 0f, 0.1f))
        hudCam.update()
        camera.position.set(camera.position.lerp(player.pp.x, player.pp.y + Constants.HEIGHT / 4, player.pp.z, 0.03f))
        camera.update()
    }

    override fun render(sb: SpriteBatch) {
        clearScreen(149, 219, 157)
        sb.use {
            sb.projectionMatrix = hudCam.combined
            sb.draw(title, (Constants.WIDTH - title.regionWidth) / 2f, 60f)

            sb.projectionMatrix = camera.combined
            tileMap.render(sb)
            player.render(sb)
        }
    }
}