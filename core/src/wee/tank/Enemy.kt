package wee.tank

import com.badlogic.gdx.math.Vector2
import kotlin.math.PI
import kotlin.math.atan2

abstract class Enemy : Tank() {
    /** 弾を発射したいか否か */
    var wantShoot = false
    /** 目的地 */
    var destination = Vector2()

    override fun act(delta: Float) {
        //if (wantShoot) wantShoot = false
        //if (cnt % 60 == 0) wantShoot = true

        val direction = destination.cpy().sub(x, y)
        if (direction.len() >= MOVE_SPEED) {
            direction.setLength(MOVE_SPEED.toFloat()).run { moveBy(x, y) }
        } else {
            destination.run { setPosition(x, y) }
        }

        super.act(delta)
    }

    /**
     * 敵機の生成
     */
    fun activate(x: Float, y: Float) {
        setPosition(x, y)
        destination.set(x, y)
    }

    /**
     * playerやbulletの位置からtargetと動きを決める
     */
    abstract fun decideTargetAndMovement(player: Player, bullets: Array<Bullet>)
}