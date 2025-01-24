package org.command.State

interface IState {
    fun display()

    fun click()
}

class PauseState(
    private val context: IAudioPlayerImpl
): IState {
    override fun display() {
        println("audio playback stopped")
    }

    override fun click() {
        context.setPlayState()
    }
}

class PlayState(
    private val context: IAudioPlayerImpl
): IState {
    override fun display() {
        println("audio playback is active")
    }

    override fun click() {
        context.setPauseState()
    }
}

interface IAudioPlayer {
    fun print()
    fun click()
}

interface IAudioPlayerImpl {
    fun setPauseState()
    fun setPlayState()
}

class AudioPlayerButton(): IAudioPlayer {
    //внутренний приват объект aka class
    private val audioPlayerImpl: IAudioPlayerImpl = object: IAudioPlayerImpl {
        override fun setPauseState() {
            mState = pauseState
        }

        override fun setPlayState() {
            mState = playState
        }
    }

    private val pauseState = PauseState(audioPlayerImpl)
    private val playState = PlayState(audioPlayerImpl)
    private var mState: IState = pauseState


    override fun print() {
        mState.display()
    }

    override fun click() {
        mState.click()
    }
}

fun main() {
    val audioPlayerButton = AudioPlayerButton()

    repeat(5) {
        audioPlayerButton.click()
        audioPlayerButton.print()
    }
}