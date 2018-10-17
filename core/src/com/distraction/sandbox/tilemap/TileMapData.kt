package com.distraction.sandbox.tilemap

class TileMapDataModel(var numRows: Int, var numCols: Int, var grid: IntArray)

class TileMapData {
    companion object {
        val levelData = arrayOf(
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 0, 0, 1,
                        1, 0, 0, 1,
                        1, 1, 1, 1
                ))
        )
    }
}