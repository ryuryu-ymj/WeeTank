package wee.tank

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

class Player : Tank() {
    var target = Vector2()

    init {
        setPosition(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)
    }

    override fun act(delta: Float) {
        // 入力による移動
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) -> x -= moveSpeed
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) -> x += moveSpeed
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> y -= moveSpeed
            Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> y += moveSpeed
        }

        angle = atan2(target.y - y, target.x - x)

        super.act(delta)
    }
}