package wee.tank

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Bullet : MyActor() {
    companion object {
        private val texture = TextureRegion(Texture("bullet.png"))
    }

    /** 弾丸の方向 三時の方向から反時計回りにラジアン */
    var angle = 0f
        private set
    private var isReflected = false

    init {
        width = texture.regionWidth.toFloat()
        height = texture.regionHeight.toFloat()
    }

    override fun dispose() {
        clear()
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

        rect.set(x - width / 2 + 5, y - height / 2 + 5, width - 10, height - 10)
        //print("($x, $y)")
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
        rect.set(x - width / 2 + 5, y - height / 2 + 5, width - 10, height - 10)
        this.angle = angle
        isReflected = false
    }

    fun reflect(block: Block) {
        //TODO bulletがblockとblockの間にあたったときにめり込む問題の修正
        if (isReflected) {
            remove()
            isReflected = false
        }
        val a = ((y - block.y) / (x - block.x))
        val excess = 5
        if (a > -1 && a < 1) {
            x = if (x - block.x > 0) {
                block.x + block.width / 2 + excess // conflict with the right of the block
            } else {
                block.x - block.width / 2 - excess // left
            }
            angle = PI.toFloat() - angle
        } else {
            y = if (y - block.y > 0) {
                block.y + block.height / 2 + excess // top
            } else {
                block.y - block.height / 2 - excess // bottom
            }
            angle = -angle
        }
        //if (angle > PI * 2) angle -= PI.toFloat() * 2
        /*val a = ((y - block.y) / (x - block.x))
        val excess = 5
        val position: Int
        if (angle >= 0 && angle < PI / 2) {
            position = if (a > -1 && a < 1) {
                2 // left
            } else {
                3 // bottom
            }
        } else if (angle >= PI / 2 && angle < PI) {
            position = if (a > -1 && a < 1) {
                0 // right
            } else {
                3 // bottom
            }
        } else if (angle >= PI && angle < PI * 1.5) {
            position = if (a > -1 && a < 1) {
                0 // right
            } else {
                1 // top
            }
        } else {
            position = if (a > -1 && a < 1) {
                2 // left
            } else {
                1 // top
            }
        }
        when (position) {
            0 -> {
                x = block.x + block.width / 2 + excess
                angle = PI.toFloat() - angle
                println("right")
            }
            1 -> {
                y = block.y + block.height / 2 + excess
                angle = -angle
                println("top")
            }
            2 -> {
                x = block.x - block.width / 2 - excess
                angle = PI.toFloat() - angle
                println("left")
            }
            3 -> {
                y = block.y - block.height / 2 - excess
                angle = -angle
                println("bottom")
            }
        }*/
        rect.set(x - width / 2 + 5, y - height / 2 + 5, width - 10, height - 10)
        isReflected = true
    }
}