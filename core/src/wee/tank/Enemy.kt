package wee.tank

import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2
import kotlin.math.sqrt

class Enemy : Tank() {
    /** 弾を発射したいか否か */
    var wantToShot = false
    private var cnt = 0

    override fun act(delta: Float) {
        if (wantToShot) wantToShot = false
        if (cnt % 60 == 0) wantToShot = true

        cnt++
        super.act(delta)
    }

    /**
     * 敵機の生成
     */
    fun activate(x: Float, y: Float) {
        setPosition(x, y)
    }

    fun decideTarget(player: Player, bullets: Array<Bullet>) {
        //近くに弾があれば狙う
        bullets.find { it.hasParent() && getDistance(x, y, it.x, it.y) < 500 }.let {
            target = if (it != null) {
                Vector2(it.x, it.y)
            } else { //なければプレイヤーを狙う
                Vector2(player.x, player.y)
            }
        }
        //println("${player.x}, ${player.y}")
    }

    /** 二点間の距離 */
    private fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}