package com.example.analysisgame.presentation.game

class GameLoop(
    private var gamePanel: GamePanel
) : Runnable {
    private var gameThread: Thread = Thread(this)


    override fun run() {

        var lastFPSCheck = System.currentTimeMillis()
        var fps = 0

        var lastDelta = System.nanoTime()
        val nanoSec = 1_000_000_000

        while (true){
            val nowDelta = System.nanoTime()
            val timeSinceLastDelta = nowDelta - lastDelta
            val delta = timeSinceLastDelta / nanoSec.toDouble()

            gamePanel.update(delta)
            gamePanel.render()
            lastDelta = nowDelta

            fps++

            val now = System.currentTimeMillis()
            if(now - lastFPSCheck >= 1000){
                //println("FPS: $fps")
                fps = 0
                lastFPSCheck += 1000
            }
        }
    }

    fun startGameLoop(){
        gameThread.start()
    }
}