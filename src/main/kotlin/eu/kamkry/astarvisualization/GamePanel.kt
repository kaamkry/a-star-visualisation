package eu.kamkry.astarvisualization

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.StringConverter


class GamePanel(val stage: Stage) {

    val vBox = setBackground()

    val board = Board()
    var pathfinder: Pathfinder? = null
    val slider = Slider()
    val startButton = Button("Start")
    val resetButton = Button("Clear")
    val exitButton = Button("Exit")


    init {


        vBox.alignment = Pos.TOP_CENTER
        vBox.padding = Insets(55.0, 0.0, 0.0, 0.0)
        vBox.spacing = 55.0
        setSlider()

        startButton.setOnAction { start() }
        board.setOnMousePressed { clear() }
        resetButton.setOnAction { reset() }
        exitButton.setOnAction { exit() }


        setButtonsStyle()
        val buttonHBox = setButtonBox()

        vBox.getChildren().addAll(board, buttonHBox)


    }

    fun show() {
        val scene = Scene(vBox)
        scene.stylesheets.add(javaClass.getResource("/css/App.css").toExternalForm())

        vBox.children.forEach { it.fade(500.0, 0.0, 1.0) }
        stage.scene = scene
    }

    private fun setButtonsStyle() {
        startButton.styleClass.addAll("start-button","sm")
        startButton.prefWidth = 80.0
        resetButton.styleClass.addAll("reset-button","sm")
        resetButton.prefWidth = 80.0
        exitButton.styleClass.addAll("exit-button","sm")
        exitButton.prefWidth = 60.0


    }

    private fun setButtonBox(): HBox {
        val filler = Region()
       filler.prefWidth = 20.0

        val buttonHBox = HBox()
        with(buttonHBox) {
            padding = Insets(0.0,0.0,0.0,125.0)
            alignment = Pos.CENTER_LEFT
            spacing = 25.0
            children.addAll(startButton,slider, resetButton,exitButton)
        }
        return buttonHBox
    }

    private fun setBackground(): VBox {
        val vBox = VBox()
        vBox.setAlignment(Pos.CENTER)
        vBox.style = "-fx-background-color: #202233;"
        vBox.spacing = 80.0
        return vBox
    }

    private fun setSlider() {
        slider.min = 0.5
        slider.max = 100.0
        slider.value = 50.0
        slider.minWidth = 250.0
        slider.maxHeight = 10.0

        slider.setMinorTickCount(0)
        slider.setMajorTickUnit(100.0)
        slider.setShowTickMarks(true)
        slider.setShowTickLabels(true)


        slider.valueProperty().addListener({ _, _, _ ->
            pathfinder?.speed = slider.value
        })
        slider.labelFormatter = object : StringConverter<Double>() {
            override fun fromString(string: String?): Double {
                when (string) {
                    "slow" -> return 0.5
                    else -> return 100.0
                }
            }

            override fun toString(n: Double): String {
                if (n < 1.0) return "slow"
                else return "fast"
            }
        }
    }

    private fun start() {
        pathfinder = Pathfinder(board.getArray(), slider.value)
        Thread(pathfinder).start()
        startButton.isDisable = true

    }

    private fun clear() {
        pathfinder?.apply {
            if (!running) {
                board.clear()
                running = true
                startButton.isDisable = false
            }
        }
    }

    private fun reset() {
        pathfinder?.apply {
            board.reset()
            running = true
            startButton.isDisable = false
        }
    }

    private fun exit() {
        vBox.children.forEach { it.fade(500.0, 1.0, 0.0) }
        Menu(stage)
    }

}