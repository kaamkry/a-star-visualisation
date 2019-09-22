package eu.kamkry.astarvisualization

import javafx.geometry.Pos
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane

import kotlin.collections.ArrayList


const val ROWS = 50
const val SIDE = WIDTH-100

class Board : GridPane() {


    val player = Tile(TileType.START, this, draggable = true)
    val target = Tile(TileType.END, this, draggable = true)

    init {
        setMaxSize(SIDE, SIDE)
        setMinSize(SIDE, SIDE)
        alignment = Pos.CENTER
        vgap = 1.0
        hgap = 1.0
        width = SIDE
        height = SIDE
        setUpTiles()
    }

    fun setUpTiles() {
        for (i in 0..ROWS) {
            for (j in 0..ROWS) {
                val tile = Tile(TileType.EMPTY, this, i, j)

                add(tile, i, j)
            }
        }

        add(player, 0, 0)
        add(target, ROWS, ROWS)
        player.draggable=true
        target.draggable=true
    }

    fun swapTiles(dragged: Tile, released: Tile) {
        children.remove(dragged)
        add(dragged, getColumnIndex(released), getRowIndex(released))

    }

    fun reset() {
        player.reset()
        target.reset()
        children.forEach {
            if(it is Tile) {
                it.reset()
                if (it.type != TileType.START && it.type != TileType.END)
                    it.changeColor(TileType.EMPTY)
            }
        }
    }
    fun clear(){
        player.reset()
        target.reset()
        children.forEach {
            if(it is Tile)
            when(it.type){
                TileType.OPEN,
                TileType.CLOSED,
                TileType.PATH -> {
                    it.changeColor(TileType.EMPTY)
                    it.reset()
                }
                else -> Unit
            }
        }
    }

    fun getArray(): Array<Array<Tile?>> {
        val array: Array<Array<Tile?>> = Array(ROWS + 1) { arrayOfNulls<Tile>(ROWS + 1) }
        for (child in children) {
            if (child is Tile) {
                val x =getColumnIndex(child)
                val y = getRowIndex(child)
                array[x][y] = child
                child.setCoords(x,y)
            }
        }
        return array
    }
}