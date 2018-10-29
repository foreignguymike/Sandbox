package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

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

class HUD(context: Context, private val buttonListener: ButtonListener) {
    private val touchUnprojected = Vector3()

    private val buttons = hashMapOf(
            ButtonListener.ButtonType.LEFT to
                    Button(context.assets.getAtlas().findRegion("leftbutton"),
                            Rectangle(10f, 10f, 25f, 25f)),
            ButtonListener.ButtonType.UP to
                    Button(context.assets.getAtlas().findRegion("upbutton"),
                            Rectangle(36f, 36f, 25f, 25f)),
            ButtonListener.ButtonType.RIGHT to
                    Button(context.assets.getAtlas().findRegion("rightbutton"),
                            Rectangle(62f, 10f, 25f, 25f)),
            ButtonListener.ButtonType.DOWN to
                    Button(context.assets.getAtlas().findRegion("downbutton"),
                            Rectangle(36f, 10f, 25f, 25f)),
            ButtonListener.ButtonType.RESTART to
                    Button(context.assets.getAtlas().findRegion("restart"),
                            Rectangle(5f, 115f, 48f, 17f)),
            ButtonListener.ButtonType.BACK to
                    Button(context.assets.getAtlas().findRegion("back"),
                            Rectangle(5f, 98f, 48f, 17f)))

    private val movesImage = context.assets.getAtlas().findRegion("moves")
    var moves = 0
    private val numberFont = NumberFont(context)

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
        buttons.forEach { _, value ->
            sb.draw(value.image, value.rect.x, value.rect.y)
        }
        sb.draw(movesImage, Constants.WIDTH - movesImage.regionWidth - 25, Constants.HEIGHT - movesImage.regionHeight - 9)
        numberFont.render(sb, moves, Constants.WIDTH - movesImage.regionWidth + 5, Constants.HEIGHT - movesImage.regionHeight - 9)
    }
}