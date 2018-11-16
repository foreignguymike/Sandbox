package fslm.distraction.com.levelmaker

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.*

fun main(args: Array<String>) {
    LevelMaker()
}

class LevelMaker : ClickTypeListener, ActionListener {
    val frame = JFrame()
    val gridPanel = GridPanel()
    val tilePanel = TilePanel(this)

    init {

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val menubar = JMenuBar()
        val fileMenu = JMenu("File")
        menubar.add(fileMenu)

        fileMenu.add(createMenuItem("Export", KeyEvent.VK_E))
        fileMenu.add(createMenuItem("Export active", KeyEvent.VK_A))
        frame.jMenuBar = menubar

        frame.contentPane = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }
        frame.contentPane.add(gridPanel)
        frame.contentPane.add(tilePanel)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    override fun onClickType(clickType: TilePanel.ClickType) {
        gridPanel.clickType = clickType
    }

    override fun actionPerformed(e: ActionEvent?) {
        e?.let {
            when (e.actionCommand) {
                "Export" -> {
                    val str = gridPanel.createLevelText()
                    JOptionPane.showMessageDialog(frame, str)
                    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(str), null)
                }
                "Export active" -> {
                    val str = gridPanel.createLevelText(true)
                    JOptionPane.showMessageDialog(frame, str)
                    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(str), null)
                }
            }
        }
    }

    fun createMenuItem(text: String, key: Int) = JMenuItem(text).apply {
        accelerator = KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK)
        actionCommand = text
        addActionListener(this@LevelMaker)
    }
}