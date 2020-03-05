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

class Enemy : Actor() {
    private val textureBody = Texture("tank_body.png")
    private val textureGun = TextureRegion(Texture("tank_gun.png"))
    /** 衝突判定用の枠 */
    var rect = Rectangle()

    private var target = Vector2(0f, 0f)
    /** 砲台の角度<p>
     * 三時の方向から反時計回りにラジアン */
    var angle = 0f
    /** 弾を発射したいか否か */
    var wantToShot = false
    private var cnt = 0

    init {
        /* 拡大・縮小時も滑らかにする. */
        textureBody.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = textureBody.width.toFloat()
        height = textureBody.height.toFloat()

        setPosition(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)
    }

    override fun act(delta: Float) {
        if (wantToShot) wantToShot = false
        if (cnt % 60 == 0) wantToShot = true

        rect.set(x - width / 2 + 10, y - height / 2 + 10, width - 20, height - 20)
        cnt++
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(textureBody, x - width / 2, y - height / 2)
            //draw(textureGun, x - width / 2, y - height / 2)
            draw(textureGun, x - width / 2, y - height / 2, width / 2, height / 2,
                    width, height, 1f, 1f, Math.toDegrees(angle.toDouble()).toFloat())
        }
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