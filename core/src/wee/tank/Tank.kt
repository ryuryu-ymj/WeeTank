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
    private val textureBody = Texture("tank_body.png")
    private val textureGun = TextureRegion(Texture("tank_gun.png"))
    /** 衝突判定用の枠 */
    var rect = Rectangle()
    /** 砲台の角度<p>
     * 三時の方向から反時計回りにラジアン */
    var angle = 0f
    /** 直近の弾を打ってからの経過時間 */
    private var noShootTime = 0
    /**
     * 所持する弾の数<p>
     * 一定時間打たないと回復する
     */
    private var bulletCnt = BULLET_CNT_MAX

    /**
     * 弾を打てるか否かを管理するための調製用の定数
     */
    private companion object {
        /** 弾の最大装填数 */
        const val BULLET_CNT_MAX = 5
        /** 弾を打った後，次打てるまでの時間 */
        const val CANT_SHOOT_TIME = 10
        /** 弾の装填にかかる時間 */
        const val RECOVER_BULLET_TIME = 30
    }

    init {
        /* 拡大・縮小時も滑らかにする. */
        textureBody.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = textureBody.width.toFloat()
        height = textureBody.height.toFloat()
    }

    override fun act(delta: Float) {
        noShootTime++
        if (noShootTime > RECOVER_BULLET_TIME) bulletCnt = BULLET_CNT_MAX

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
    fun canShoot() = (noShootTime > CANT_SHOOT_TIME && bulletCnt != 0)
}