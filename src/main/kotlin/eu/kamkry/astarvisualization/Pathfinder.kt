package eu.kamkry.astarvisualization


import javafx.application.Platform
import java.lang.NullPointerException
import java.lang.management.PlatformLoggingMXBean
import kotlin.collections.ArrayList


class Pathfinder(val tiles: Array<Array<Tile?>>, var speed: Double = 50.0) : Runnable {

    val open = ArrayList<Tile>()
    val closed = ArrayList<Tile>()
    var startNode = find(TileType.START)
    var endNode = find(TileType.END)

    var running: Boolean = false


    override fun run() {
        running = true
        startNode.setCosts(startNode, endNode)
        open.add(startNode)
        findPath()
    }

    fun findPath() {
        setDraggable(false)
        while (open.size > 0) {
            val current = open[0]
            if (current.hCost == 0) break
            open.remove(current)
            closed.add(current)
            current.neighbors.forEach { neighbor ->
                neighbor?.apply {
                    if (type != TileType.WALL && !closed.contains(this)) {
                        val newLowestGCost = current.gCost + current.distanceTo(this)
                        if (!open.contains(this) || newLowestGCost < gCost) {
                            setCosts(startNode, endNode)
                            gCost = newLowestGCost
                            fCost = hCost + gCost
                            if (!open.contains(this)) {
                                open.add(this)
                                parent = current
                                changeColor(TileType.OPEN)
                            }
                        }
                    }
                }
            }
            current.changeColor(TileType.CLOSED)
            showStartEnd()
            Thread.sleep((100 / speed).toLong())
        }
        if (endNode.parent != null)
            printPathRecursively(endNode)
        else notFound()
        setDraggable(true)
        running = false
    }

    private fun notFound() {
        tiles.forEach {
            it.forEach {
                if (it?.type == TileType.OPEN || it?.type == TileType.CLOSED) {
                    Platform.runLater { it.style = "-fx-background-color: #9C113B;" }
                }
            }
        }
    }


    private fun setDraggable(bool: Boolean) {
        startNode.draggable = bool
        endNode.draggable = bool
    }

    private fun showStartEnd() {
        startNode.changeColor(TileType.START)
        endNode.changeColor(TileType.END)
    }

    private fun printPathRecursively(tile: Tile) {
        tile.changeColor(TileType.PATH)
        showStartEnd()
        Thread.sleep((100 / speed).toLong())
        tile.parent?.let { printPathRecursively(it) }
    }

    private val Tile.neighbors: List<Tile?>
        get() {
            val neighbors = ArrayList<Tile?>()
            if (x > 0) neighbors.add(tiles[x - 1][y])
            if (x < tiles.size - 1) neighbors.add(tiles[x + 1][y])
            if (y > 0) neighbors.add(tiles[x][y - 1])
            if (y < tiles[0].size - 1) neighbors.add(tiles[x][y + 1])


            if (x > 0 && y > 0) {
                if (tiles[x - 1][y]?.type != TileType.WALL ||
                        tiles[x][y - 1]?.type != TileType.WALL)
                    neighbors.add(tiles[x - 1][y - 1])
            }
            if (x < tiles.size - 1 && y < tiles[0].size - 1)
                if (tiles[x + 1][y]?.type != TileType.WALL ||
                        tiles[x][y + 1]?.type != TileType.WALL)
                    neighbors.add(tiles[x + 1][y + 1])

            if (x > 0 && y < tiles.size - 1)
                if (tiles[x - 1][y]?.type != TileType.WALL ||
                        tiles[x][y + 1]?.type != TileType.WALL)
                    neighbors.add(tiles[x - 1][y + 1])

            if (x < tiles.size - 1 && y > 0)
                if (tiles[x + 1][y]?.type != TileType.WALL ||
                        tiles[x][y - 1]?.type != TileType.WALL)
                    neighbors.add(tiles[x + 1][y - 1])

            return neighbors
        }

    private fun find(type: TileType): Tile {
        for (t in tiles) {
            for (tile in t)
                if (tile?.type == type)
                    return tile
        }
        throw NullPointerException()
    }

}

