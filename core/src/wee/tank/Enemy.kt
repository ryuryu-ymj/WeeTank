package wee.tank

import com.badlogic.gdx.math.Vector2

abstract class Enemy(x: Float, y: Float) : Tank() {
    /** 弾を発射したいか否か */
    var wantShoot = false
        protected set

    /** 目的地 */
    var destination = Vector2()
        protected set
    var isReachDestination = true
        get() = destination.epsilonEquals(x, y)
        private set
    private var cnt = (60..180).random()

    init {
        setPosition(x, y)
        destination.set(x, y)
    }

    override fun act(delta: Float) {
        //if (wantShoot) wantShoot = false
        //if (cnt % 60 == 0) wantShoot = true

        val direction = destination.cpy().sub(x, y)
        if (direction.len() >= moveSpeed) {
            direction.setLength(moveSpeed.toFloat()).run { moveBy(x, y) }
        } else {
            destination.run { setPosition(x, y) }
        }

        super.act(delta)
    }

    /**
     * playerやbulletの位置からtargetと動きを決める
     */
    abstract fun decideTargetAndMovement(player: Player, bullets: List<Bullet>, blocks: MutableList<Block>)

    protected fun getTrueSometimes(chance: Int): Boolean {
        cnt -= chance
        println(cnt)
        if (cnt < 0) {
            cnt = (240..480).random()
            return true
        }
        return false
    }
}