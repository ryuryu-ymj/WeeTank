package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class Target : Actor()
{
    private val texture = Texture("target.png")

    init
    {
        /* 拡大・縮小時も滑らかにする. */
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = texture.width.toFloat()
        height = texture.height.toFloat()

        setPosition(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)
    }

    override fun draw(batch: Batch, parentAlpha: Float)
    {
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(texture, x - width / 2, y - height / 2)
    }
}