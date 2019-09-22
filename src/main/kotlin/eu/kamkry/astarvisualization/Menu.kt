package eu.kamkry.astarvisualization

import com.guigarage.flatterfx.FlatterFX
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import javafx.stage.Stage
import kotlin.concurrent.thread


class Menu(stage: Stage) {

    val vBox = VBox()
    val title = Label("A* Algorithm Visualization")
    val author = Label("by Kamil Krysiak")
    val start = Button("Start")
    val exit = Button("Exit")

    init {
        val gamePanel = GamePanel(stage)
        FlatterFX.style()

        title.style = "-fx-font-size: 35"
        author.style = "-fx-font-size: 12"

        val authorAlignment = HBox()
        authorAlignment.alignment = Pos.CENTER_RIGHT
        authorAlignment.padding = Insets(0.0,25.0,0.0,0.0)
        authorAlignment.children.add(author)

        val titleBox = VBox()
        titleBox.children.addAll(title, authorAlignment)
        titleBox.alignment = Pos.CENTER

        start.prefWidth = 200.0
        start.styleClass.addAll("start-button","lg")
        start.setOnAction {
            vBox.children.forEach { it.fade(250.0, 1.0, 0.0) }
            thread {
                Thread.sleep(250)
                Platform.runLater {
                    gamePanel.show()
                }
            }
        }

        exit.styleClass.addAll("exit-button","lg")
        exit.minWidth = 200.0
        exit.setOnAction {
            Platform.exit()
            System.exit(0)
        }

        vBox.alignment = Pos.CENTER
        vBox.padding = Insets(100.0)
        vBox.spacing = 30.0
        vBox.children.addAll(titleBox, start, exit)
        vBox.children.forEach { it.fade(1000.0, 0.0, 1.0) }
        val scene = Scene(vBox)
        scene.stylesheets.add(javaClass.getResource("/css/App.css").toExternalForm())
        stage.scene = scene
    }


}