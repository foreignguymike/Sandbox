package com.distraction.sandbox.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

class GSM {

    private var states = Stack<GameState>()

    fun push(state: GameState) {
        states.push(state)
    }

    fun pop() : GameState {
        return states.pop()
    }

    fun replace(state: GameState) : GameState {
        var s = states.pop();
        states.push(state)
        return s
    }

    fun update(dt: Float) {
        states.peek().update(dt)
    }

    fun render(sb: SpriteBatch) {
        states.peek().render(sb)
    }

}