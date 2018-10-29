package com.distraction.sandbox

import com.badlogic.gdx.Gdx
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

class Button(val image: TextureRegion, val rect: Rectangle)
class NumberLabel(val image: TextureRegion, val pos: Vector2, var num: Int)

class HUD(context: Context, private val buttonListener: ButtonListener) {
    private val touchUnprojected = Vector3()

    private val buttons = hashMapOf(
            LEFT to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            Rectangle(6f, 26f, 25f, 25f)),
            UP to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            Rectangle(21f, 41f, 25f, 25f)),
            RIGHT to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            Rectangle(36f, 26f, 25f, 25f)),
            DOWN to
                    Button(context.assets.getAtlas().findRegion("arrowbutton"),
                            Rectangle(21f, 11f, 25f, 25f)),
            RESTART to
                    Button(context.assets.getAtlas().findRegion("restart"),
                            Rectangle(5f, 115f, 48f, 17f)),
            BACK to
                    Button(context.assets.getAtlas().findRegion("back"),
                            Rectangle(5f, 98f, 48f, 17f)))

    private val labels = arrayOf(
            NumberLabel(context.assets.getAtlas().findRegion("best"),
                    Vector2(Constants.WIDTH - 50f, Constants.HEIGHT - 16f), 0),
            NumberLabel(context.assets.getAtlas().findRegion("moves"),
                    Vector2(Constants.WIDTH - 55f, Constants.HEIGHT - 25f), 0))

    private val numberFont = NumberFont(context)

    fun incrementMoves() {
        labels[1].num++
    }

    private val cam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH, Constants.HEIGHT)
    }
    private val fontCam = OrthographicCamera().apply {
        setToOrtho(false, Constants.WIDTH * 2f, Constants.HEIGHT * 2f)
    }

    fun update(dt: Float) {
        if (Gdx.input.isTouched) {
            touchUnprojected.set(1f * Gdx.input.x, 1f * Gdx.input.y, 0f)
            cam.unproject(touchUnprojected)

            buttons.forEach { key, value ->
                if (value.rect.contains(touchUnprojected.x, touchUnprojected.y)) {
                    buttonListener.onButtonPressed(key)
                }
            }
        }
    }

    fun render(sb: SpriteBatch) {
        sb.projectionMatrix = fontCam.combined
        sb.projectionMatrix = cam.combined
        buttons.forEach { key, value ->
            when (key) {
                LEFT -> sb.drawRotated(value.image, value.rect.x, value.rect.y, 90f)
                DOWN -> sb.draw(value.image, value.rect.x, value.rect.y + value.image.regionHeight, 1f * value.image.regionWidth, 1f * -value.image.regionHeight)
                RIGHT -> sb.drawRotated(value.image, value.rect.x, value.rect.y, -90f)
                else -> sb.draw(value.image, value.rect.x, value.rect.y)
            }
        }
        labels.forEach {
            sb.draw(it.image, it.pos.x, it.pos.y)
            numberFont.render(sb, it.num, it.pos.x + it.image.regionWidth + 5, it.pos.y)
        }
    }
}