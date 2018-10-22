package com.distraction.sandbox.tilemap

class TileMapDataModel(val numRows: Int, val numCols: Int, val grid: IntArray, val objs: Array<TileObjectDataModel> = arrayOf(), val startRow: Int = 0, val startCol: Int = 0)
class TileObjectDataModel(val type: TileObjectType, val row: Int, val col: Int)

enum class TileObjectType {
    ARROW_RIGHT,
    ARROW_LEFT,
    ARROW_DOWN,
    ARROW_UP,
    SUPER_JUMP
}

class TileMapData {
    companion object {
        val levelData = arrayOf(
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 0, 0, 1,
                        1, 0, 0, 1,
                        1, 1, 1, 1
                )),
                TileMapDataModel(3, 5, intArrayOf(
                        1, 1, 1, 1, 1,
                        1, 1, 0, 1, 1,
                        1, 1, 1, 1, 1
                ), arrayOf(
                        superJump(1, 1),
                        arrowRight(0, 2),
                        arrowLeft(2, 2))),
                TileMapDataModel(5, 5, intArrayOf(
                        0, 0, 1, 1, 1,
                        1, 1, 1, 0, 1,
                        1, 1, 1, 0, 1,
                        1, 1, 1, 0, 1,
                        0, 0, 1, 1, 1
                ), arrayOf(), 1, 0),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 0, 1,
                        1, 0, 1, 1,
                        1, 1, 1, 1
                ), arrayOf(
                        superJump(1, 1),
                        superJump(2, 2)))
        )

        private fun arrowUp(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_UP, row, col)
        private fun arrowLeft(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_LEFT, row, col)
        private fun arrowDown(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_DOWN, row, col)
        private fun arrowRight(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_RIGHT, row, col)
        private fun superJump(row: Int, col: Int) = TileObjectDataModel(TileObjectType.SUPER_JUMP, row, col)

    }
}