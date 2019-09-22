package eu.kamkry.astarvisualization

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.input.MouseDragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import kotlinx.coroutines.GlobalScope
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


enum class TileType {
    EMPTY, START, END, WALL, OPEN, CLOSED, PATH
}

class Tile(
        var type: TileType,
        val board: Board,
        var x: Int = -1,
        var y: Int = -1,
        var draggable: Boolean = false
) : Region() {

    var isBeingDragged = false
    var gCost = 999999999
    var hCost = 999999999
    var fCost = 999999999
    var parent: Tile? = null

    init {
        minWidth = SIDE / ROWS
        minHeight = SIDE / ROWS
        changeColor(type)
        fun setUpDragEvents() {
            setOnMousePressed {
                it.isDragDetect = true
                paint(it)
            }
            setOnDragDetected {
                startFullDrag()
            }
            setOnMouseDragged {
                if (draggable) {
                    when (type) {
                        TileType.START, TileType.END -> isBeingDragged = true
                        else -> Unit
                    }
                }
            }
            setOnMouseDragOver {
                if (board.player.isBeingDragged && board.player.draggable)
                    board.swapTiles(board.player, this)
                else if (board.target.isBeingDragged && board.target.draggable)
                    board.swapTiles(board.target, this)
                else paint(it)
            }
            setOnMouseDragReleased {
                isBeingDragged = false
                board.player.isBeingDragged = false
                board.target.isBeingDragged = false
            }
        }
        setUpDragEvents()
    }

    fun setCoords(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    private fun paint(it: MouseEvent) {
        if (type == TileType.EMPTY || type == TileType.WALL)
            if (it.isPrimaryButtonDown)
                changeColor(TileType.WALL)
            else if (it.isSecondaryButtonDown)
                changeColor(TileType.EMPTY)
    }

    fun setCosts(start: Tile, end: Tile) {
        gCost = this.distanceTo(start)
        hCost = this.distanceTo(end)
        fCost = gCost + hCost
    }

    fun distanceTo(tile: Tile): Int {
        val distX = abs(this.x - tile.x)
        val distY = abs(this.y - tile.y)
        val dist = 10 * (distX + distY) + (14 - 2 * 10) * min(distX, distY)
        return dist
    }

    fun changeColor(new: TileType) {
        type = new
        Platform.runLater {
            when (type) {
                TileType.START -> this.style = "-fx-background-color: #5A99D0;"
                TileType.END -> this.style = "-fx-background-color: #9C113B;"
                TileType.WALL -> this.style = "-fx-background-color: #333652;"
                TileType.EMPTY -> this.style = "-fx-background-color: #E9EAEC;"
                TileType.OPEN -> this.style = "-fx-background-color: #5C8545;"
                TileType.CLOSED -> this.style = "-fx-background-color: #739E5A;"
                TileType.PATH -> this.style = "-fx-background-color:  #FAD02C;"
            }
        }
    }

    fun reset() {
        gCost = 0
        hCost = 0
        fCost = 0
        parent = null
    }



    override fun toString(): String {
        return "${type.name}:($x,$y), (g=$gCost,h=$hCost,f=$fCost)"
    }
}