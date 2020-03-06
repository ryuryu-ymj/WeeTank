package wee.tank

import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

class EnemyType2 : Enemy() {
    override fun decideTargetAndMovement(player: Player, bullets: Array<Bullet>) {
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
                    destination = Vector2(x, y).sub(h.setLength(h.len() - 70)) //最も近くかつ当たる弾をよける
                }
                return
            }
        }

        angle = atan2(player.y - y, player.x - x)
        wantShoot = when {
            wantShoot -> false
            (0..200).random() == 0 -> true //ときたまplayerを狙ってくる
            else -> false
        }
    }
}