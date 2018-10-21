package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Animation
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas

class Player(context: Context, tileMap: TileMap) : TileObject(context, tileMap) {

    enum class Direction {
        UP,
        RIGHT,
        LEFT,
        DOWN
    }

    var pdest = Vector3(p.x, p.y, p.z)
    var totalDist = 0f
    var speed = 32f * 2
    var jumpHeight = 20f
    val animation = Animation(context.assets.getAtlas().findRegion("playersprites").split(14, 18)[0], 1f / 15f)
    var moving = false
    var direction = Direction.RIGHT

    init {
        setTile(row, col)
        p.z = 5f
    }

    fun setTile(row: Int, col: Int) {
        this.row = row
        this.col = col
        tileMap.toPosition(row, col, p)
    }

    fun moveTile(rowdx: Int, coldx: Int) {
        if (moving) return
        if (!tileMap.isValidTile(row + rowdx, col + coldx)) return
        when {
            coldx > 0 -> direction = Direction.RIGHT
            coldx < 0 -> direction = Direction.LEFT
            rowdx > 0 -> direction = Direction.DOWN
            rowdx < 0 -> direction = Direction.UP
        }
        row += rowdx
        col += coldx
        tileMap.toPosition(row, col, pdest)
        totalDist = getRemainingDistance()
        moving = true
    }

    fun getRemainingDistance() = (pdest.x - p.x) + (pdest.y - p.y)

    override fun update(dt: Float) {
        val dist = speed * dt
        if (p.x < pdest.x) {
            p.x += dist
            if (p.x > pdest.x) {
                p.x = pdest.x
            }
        }
        if (p.x > pdest.x) {
            p.x -= dist
            if (p.x < pdest.x) {
                p.x = pdest.x
            }
        }
        if (p.y < pdest.y) {
            p.y += dist
            if (p.y > pdest.y) {
                p.y = pdest.y
            }
        }
        if (p.y > pdest.y) {
            p.y -= dist
            if (p.y < pdest.y) {
                p.y = pdest.y
            }
        }
        if (p.x == pdest.x && p.y == pdest.y) {
            if (moving) {
                tileMap.getTile(row, col).toggleActive()
            }
            moving = false
        }
        p.z = 5 + (jumpHeight * MathUtils.sin(3.14f * getRemainingDistance() / totalDist))
        animation.update(dt)
    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        if (direction == Direction.RIGHT || direction == Direction.UP) {
            sb.draw(animation.getImage(), pp.x - animation.getImage().regionWidth / 2, pp.y - animation.getImage().regionHeight / 2 + p.z)
        } else {
            sb.draw(
                    animation.getImage(),
                    pp.x + animation.getImage().regionWidth / 2,
                    pp.y - animation.getImage().regionHeight / 2 + p.z,
                    -animation.getImage().regionWidth * 1f,
                    animation.getImage().regionHeight * 1f)
        }
    }
}