package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

class GSM {

    private var states = Stack<GameState>()
    var depth = 1

    fun push(state: GameState) {
        states.push(state)
    }

    fun pop() : GameState {
        return states.pop()
    }

    fun replace(state: GameState) : GameState {
        val s = states.pop()
        states.push(state)
        return s
    }

    fun update(dt: Float) {
        for (i in depth - 1..0) {
            states[i].update(dt)
        }
    }

    fun render(sb: SpriteBatch) {
        for (i in depth - 1..0) {
            states[i].render(sb)
        }
    }

}