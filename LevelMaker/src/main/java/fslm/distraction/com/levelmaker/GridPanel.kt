package fslm.distraction.com.levelmaker

import fslm.distraction.com.levelmaker.Tile.TileObject.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class TileObjectData(val tileObject: Tile.TileObject, val row: Int, val col: Int)

class Tile(var row: Int, var col: Int, val rect: Rectangle = Rectangle(), tile: Boolean = false, active: Boolean = false, val objects: ArrayList<TileObject> = arrayListOf()) {
    enum class TileObject {
        RIGHT,
        UP,
        DOWN,
        LEFT,
        JUMP,
        TELEPORT;

        fun isArrow() = this == RIGHT || this == UP || this == DOWN || this == LEFT
    }

    var tile = tile
        set(value) {
            if (!value) {
                active = false
                objects.clear()
            }
            field = value
        }

    var active = active
        set(value) {
            if (tile) {
                field = value
            }
        }

    val inactiveColor = Color(0, 40, 0)
    val activeColor = Color(0, 220, 0)
    val arrowColor = Color(40, 100, 250)
    val jumpColor = Color(250, 250, 40)
    val teleportColor = Color(50, 150, 255)
    val stroke3 = BasicStroke(3f)

    fun tileObject(tileObject: TileObject) {
        if (!tile) {
            return
        }
        if (objects.contains(tileObject)) {
            objects.remove(tileObject)
            return
        }

        if (tileObject.isArrow()) {
            objects.removeAll {
                it.isArrow() || it == TELEPORT
            }
        } else if (tileObject == JUMP) {
            objects.removeAll {
                it == TELEPORT
            }
        } else if (tileObject == TELEPORT) {
            objects.removeAll {
                it.isArrow() || it == JUMP
            }
        }
        val index = if (tileObject.isArrow()) objects.size else 0
        objects.add(index, tileObject)
    }

    fun draw(g: Graphics2D) {
        g.translate(rect.x, rect.y)
        if (tile) {
            g.color = if (active) activeColor else inactiveColor
            g.fillRect(0, 0, 32, 32)
        } else {
            g.color = Color.GRAY
            g.fillRect(0, 0, 32, 32)
        }
        val c = g.color
        val s = g.stroke
        g.stroke = stroke3
        for (tileObject in objects) {
            when (tileObject) {
                LEFT -> {
                    g.color = arrowColor
                    g.drawLine(rect.width / 4, rect.height / 2, 3 * rect.width / 4, 3 * rect.height / 4)
                    g.drawLine(rect.width / 4, rect.height / 2, 3 * rect.width / 4, rect.height / 4)
                }
                UP -> {
                    g.color = arrowColor
                    g.drawLine(rect.width / 2, rect.height / 4, rect.width / 4, 3 * rect.height / 4)
                    g.drawLine(rect.width / 2, rect.height / 4, 3 * rect.width / 4, 3 * rect.height / 4)
                }
                RIGHT -> {
                    g.color = arrowColor
                    g.drawLine(3 * rect.width / 4, rect.height / 2, rect.width / 4, 3 * rect.height / 4)
                    g.drawLine(3 * rect.width / 4, rect.height / 2, rect.width / 4, rect.height / 4)
                }
                DOWN -> {
                    g.color = arrowColor
                    g.drawLine(rect.width / 2, 3 * rect.height / 4, rect.width / 4, rect.height / 4)
                    g.drawLine(rect.width / 2, 3 * rect.height / 4, 3 * rect.width / 4, rect.height / 4)
                }
                JUMP -> {
                    g.color = jumpColor
                    g.drawRect(rect.width / 4, rect.height / 4, rect.width / 2, rect.height / 2)
                }
                TELEPORT -> {
                    g.color = teleportColor
                    g.drawOval(rect.width / 4, rect.height / 4, rect.width / 2, rect.height / 2)
                }
            }
        }
        g.color = c
        g.stroke = s
        g.translate(-rect.x, -rect.y)
    }
}

data class Vector2(var x: Int = 0, var y: Int = 0)

fun JPanel.paintImmediately() = paintImmediately(0, 0, width, height)

class GridPanel : JPanel() {
    var clickType = TilePanel.ClickType.TILE
        set(value) {
            if (value == TilePanel.ClickType.CLEAR) {
                for (tile in tiles) {
                    tile.tile = false
                }
                paintImmediately()
            } else if (value == TilePanel.ClickType.DEACTIVATE) {
                for (tile in tiles) {
                    tile.active = false
                }
                moveCount = 0
                paintImmediately()
            } else {
                field = value
            }
        }

    val size = 32
    val numRows = 24
    val numCols = 24
    val totalTiles = numRows * numCols
    val tiles = Array(totalTiles) {
        val (row, col) = indexToTile(it)
        Tile(row, col).apply {
            val (x, y) = tileToPosition(row, col)
            rect.setBounds(x, y, size, size)
        }
    }
    var moveCount = 0

    init {
        preferredSize = Dimension(size * numCols, size * numRows)
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                e?.let {
                    val (row, col) = positionToTile(e.x, e.y)
                    val index = row * numCols + col
                    with(tiles[index]) {
                        when (clickType) {
                            TilePanel.ClickType.TILE -> tile = !tile
                            TilePanel.ClickType.ACTIVE -> {
                                active = !active
                                moveCount++
                            }
                            TilePanel.ClickType.RIGHT -> tileObject(RIGHT)
                            TilePanel.ClickType.UP -> tileObject(UP)
                            TilePanel.ClickType.DOWN -> tileObject(DOWN)
                            TilePanel.ClickType.LEFT -> tileObject(LEFT)
                            TilePanel.ClickType.JUMP -> tileObject(JUMP)
                            TilePanel.ClickType.TELEPORT -> tileObject(TELEPORT)
                            else -> {}
                        }
                    }
                    paintImmediately()
                }
            }
        })
        isFocusable = true
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        for (tile in tiles) {
            tile.draw(g2)
        }
        val c = g2.color
        g2.color = Color.BLACK
        for (i in 0..size) {
            g2.drawLine(0, i * size, size * numCols, i * size)
            g2.drawLine(i * size, 0, i * size, size * numRows)
        }
        g2.color = Color.WHITE
        g2.fillRect(0, 0, 100, 20)
        g2.color = Color.BLACK
        g2.drawString("move count: $moveCount", 5, 15)
        g2.color = c
    }

    fun positionToTile(x: Int, y: Int) = Vector2(y / size, x / size)
    fun tileToPosition(row: Int, col: Int) = Vector2(col * size, row * size)
    fun indexToTile(index: Int) = Vector2(index / numCols, index % numCols)
    fun tileToIndex(row: Int, col: Int) = row * numCols + col

    fun createLevelText(): String {
        val sb = StringBuilder()

        var minRow = numRows
        var minCol = numCols
        var maxRow = 0
        var maxCol = 0
        for (index in 0 until totalTiles) {
            if (tiles[index].tile) {
                val (row, col) = indexToTile(index)
                if (row < minRow) {
                    minRow = row
                }
                if (col < minCol) {
                    minCol = col
                }
                if (row > maxRow) {
                    maxRow = row
                }
                if (col > maxCol) {
                    maxCol = col
                }
            }
        }

        val gridRows = maxRow - minRow + 1
        val gridCols = maxCol - minCol + 1
        val minIndex = tileToIndex(minRow, minCol)
        val grid = ArrayList<Int>(gridRows * gridCols)
        val objects = ArrayList<TileObjectData>()

        for (row in minRow..maxRow) {
            for (col in minCol..maxCol) {
                val index = tileToIndex(row, col)
                val tile = tiles[index]
                val arow = row - minRow
                val acol = col - minCol
                grid.add(if (tile.tile) 1 else 0)
                for (tileObject in tile.objects) {
                    objects.add(TileObjectData(tileObject, arow, acol))
                }
            }
        }
        objects.sortWith(Comparator { t1, t2 ->
            if (t1.tileObject.isArrow()) -1 else 1
        })

        sb.append("TileMapDataModel($gridRows, $gridCols, intArrayOf(\n")
        sb.append("\t${grid.joinToString(", ", "\n", gridCols)}\n)${if (objects.size > 0) ", arrayOf(\n" else ")"}")
        if (objects.size > 0) {
            sb.append("\t\t${objects.joinToString(",\n", transform = {
                when (it.tileObject) {
                    RIGHT -> "arrowRight(${it.row}, ${it.col})"
                    UP -> "arrowUp(${it.row}, ${it.col})"
                    DOWN -> "arrowDown(${it.row}, ${it.col})"
                    LEFT -> "arrowLeft(${it.row}, ${it.col})"
                    JUMP -> "superJump(${it.row}, ${it.col})"
                    TELEPORT -> "teleport(${it.row}, ${it.col})"
                }
            })}))")
        }
        return sb.toString()
    }
}

fun <T> Iterable<T>.joinToString(separator: CharSequence = ", ", separator2: CharSequence = "", separator2interval: Int = 0, prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): String {
    return joinTo(StringBuilder(), separator, separator2, separator2interval, prefix, postfix, limit, truncated, transform).toString()
}
fun <T, A : Appendable> Iterable<T>.joinTo(buffer: A, separator: CharSequence = ", ", separator2: CharSequence = "", separator2interval: Int = 0, prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null): A {
    buffer.append(prefix)
    var count = 0
    var count2 = 0
    for (element in this) {
        if (++count > 1) buffer.append(separator)
        if (++count2 > separator2interval) {
            count2 = 1
            buffer.append(separator2)
        }
        if (limit < 0 || count <= limit) {
            buffer.appendElement(element, transform)
        } else break
    }
    if (limit in 0..(count - 1)) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}
fun <T> Appendable.appendElement(element: T, transform: ((T) -> CharSequence)?) {
    when {
        transform != null -> append(transform(element))
        element is CharSequence? -> append(element)
        element is Char -> append(element)
        else -> append(element.toString())
    }
}
