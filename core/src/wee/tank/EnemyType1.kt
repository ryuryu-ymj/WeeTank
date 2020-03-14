package wee.tank

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import kotlin.math.PI
import kotlin.math.atan2

/**
 * 固定砲台タイプのtank
 */
class EnemyType1(x: Float, y: Float): Enemy(x, y) {
    override fun decideTargetAndMovement(player: Player, bullets: List<Bullet>, blocks: MutableList<Block>) {
        bullets.filter {
            it.hasParent()
        }.minBy {
            Vector2(x - it.x, y - it.y).len()
        }?.let { bullet ->
            val l = Vector2(x - bullet.x, y - bullet.y)
            val d = Vector2(1f, 0f).rotateRad(bullet.angle)
            val d2 = d.cpy().scl(l.dot(d))
            /** tankからbulletの弾道への垂線ベクトル */
            val h = d2.cpy().sub(l)
            if (h.len() < 70 && l.dot(d) > 0 && l.len() < 400) {
                if ((0..15).random() == 0) {
                    val bulletToTank = atan2(y - bullet.y, x - bullet.x)
                    angle = bulletToTank * 2 - bullet.angle + PI.toFloat()//最も近くかつ当たる弾を打ち返す
                    wantShoot = true
                }
                return
            } else {
                wantShoot = false
            }
        }

        if (!blocks.any {
                    Intersector.intersectSegmentRectangle(Vector2(x, y), Vector2(player.x, player.y), it.rect)
                }) {
            //println("no obstacle")
            angle = atan2(player.y - y, player.x - x)
            wantShoot = when {
                wantShoot -> false
                (0..300).random() == 0 -> true //ときたまplayerを狙ってくる
                else -> false
            }
        }
    }
}