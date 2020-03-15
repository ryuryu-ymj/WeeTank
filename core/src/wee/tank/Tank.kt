package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

/**
 * PlayerやEnemyの親クラス
 */
open class Tank : Actor() {
    companion object {
        private val textureBody = Texture("tank_body.png")
        private val textureGun = TextureRegion(Texture("tank_gun.png"))
    }

    /** 衝突判定用の枠 */
    val rect = Rectangle()

    /** 弾を打つ方向<p>
     * 三時の方向から反時計回りにラジアン */
    var angle = 0f
        protected set

    /** 弾の最大装填数 */
    private val bulletCntMax = 5

    /** 弾を打った後，次打てるまでの時間 */
    private val cantShootTime = 10

    /** 弾の装填にかかる時間 */
    private val recoverBulletTime = 30

    /** 移動速度 */
    val moveSpeed = 3

    /** 直近の弾を打ってからの経過時間 */
    private var noShootTime = 0

    /**
     * 所持する弾の数<p>
     * 一定時間打たないと回復する
     */
    private var bulletCnt = bulletCntMax

    init {
        /* 拡大・縮小時も滑らかにする. */
        textureBody.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = textureBody.width.toFloat()
        height = textureBody.height.toFloat()
    }

    fun dispose() {
        clear()
        textureBody.dispose()
        textureGun.texture.dispose()
    }

    override fun act(delta: Float) {
        noShootTime++
        if (noShootTime > recoverBulletTime) bulletCnt = bulletCntMax

        rect.set(x - width / 2 + 10, y - height / 2 + 10, width - 20, height - 20)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(textureBody, x - width / 2, y - height / 2)
            //draw(textureGun, x - width / 2, y - height / 2)
            draw(textureGun, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, Math.toDegrees(angle.toDouble()).toFloat())
        }
    }

    /**
     * 弾を打った時に呼び出す処理
     */
    fun shoot() {
        noShootTime = 0
        bulletCnt--
    }

    /**
     * 弾を打てるか否か
     */
    fun canShoot() = (noShootTime > cantShootTime && bulletCnt != 0)

    /**
     * blockに触れているときに呼び出す
     * @param block: 触れているblock
     */
    fun touchBlock(block: Block) {
        val a = ((y - block.y) / (x - block.x))
        if (a > -1 && a < 1) {
            x = if (x - block.x > 0) {
                block.x + block.width / 2 + rect.width / 2
            } else {
                block.x - block.width / 2 - rect.width / 2
            }
        } else {
            y = if (y - block.y > 0) {
                block.y + block.height / 2 + rect.height / 2
            } else {
                block.y - block.height / 2 - rect.height / 2
            }
        }
    }
}