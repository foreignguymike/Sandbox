package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.distraction.sandbox.tilemap.TileMapData

class ScoreHandler {
    val scores = IntArray(TileMapData.levelData.size) { 0 }

    fun getPreferences() = Gdx.app.getPreferences("scores")

    fun init() {
        with(getPreferences()) {
            for (i in 0 until scores.size) {
                if (!contains(i.toString())) {
                    putInteger(i.toString(), 0)
                }
            }
            flush()
        }
    }

    fun load() {
        with(getPreferences()) {
            for (i in 0 until scores.size) {
                if (!contains(i.toString())) {
                    init()
                }
                scores[i] = getInteger(i.toString(), 0)
            }
        }
    }

    fun saveScore(index: Int, score: Int) {
        with(getPreferences()) {
            putInteger(index.toString(), score)
            scores[index] = score
            flush()
        }
    }
}