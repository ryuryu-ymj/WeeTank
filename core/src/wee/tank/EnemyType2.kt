package wee.tank

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

/**
 * 移動普通タイプのtank
 */
class EnemyType2(x: Float, y: Float) : Enemy(x, y) {
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
                if (getTrueSometimes(7)) {
                    destination = Vector2(x, y).sub(h.setLength(h.len() - 70)) //最も近くかつ当たる弾をよける
                }
                return
            }
        }

        if (!blocks.any {
                    Intersector.intersectSegmentRectangle(Vector2(x, y), Vector2(player.x, player.y), it.rect)
                }) {
            angle = atan2(player.y - y, player.x - x)
            wantShoot = when {
                wantShoot -> false
                getTrueSometimes(1) -> true //ときたまplayerを狙ってくる
                else -> false
            }
        }

        if (isReachDestination) {
            if (getTrueSometimes(2))
            {
                destination.add(100f * (-2..2).random(), 100f * (-2..2).random()) // 適当に動く
            }
        }

    }
}