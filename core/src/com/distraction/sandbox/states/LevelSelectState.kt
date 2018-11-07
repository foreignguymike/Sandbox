package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.distraction.sandbox.*
import com.distraction.sandbox.tilemap.TileMapData

class LevelSelectState(context: Context, private var page: Int = 0) : GameState(context) {
    private val levelIcon = context.assets.getAtlas().findRegion("levelicon")
    private val colSize = 6
    private val maxPages = MathUtils.ceil(TileMapData.levelData.size / 18f)

    private val levels = Array(TileMapData.levelData.size) {
        val page = it / 18
        val row = (it % 18) / colSize
        val col = it % colSize
        val x = col * (levelIcon.regionWidth + 5f) + 38 + Constants.WIDTH * page
        val y = Constants.HEIGHT - row * (levelIcon.regionHeight + 5f) - 50
        Button(context.assets.getAtlas().findRegion("levelicon"), x, y)
    }

    private val numberFont = NumberFont(context, true)
    private val levelSelectImage = context.assets.getAtlas().findRegion("levelselect")
    private val backButton = Button(context.assets.getAtlas().findRegion("back"), y = 7f, centered = true)
    private val disableColor = Color(0.3f, 0.3f, 0.3f, 1f)
    private val staticCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }

    private val leftButton = Button(context.assets.getAtlas().findRegion("levelselectarrow"),
            Rectangle(10f, Constants.HEIGHT / 2 - 5f, 10f, 11f))
    private val rightButton = Button(context.assets.getAtlas().findRegion("levelselectarrow"),
            Rectangle(Constants.WIDTH - 20f, Constants.HEIGHT / 2 - 5f, 10f, 11f))

    init {
        camera.position.set(Constants.WIDTH * page + Constants.WIDTH / 2, Constants.HEIGHT / 2, 0f)
        camera.update()
    }

    override fun update(dt: Float) {
        if (!ignoreInput) {
            if (Gdx.input.justTouched()) {
                touchPoint.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
                camera.unproject(touchPoint)
                levels.forEachIndexed { i, it ->
                    //                    if (context.scoreHandler.locked(i)) {
//                        return@forEachIndexed
//                    }
                    if (it.rect.contains(touchPoint) && i < TileMapData.levelData.size) {
                        ignoreInput = true
                        context.gsm.push(TransitionState(context, PlayState(context, i + 1)))
                        return@forEachIndexed
                    }
                }

                touchPoint.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
                staticCam.unproject(touchPoint)
                if (backButton.rect.contains(touchPoint)) {
                    ignoreInput = true
                    context.gsm.push(TransitionState(context, TitleState(context)))
                }
                if (leftButton.rect.contains(touchPoint)) {
                    if (page > 0) {
                        page--
                    }
                } else if (rightButton.rect.contains(touchPoint)) {
                    if (page < maxPages - 1) {
                        page++
                    }
                }
            }
        }
        camera.position.set(camera.position.lerp(Constants.WIDTH * page + Constants.WIDTH / 2, Constants.HEIGHT / 2, 0f, 0.3f))
        camera.update()
    }

    override fun render(sb: SpriteBatch) {
        clearScreen(76, 176, 219)
        sb.use {
            sb.projectionMatrix = staticCam.combined
            sb.draw(levelSelectImage, (Constants.WIDTH - levelSelectImage.regionWidth) / 2, Constants.HEIGHT - levelSelectImage.regionHeight - 8)
            sb.draw(backButton.image, backButton.rect.x, backButton.rect.y)

            if (page > 0) sb.drawButton(leftButton, true)
            if (page < maxPages - 1) sb.drawButton(rightButton)

            sb.projectionMatrix = camera.combined
            levels.forEachIndexed { i, it ->
                val c = sb.color
//                if (context.scoreHandler.locked(i)) {
//                    sb.color = disableColor
//                }
                if (context.scoreHandler.scores[i] == 0) {
                    sb.color = disableColor
                }
                sb.draw(it.image, it.rect.x, it.rect.y)
                numberFont.num = i + 1
                numberFont.render(sb, it.rect.x + levelIcon.regionWidth / 2, it.rect.y + (levelIcon.regionHeight - 6) / 2)
                sb.color = c
            }
        }
    }
}