package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.random.Random

class Enemy : Tank() {
    private var target = Vector2(0f, 0f)
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

    fun decideAngle(player: Player, bullets: Array<Bullet>) {
        //近くに弾があれば狙う
        bullets.find { it.hasParent() && getDistance(x, y, it.x, it.y) < 500 }.let {
            angle = if (it != null) {
                atan2(it.y - y, it.x - x)
            } else { //なければプレイヤーを狙う
                atan2(player.y - y, player.x - x)
            }
        }
    }

    /** 二点間の距離 */
    private fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}