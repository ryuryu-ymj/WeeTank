package wee.tank

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class MyActor: Actor() {
    /** 衝突判定用の枠 */
    val rect = Rectangle()

    abstract fun dispose()
}