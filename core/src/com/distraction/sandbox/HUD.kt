package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.ButtonListener.ButtonType.*

interface ButtonListener {
    enum class ButtonType {
        UP,
        LEFT,
        DOWN,
        RIGHT,
        RESTART,
        BACK
    }

    fun onButtonPressed(type: ButtonType)
}

open class Button(val image: TextureRegion, val rect: Rectangle, val color: Color = Color(1f, 1f, 1f, 1f), val centered: Boolean = false) {
    constructor(image: TextureRegion, x: Float = 0f, y: Float = 0f, color: Color = Color(1f, 1f, 1f, 1f), centered: Boolean = false) :
            this(
                    image,
                    Rectangle(if (centered) (Constants.WIDTH - image.regionWidth) / 2f else x, y, 1f * image.regionWidth, 1f * image.regionHeight),
                    color,
                    centered)
}

class NumberLabel(context: Context, val image: TextureRegion, val pos: Vector2, var num: Int) {
    private val numberFont = NumberFont(context)
    fun render(sb: SpriteBatch) {
        sb.draw(image, pos.x, pos.y)
        numberFont.render(sb, pos.x + image.regionWidth + 5, pos.y, num)
    }
}

class HUD(context: Context, private val buttonListener: ButtonListener) {
    private val touchPoint = Vector3()
    private val alpha = 0.5f

    private val buttons = hashMapOf(
            LEFT to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            6f, 26f,
                            Color(1f, 1f, 1f, alpha)),
            UP to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            21f, 41f,
                            Color(1f, 1f, 1f, alpha)),
            RIGHT to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            36f, 26f,
                            Color(1f, 1f, 1f, alpha)),
            DOWN to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            21f, 11f,
                            Color(1f, 1f, 1f, alpha)),
            BACK to
                    Button(context.assets.getAtlas().findRegion("back"),
                            5f, 115f),
            RESTART to
                    Button(context.assets.getAtlas().findRegion("restart"),
                            5f, 98f))

    private val labels = arrayOf(
            NumberLabel(
                    context,
                    context.assets.getAtlas().findRegion("goal"),
                    Vector2(Constants.WIDTH - 50f, Constants.HEIGHT - 16f),
                    0),
            NumberLabel(
                    context,
                    context.assets.getAtlas().findRegion("best"),
                    Vector2(Constants.WIDTH - 50f, Constants.HEIGHT - 25f),
                    0),
            NumberLabel(
                    context,
                    context.assets.getAtlas().findRegion("moves"),
                    Vector2(Constants.WIDTH - 55f, Constants.HEIGHT - 34f),
                    0))

    fun setGoal(goal: Int) {
        labels[0].num = goal
    }
    fun setBest(best: Int) {
        labels[1].num = best
    }

    fun getBest() = labels[1].num
    fun incrementMoves() = labels[2].num++
    fun getMoves() = labels[2].num

    private val cam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }
    private val fontCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH * 2f, Constants.HEIGHT * 2f)
    }

    fun update(dt: Float) {
        if (Gdx.input.isTouched) {
            touchPoint.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
            cam.unproject(touchPoint)

            buttons.forEach { (key, value) ->
                if (value.rect.contains(touchPoint)) {
                    buttonListener.onButtonPressed(key)
                }
            }
        }
    }

    fun render(sb: SpriteBatch) {
        sb.projectionMatrix = fontCam.combined
        sb.projectionMatrix = cam.combined
        buttons.forEach { (key, value) ->
            val c = sb.color
            sb.color = value.color
            when (key) {
                LEFT -> sb.drawRotated(value.image, value.rect.x, value.rect.y, 90f)
                DOWN -> sb.draw(value.image, value.rect.x, value.rect.y + value.image.regionHeight, 1f * value.image.regionWidth, 1f * -value.image.regionHeight)
                RIGHT -> sb.drawRotated(value.image, value.rect.x, value.rect.y, -90f)
                else -> sb.draw(value.image, value.rect.x, value.rect.y)
            }
            sb.color = c
        }
        labels.forEach {
            it.render(sb)
        }
    }
}