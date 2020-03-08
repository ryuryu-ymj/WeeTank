package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

class Block(x: Float, y: Float) : Actor() {
    private val texture = Texture("block.png")
    /** 衝突判定用の枠 */
    var rect = Rectangle(x, y, width, height)

    init {
        /* 拡大・縮小時も滑らかにする. */
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = texture.width.toFloat()
        height = texture.height.toFloat()
        setPosition(x, y)
    }

    fun dispose() {
        texture.dispose()
    }
    
    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2)
        }
    }
}