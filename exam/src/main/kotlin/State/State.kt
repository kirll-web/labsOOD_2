package org.command.State

interface IState {
    fun display()

    fun click()
}

class PauseState(
    private val context: IAudioPlayerButtonImpl
): IState {
    override fun display() {
        println("audio playback stopped")
    }

    override fun click() {
        context.setPlayState()
    }
}

class PlayState(
    private val context: IAudioPlayerButtonImpl
): IState {
    override fun display() {
        println("audio playback is active")
    }

    override fun click() {
        context.setPauseState()
    }
}

interface IAudioPlayerButton {
    fun print()
    fun click()
}

interface IAudioPlayerButtonImpl {
    fun setPauseState()
    fun setPlayState()
}

class AudioPlayerButton(): IAudioPlayerButton {
    //внутренний приваттный класс
    class AudioPlayerButtonImpl(
        private val context: AudioPlayerButton
    ): IAudioPlayerButtonImpl {
        override fun setPauseState() {
            context.mState = context.pauseState
        }

        override fun setPlayState() {
            context.mState = context.playState
        }
    }
    private val mAudioPlayerButtonImlp = AudioPlayerButtonImpl(this)
    private val pauseState = PauseState(mAudioPlayerButtonImlp)
    private val playState = PlayState(mAudioPlayerButtonImlp)


    //можно через внутренний объект, который реализует интерфейс
   /* private val audioPlayerImpl: IAudioPlayerButtonImpl = object: IAudioPlayerButtonImpl {
        override fun setPauseState() {
            mState = pauseState
        }

        override fun setPlayState() {
            mState = playState
        }
    }
    private val pauseState = PauseState(audioPlayerImpl)
    private val playState = PlayState(audioPlayerImpl)
    */

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