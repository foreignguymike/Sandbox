package com.distraction.sandbox

class AnimationSet {

    val set = hashMapOf<String, Animation>()
    var currentAnimationKey: String? = null
    var currentAnimation: Animation? = null

    fun addAnimation(key: String, value: Animation) {
        set.put(key, value)
    }

    fun setAnimation(key: String) {
        if (key.equals(currentAnimationKey)) {
            return
        }
        currentAnimationKey = key
        currentAnimation = set[key]
        currentAnimation?.reset()
    }

    fun update(dt: Float) = currentAnimation?.update(dt)

    fun getImage() = currentAnimation?.getImage()

}