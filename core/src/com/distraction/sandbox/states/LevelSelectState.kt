package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.*
import com.distraction.sandbox.tilemap.TileMapData

class LevelSelectState(context: Context) : GameState(context) {
    private val levelIcon = context.assets.getAtlas().findRegion("levelicon")
    private val colSize = 6
    private val touchPoint = Vector3()

    private val levels = Array(18) {
        val row = it / colSize
        val col = it % colSize
        val x = col * (levelIcon.regionWidth + 5f) + 38
        val y = Constants.HEIGHT - row * (levelIcon.regionHeight + 5f) - 50
        Button(context.assets.getAtlas().findRegion("levelicon"), x, y)
    }

    private val numberFont = NumberFont(context, true)
    private val levelSelectImage = context.assets.getAtlas().findRegion("levelselect")
    private val backButton = Button(context.assets.getAtlas().findRegion("back"), y = 7f, centered = true)

    override fun update(dt: Float) {
        if (!ignoreInput) {
            if (Gdx.input.justTouched()) {
                touchPoint.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
                camera.unproject(touchPoint)
                levels.forEachIndexed { i, it ->
                    if (it.rect.contains(touchPoint) && i < TileMapData.levelData.size) {
                        ignoreInput = true
                        context.gsm.push(TransitionState(context, PlayState(context, i + 1)))
                        return
                    }
                }
                if (backButton.rect.contains(touchPoint)) {
                    ignoreInput = true
                    context.gsm.push(TransitionState(context, TitleState(context)))
                }
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        clearScreen(76, 176, 219)
        sb.use {
            sb.draw(levelSelectImage, (Constants.WIDTH - levelSelectImage.regionWidth) / 2, Constants.HEIGHT - levelSelectImage.regionHeight - 8)
            sb.draw(backButton.image, backButton.rect.x, backButton.rect.y)
            levels.forEachIndexed { i, it ->
                sb.draw(it.image, it.rect.x, it.rect.y)
                numberFont.num = i + 1
                numberFont.render(sb, it.rect.x + levelIcon.regionWidth / 2, it.rect.y + (levelIcon.regionHeight - 6) / 2)
            }
        }
    }
}