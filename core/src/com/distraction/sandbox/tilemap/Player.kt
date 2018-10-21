package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.*
import com.distraction.sandbox.states.MoveListener
import com.distraction.sandbox.tilemap.Player.Direction.*

class Player(context: Context, tileMap: TileMap, val moveListener: MoveListener) : TileObject(context, tileMap) {

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
    var sliding = false
    var direction = RIGHT

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

    override fun setTile(row: Int, col: Int) {
        super.setTile(row, col)
        tileMap.getTile(row, col).toggleActive()
    }

    fun moveTile(rowdx: Int, coldx: Int) {
        if (moving) return
        if (!tileMap.isValidTile(row + rowdx, col + coldx)) return
        when {
            coldx > 0 -> direction = RIGHT
            coldx < 0 -> direction = LEFT
            rowdx > 0 -> direction = DOWN
            rowdx < 0 -> direction = UP
        }
        row += rowdx
        col += coldx
        tileMap.toPosition(row, col, pdest)
        totalDist = getRemainingDistance()
        moving = true
    }

    fun getRemainingDistance() = (pdest.x - p.x) + (pdest.y - p.y)

    override fun update(dt: Float) {
        val dist = speed * dt * if (sliding) 4 else 1
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
            val tile = tileMap.getTile(row, col)
            if (moving) {
                tile.toggleActive()
                moveListener.onMoved()
                sliding = false
                if (tile.active) {
                    tileMap.otherObjects.add(TileLight(context, tileMap, row, col))
                }
            }
            moving = false
            tile.objects.forEach {
                if (it is Arrow) {
                    sliding = true
                    when (it.direction) {
                        UP -> moveTile(-1, 0)
                        RIGHT -> moveTile(0, 1)
                        LEFT -> moveTile(0, -1)
                        DOWN -> moveTile(1, 0)
                    }
                }
            }
        }
        if (sliding) {
            p.z = 4f
        } else {
            p.z = 4f + (jumpHeight * MathUtils.sin(3.14f * getRemainingDistance() / totalDist))
        }

        if (sliding) {
            animationSet.setAnimation(if (direction == RIGHT || direction == DOWN) "crouch" else "crouchr")
        } else if (p.x == pdest.x && p.y == pdest.y) {
            if ((animationSet.currentAnimationKey.equals("jump") || animationSet.currentAnimationKey.equals("jumpr"))) {
                animationSet.setAnimation(if (direction == RIGHT || direction == DOWN) "crouch" else "crouchr")
            } else if (animationSet.currentAnimation!!.hasPlayedOnce()) {
                animationSet.setAnimation(if (direction == RIGHT || direction == DOWN) "idle" else "idler")
            }
        } else {
            if ((animationSet.currentAnimationKey.equals("idle") || animationSet.currentAnimationKey.equals("idler"))) {
                animationSet.setAnimation(if (direction == RIGHT || direction == DOWN) "crouch" else "crouchr")
            } else if (animationSet.currentAnimation!!.hasPlayedOnce()) {
                animationSet.setAnimation(if (direction == RIGHT || direction == DOWN) "jump" else "jumpr")
            }
        }
        animationSet.update(dt)
    }

    override fun render(sb: SpriteBatch) {
        tileMap.toIsometric(p.x, p.y, pp)
        if (direction == RIGHT || direction == UP) {
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