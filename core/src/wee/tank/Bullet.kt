package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.cos
import kotlin.math.sin

class Bullet : Actor() {
    private val texture = TextureRegion(Texture("bullet.png"))
    /** 衝突判定用の枠 */
    var rect = Rectangle()

    /** 弾丸の方向 三時の方向から反時計回りにラジアン */
    var angle = 0f
        private set

    init {
        width = texture.regionWidth.toFloat()
        height = texture.regionHeight.toFloat()

        setPosition(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)
    }

    fun dispose()
    {
        texture.texture.dispose()
    }

    override fun act(delta: Float) {
        val d = 7
        x += d * cos(angle)
        y += d * sin(angle)

        if (x < 0 || x > STAGE_WIDTH || y < 0 || y > STAGE_HEIGHT) {
            //println("remove $delta")
            remove()
        }

        rect.set(x - width / 2 + 2, y - height / 2 + 5, width - 4, height - 10)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.apply {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, Math.toDegrees(angle.toDouble()).toFloat())
        }
    }

    /**
     * 弾丸の生成
     * @param angle: 弾丸の方向 三時の方向から反時計回りにラジアン
     */
    fun activate(x: Float, y: Float, angle: Float) {
        setPosition(x, y)
        this.angle = angle
    }
}