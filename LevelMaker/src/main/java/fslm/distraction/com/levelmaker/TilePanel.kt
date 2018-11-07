package fslm.distraction.com.levelmaker

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

interface ClickTypeListener {
    fun onClickType(clickType: TilePanel.ClickType)
}

class TilePanel(val clickTypeListener: ClickTypeListener) : JPanel(), ActionListener {
    enum class ClickType(val tileType: Boolean = true) {
        TILE,
        ACTIVE,
        RIGHT,
        UP,
        DOWN,
        LEFT,
        JUMP,
        TELEPORT,
        CLEAR(false),
        DEACTIVATE(false)
    }

    val label = JLabel(ClickType.TILE.name)

    override fun actionPerformed(e: ActionEvent?) {
        e?.let {
            val clickType = ClickType.valueOf(e.actionCommand)
            if (!clickType.tileType) {
                if (JOptionPane.showConfirmDialog(null, clickType.name, "", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    return
                }
            }
            clickTypeListener.onClickType(clickType)
            if (clickType.tileType) {
                label.text = e.actionCommand
            }
        }
    }

    init {
        preferredSize = Dimension(300, 100)
        isFocusable = true

        for (clickType in ClickType.values()) {
            addButton(clickType.name)
        }
        add(label)
    }

    fun addButton(text: String) {
        add(JButton(text).apply {
            addActionListener(this@TilePanel)
            actionCommand = text
        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        g?.let {
            val c = g.color
            g.color = Color.BLACK
            g.fillRect(0, 0, width, height)
            g.color = c
        }
    }
}