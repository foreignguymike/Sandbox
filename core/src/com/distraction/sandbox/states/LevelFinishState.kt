package com.distraction.sandbox.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.distraction.sandbox.*

class LevelFinishState(context: Context, val level: Int, val moves: Int, val best: Int) : GameState(context) {
    private val dot = context.assets.getAtlas().findRegion("dot")
    private val dimColor = Color(0f, 0f, 0f, 0f)

    private val completeImage = Button(context.assets.getAtlas().findRegion("complete"), 0f, 80f, centered = true)
    private val bestLabel = NumberLabel(context, context.assets.getAtlas().findRegion("best"), Vector2(Constants.WIDTH / 2 - 20f, Constants.HEIGHT / 2f), best)
    private val movesLabel = NumberLabel(context, context.assets.getAtlas().findRegion("moves"), Vector2(Constants.WIDTH / 2 - 20f, Constants.HEIGHT / 2f - 10), moves)
    private val newRecordImage = Button(context.assets.getAtlas().findRegion("newrecord"), 0f, 40f, centered = true)

    private val restartButton = Button(context.assets.getAtlas().findRegion("restart"), 5f, 115f)
    private val backButton = Button(context.assets.getAtlas().findRegion("back"),5f, 98f)
    private val nextButton = Button(context.assets.getAtlas().findRegion("next"), 0f, 15f, centered = true)

    init {
        context.gsm.depth++
    }

    override fun update(dt: Float) {
        if (!ignoreInput) {
            if (Gdx.input.justTouched()) {
                touchPoint.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
                camera.unproject(touchPoint)
                if (nextButton.rect.contains(touchPoint)) {
                    ignoreInput = true
                    context.gsm.push(TransitionState(context, PlayState(context, level + 1), 2))
                } else if (backButton.rect.contains(touchPoint)) {
                    ignoreInput = true
                    context.gsm.push(TransitionState(context, LevelSelectState(context), 2))
                } else if (restartButton.rect.contains(touchPoint)) {
                    ignoreInput = true
                    context.gsm.push(TransitionState(context, PlayState(context, level), 2))
                }
            }
        }
        if (dimColor.a < 0.7f) {
            dimColor.a += 2f * dt
            if (dimColor.a > 0.7f) {
                dimColor.a = 0.7f
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        sb.use {
            val c = sb.color
            sb.color = dimColor
            sb.draw(dot, 0f, 0f, Constants.WIDTH, Constants.HEIGHT)
            sb.color = c

            sb.draw(completeImage)
            bestLabel.render(sb)
            movesLabel.render(sb)
            if (best == 0 || moves < best) {
                sb.draw(newRecordImage)
            }

            sb.draw(restartButton)
            sb.draw(backButton)
            sb.draw(nextButton)
        }
    }
}