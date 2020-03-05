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

class Player : Tank() {
    lateinit var touchPoint: Vector2

    init {
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

        super.act(delta)
    }
}