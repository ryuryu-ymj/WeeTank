package wee.tank

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.atan2

class Player : Actor() {
    private val textureBody = Texture("tank_body.png")
    private val textureGun = TextureRegion(Texture("tank_gun.png"))
    /** 衝突判定用の枠 */
    var rect = Rectangle()

    lateinit var touchPoint: Vector2
    /** 砲台の角度<p>
     * 三時の方向から反時計回りにラジアン */
    var angle = 0f

    init {
        /* 拡大・縮小時も滑らかにする. */
        textureBody.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = textureBody.width.toFloat()
        height = textureBody.height.toFloat()

        setPosition(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)
    }

    override fun act(delta: Float) {
        // 入力による移動
        val speed = 3
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) -> x -= speed
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) -> x += speed
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> y -= speed
            Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> y += speed
        }

        angle = atan2(touchPoint.y - y, touchPoint.x - x)

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
}