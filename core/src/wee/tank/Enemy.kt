package wee.tank

import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

class Enemy : Tank() {
    /** 弾を発射したいか否か */
    var wantShoot = false
    /** 目的地 */
    var destination = Vector2(800f, 800f)
    private var cnt = 0

    override fun act(delta: Float) {
        if (wantShoot) wantShoot = false
        //if (cnt % 60 == 0) wantShoot = true

        var direction = destination.cpy().sub(x, y)
        if (direction.len() >= MOVE_SPEED) {
            direction.setLength(MOVE_SPEED.toFloat()).run { moveBy(x, y) }
        } else {
            destination.run { setPosition(x, y) }
            //println(destination)
        }

        cnt++
        super.act(delta)
    }

    /**
     * 敵機の生成
     */
    fun activate(x: Float, y: Float) {
        setPosition(x, y)
    }

    /**
     * playerやbulletの位置からtargetと動きを決める
     */
    fun decideTargetAndMovement(player: Player, bullets: Array<Bullet>) {
        //最も近い弾をよける
        bullets.minBy {
            getDistanceFromBullet(it)
        }?.let { bullet ->
            val l = Vector2(x - bullet.x, y - bullet.y)
            val d = Vector2(1f, 0f).rotateRad(bullet.angle)
            //println(d)
            val d2 = d.scl(l.dot(d))
            /** tankからbulletの弾道への垂線ベクトル */
            val h = d2.sub(l)
            if (h.len() < 100) {
                //println("$h")
                destination = Vector2(x, y).sub(h.setLength(h.len() - 100))
                return
            }
            /*target = if (it != null) {
                Vector2(it.x, it.y)
            } else { //なければプレイヤーを狙う
                Vector2(player.x, player.y)
            }*/
        }
        //println("${player.x}, ${player.y}")
    }

    /** 二点間の距離 */
    private fun getPointsDistance(x1: Float, y1: Float, x2: Float, y2: Float) //
            = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))

    private fun getDistanceFromBullet(bullet: Bullet) //
            = getPointsDistance(x, y, bullet.x, bullet.y)

    private fun getDistanceFromBulletLine(bullet: Bullet): Float {
        val l = Vector2(x - bullet.x, y - bullet.y)
        val d = Vector2().setAngleRad(angle)
        val d2 = d.scl(l.dot(d))
        val h = d2.sub(l)
        return h.len()
    }
}