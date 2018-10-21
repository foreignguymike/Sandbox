package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.*

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
    val animationSet = AnimationSet()
    var moving = false
    var direction = Direction.RIGHT

    init {
        setTile(row, col)

        animationSet.addAnimation("idle", Animation(context.assets.getAtlas().findRegion("playeridle").split(16, 18)[0], 1f / 2f))
        animationSet.addAnimation("idler", Animation(context.assets.getAtlas().findRegion("playeridler").split(16, 18)[0], 1f / 2f))
        animationSet.addAnimation("jump", Animation(context.assets.getAtlas().findRegion("playerjump").split(13, 18)[0], -1f))
        animationSet.addAnimation("jumpr", Animation(context.assets.getAtlas().findRegion("playerjumpr").split(13, 18)[0], -1f))
        animationSet.addAnimation("crouch", Animation(context.assets.getAtlas().findRegion("playercrouch").split(16, 18)[0], 1f / 10f))
        animationSet.addAnimation("crouchr", Animation(context.assets.getAtlas().findRegion("playercrouchr").split(16, 18)[0], 1f / 10f))

        animationSet.setAnimation("idle")
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
        p.z = 7f + (jumpHeight * MathUtils.sin(3.14f * getRemainingDistance() / totalDist))

        if (p.x == pdest.x && p.y == pdest.y) {
            if ((animationSet.currentAnimationKey.equals("jump") || animationSet.currentAnimationKey.equals("jumpr"))) {
                animationSet.setAnimation(if (direction == Direction.RIGHT || direction == Direction.DOWN) "crouch" else "crouchr")
            } else if (animationSet.currentAnimation!!.hasPlayedOnce()) {
                animationSet.setAnimation(if (direction == Direction.RIGHT || direction == Direction.DOWN) "idle" else "idler")
            }
        } else {
            if ((animationSet.currentAnimationKey.equals("idle") || animationSet.currentAnimationKey.equals("idler"))) {
                animationSet.setAnimation(if (direction == Direction.RIGHT || direction == Direction.DOWN) "crouch" else "crouchr")
            } else if (animationSet.currentAnimation!!.hasPlayedOnce()){
                animationSet.setAnimation(if (direction == Direction.RIGHT || direction == Direction.DOWN) "jump" else "jumpr")
            }
        }
        animationSet.update(dt)
    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        if (direction == Direction.RIGHT || direction == Direction.UP) {
            sb.draw(animationSet.getImage(), pp.x - animationSet.getImage()!!.regionWidth / 2, pp.y - animationSet.getImage()!!.regionHeight / 2 + p.z)
        } else {
            sb.draw(
                    animationSet.getImage(),
                    pp.x + animationSet.getImage()!!.regionWidth / 2,
                    pp.y - animationSet.getImage()!!.regionHeight / 2 + p.z,
                    -animationSet.getImage()!!.regionWidth * 1f,
                    animationSet.getImage()!!.regionHeight * 1f)
        }
    }
}