package com.distraction.sandbox

import com.badlogic.gdx.Gdx
import com.distraction.sandbox.tilemap.TileMapData

class ScoreHandler {
    val scores = Array(TileMapData.levelData.size + 1) { 0 }

    fun getPreferences() = Gdx.app.getPreferences("scores")

    fun init() {
        with(getPreferences()) {
            for (i in 0 until scores.size) {
                if (getInteger(i.toString()) == 0) {
                    putInteger(i.toString(), 0)
                }
            }
            flush()
        }
    }

    fun load() {
        with(getPreferences()) {
            for (i in 0 until scores.size) {
                if (getString(i.toString()) == null) {
                    init()
                }
                scores[i] = getInteger(i.toString(), 0)
            }
        }
    }

    fun saveScore(level: Int, score: Int) {
        with(getPreferences()) {
            putInteger(level.toString(), score)
            scores[level] = score
            flush()
        }
    }
}