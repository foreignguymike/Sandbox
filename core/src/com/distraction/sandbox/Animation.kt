package com.distraction.sandbox

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Animation(
        val sprites: Array<TextureRegion>,
        val delay: Float = 1f/60) {

    private var time = 0f
    private var frameIndex = 0

    fun update(dt: Float) {
        time += dt
        while (time >= delay) {
            time -= delay
            frameIndex++
            if (frameIndex >= sprites.size) {
                frameIndex = 0
            }
        }
    }

    fun getImage() = sprites[frameIndex]

}