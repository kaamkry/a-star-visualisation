package eu.kamkry.astarvisualization

import com.guigarage.flatterfx.FlatterFX
import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration

const val WIDTH = 700.0
const val HEIGHT = 800.0


class App : Application() {

    @Throws(Exception::class)
    override fun start(stage: Stage) {
        stage.title = "A* Algorithm Visualization"
        stage.width = WIDTH
        stage.height = HEIGHT
        stage.isResizable = false

        stage.setOnCloseRequest {
            Platform.exit()
            System.exit(0)
        }
        stage.show()
        Menu(stage)
    }
}

fun main() {
    Application.launch(App::class.java)
}

fun Node.fade(ms:Double, from:Double, to:Double){
    val ft = FadeTransition(Duration.millis(ms), this)
    ft.fromValue = from
    ft.toValue = to
    ft.play()
}