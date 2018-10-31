package com.distraction.sandbox.tilemap

class TileMapDataModel(val numRows: Int, val numCols: Int, val grid: IntArray, val objs: Array<TileObjectDataModel> = arrayOf(), val startRow: Int = 0, val startCol: Int = 0)
open class TileObjectDataModel(val type: TileObjectType, val row: Int, val col: Int)
class TeleportDataModel(type: TileObjectType, row: Int, col: Int, val row2: Int, val col2: Int) : TileObjectDataModel(type, row, col)

enum class TileObjectType {
    ARROW_RIGHT,
    ARROW_LEFT,
    ARROW_DOWN,
    ARROW_UP,
    SUPER_JUMP,
    TELEPORT
}

class TileMapData {
    companion object {
        val levelData = arrayOf(

                ////////////// BASIC LEVELS

                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 0, 0, 1,
                        1, 0, 0, 1,
                        1, 1, 1, 1
                )),
                TileMapDataModel(3, 3, intArrayOf(
                        1, 1, 1,
                        1, 1, 1,
                        1, 1, 1
                ), startRow = 1, startCol = 1),
                TileMapDataModel(5, 3, intArrayOf(
                        1, 1, 1,
                        1, 0, 1,
                        1, 1, 1,
                        1, 0, 1,
                        1, 1, 1
                ), startRow = 0, startCol = 1),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 0, 0, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1
                ), startRow = 2, startCol = 1),
                TileMapDataModel(6, 6, intArrayOf(
                        1, 1, 1, 1, 1, 1,
                        1, 0, 1, 1, 0, 1,
                        1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1,
                        1, 0, 1, 1, 0, 1,
                        1, 1, 1, 1, 1, 1
                )),

                ////////////////// ARROW LEVELS

                TileMapDataModel(3, 5, intArrayOf(
                        1, 1, 1, 1, 1,
                        1, 0, 0, 0, 1,
                        1, 1, 1, 1, 1
                ), arrayOf(
                        arrowRight(0, 1),
                        arrowRight(0, 2),
                        arrowRight(0, 3),
                        arrowLeft(2, 1),
                        arrowLeft(2, 2),
                        arrowLeft(2, 3))),
                TileMapDataModel(3, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1
                ), arrayOf(
                        arrowLeft(1, 1),
                        arrowRight(1, 2))),
                TileMapDataModel(4, 3, intArrayOf(
                        1, 1, 1,
                        1, 1, 1,
                        1, 1, 1,
                        1, 1, 1
                ), arrayOf(
                        arrowUp(1, 0), arrowUp(2, 0), arrowUp(3, 0),
                        arrowDown(0, 2), arrowDown(1, 2), arrowDown(2, 2)),
                        1, 1),
                TileMapDataModel(3, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1
                ), Array(8) { if (it < 4) arrowDown(0, it) else arrowUp(2, it - 4) },
                1, 0),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1
                ), arrayOf(
                        arrowLeft(1, 1),
                        arrowUp(1, 2),
                        arrowRight(2,2),
                        arrowDown(2, 1))),

                //////////////// SUPER JUMP LEVELS

                TileMapDataModel(5, 5, intArrayOf(
                        1, 1, 1, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 1, 1, 1
                ), arrayOf(
                        superJump(1, 2))),
                TileMapDataModel(2, 4, intArrayOf(
                        1, 1, 0, 1,
                        1, 0, 1, 1
                ), arrayOf(
                        superJump(0, 1),
                        superJump(1, 2))),
                TileMapDataModel(4, 3, intArrayOf(
                        1, 1, 1,
                        1, 1, 1,
                        1, 1, 1,
                        1, 1, 1
                ), arrayOf(
                        superJump(1, 1),
                        superJump(2, 1))),
                TileMapDataModel(5, 5, intArrayOf(
                        1, 1, 0, 1, 1,
                        1, 1, 1, 1, 1,
                        0, 0, 1, 0, 0,
                        1, 1, 1, 1, 1,
                        1, 1, 0, 1, 1
                ), arrayOf(
                        superJump(1, 4),
                        superJump(3, 0))),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        1, 1, 1, 1
                ), arrayOf(
                        superJump(1, 1),
                        superJump(1, 2),
                        superJump(2, 1),
                        superJump(2, 2))),

                ///////////// SUPER JUMP + ARROW LEVELS

                TileMapDataModel(5, 5, intArrayOf(
                        0, 0, 1, 1, 1,
                        0, 0, 1, 0, 1,
                        1, 1, 1, 1, 1,
                        1, 0, 1, 0, 0,
                        1, 1, 1, 0, 0
                ), arrayOf(
                        arrowRight(2, 1), arrowRight(2, 2), arrowRight(2, 3), arrowUp(2, 4),
                        arrowUp(1, 4), arrowLeft(0, 4), arrowLeft(0, 3), arrowDown(0, 2), arrowDown(1, 2),
                        arrowDown(3, 2), arrowLeft(4, 2), arrowLeft(4, 1), arrowUp(4, 0), arrowUp(3, 0),
                        superJump(1, 2)),
                        2, 0),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 0,
                        1, 1, 1, 1,
                        1, 1, 1, 1,
                        0, 1, 1, 1
                ), arrayOf(
                        arrowDown(0, 1), arrowUp(3, 2),
                        superJump(1, 1), superJump(2, 2))),

                //////////////// TELEPORT LEVELS

                TileMapDataModel(5, 5, intArrayOf(
                        0, 0, 1, 1, 1,
                        0, 0, 0, 0, 1,
                        1, 0, 0, 0, 1,
                        1, 0, 0, 0, 0,
                        1, 1, 1, 0, 0
                ), arrayOf(
                        teleport(4, 2, 0, 2)), 2, 0),
                TileMapDataModel(4, 4, intArrayOf(
                        1, 1, 1, 1,
                        1, 1, 0, 1,
                        1, 0, 1, 1,
                        1, 1, 1, 1
                ), arrayOf(
                        superJump(1, 1),
                        superJump(2, 2))),

                ///////////// HARD LEVELS

                TileMapDataModel(7, 3, intArrayOf(
                        0, 1, 0,
                        1, 1, 1,
                        1, 1, 1,
                        0, 0, 0,
                        1, 1, 1,
                        1, 1, 1,
                        0, 1, 0
                ), arrayOf(
                        superJump(2, 1),
                        superJump(4, 1)),
                        startRow = 0, startCol = 1)
        )

        private fun arrowUp(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_UP, row, col)
        private fun arrowLeft(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_LEFT, row, col)
        private fun arrowDown(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_DOWN, row, col)
        private fun arrowRight(row: Int, col: Int) = TileObjectDataModel(TileObjectType.ARROW_RIGHT, row, col)
        private fun superJump(row: Int, col: Int) = TileObjectDataModel(TileObjectType.SUPER_JUMP, row, col)
        private fun teleport(row: Int, col: Int, row2: Int, col2: Int) = TeleportDataModel(TileObjectType.TELEPORT, row, col, row2, col2)
    }

}
