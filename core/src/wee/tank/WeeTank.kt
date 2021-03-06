package wee.tank

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.FitViewport
import kotlin.math.cos
import kotlin.math.sin

const val STAGE_WIDTH = 3520f
const val STAGE_HEIGHT = 1980f

class WeeTank : ApplicationAdapter() {
    private lateinit var stage: Stage
    private lateinit var batch: SpriteBatch

    private lateinit var font: BitmapFont

    private var touchPoint = Vector2()

    private var score = 0

    private lateinit var player: Player
    private lateinit var target: Target
    private val enemies = mutableListOf<Enemy>()
    private lateinit var bullets: List<Bullet>
    private val blocks = mutableListOf<Block>()
    private val bulletsGroup = Group()

    override fun create() {
        stage = Stage(FitViewport(STAGE_WIDTH, STAGE_HEIGHT))
        batch = SpriteBatch()
        Gdx.input.inputProcessor = stage
        batch.projectionMatrix = stage.camera.combined

        player = Player()
        //stage.addActor(player)
        /*enemies = arrayOf(/*EnemyType1(100f, 100f), EnemyType2(STAGE_WIDTH / 2, STAGE_HEIGHT / 2)*/)
        enemies.forEach {
            stage.addActor(it)
        }*/
        bullets = List(50) { Bullet() }
        stage.addActor(bulletsGroup)
        /*blocks = arrayOf(Block(100f, 100f), Block(100f, 200f), Block(100f, 300f), Block(200f, 100f))
        blocks.forEach {
            stage.addActor(it)
        }*/
        readStage(3)
        target = Target()
        stage.addActor(target)

        createFont()

        player.target = touchPoint
    }

    override fun render() {
        draw()
        update()
    }

    private fun draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        font.draw(batch, "点数: $score", 20f, STAGE_HEIGHT - 20)
        batch.end()

        stage.draw()
    }

    private fun update() {
        touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        stage.screenToStageCoordinates(touchPoint)

        stage.act()

        target.setPosition(touchPoint.x, touchPoint.y)

        //playerが弾を打つ
        if (Gdx.input.justTouched() && player.hasParent() && player.canShoot()) {
            player.run {
                newBullet(x, y, angle)
                shoot()
            }
        }

        //enemyが動く
        enemies.forEach {
            if (it.hasParent()) {
                it.decideTargetAndMovement(player, bullets, blocks)
                if (it.wantShoot && it.canShoot()) it.run {
                    newBullet(x, y, angle)
                    shoot()
                }
            }
        }

        //衝突判定
        bullets.forEach foreach@{ bullet ->
            if (bullet.hasParent()) {
                enemies.find { it.hasParent() && bullet.rect.overlaps(it.rect) }?.let {
                    bullet.remove()
                    it.remove()
                    return@foreach
                }
                if (player.hasParent() && player.rect.overlaps(bullet.rect)) {
                    bullet.remove()
                    player.remove()
                    println("player die")
                    return@foreach
                }
                bullets.find {
                    it.hasParent() && it != bullet && bullet.rect.overlaps(it.rect)
                }?.let {
                    bullet.remove()
                    it.remove()
                    return@foreach
                    //println("bullets collide with each other")
                }
                blocks.find {
                    bullet.rect.overlaps(it.rect)
                }?.let {
                    bullet.reflect(it)
                    return@foreach
                }
            }
        }

        blocks.forEach { block ->
            if (player.rect.overlaps(block.rect)) {
                player.touchActor(block)
            }
            enemies.find {
                it.hasParent() && it.rect.overlaps(block.rect)
            }?.touchActor(block)
        }

        enemies.forEach { enemy ->
            if (enemy.hasParent()) {
                if (player.hasParent() && player.rect.overlaps(enemy.rect)) {
                    player.touchActor(enemy)
                    enemy.touchActor(player)
                }
                enemies.find {
                    it.hasParent() && it != enemy && it.rect.overlaps(enemy.rect)
                }?.let {
                    it.touchActor(enemy)
                    enemy.touchActor(it)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun dispose() {
        player.dispose()
        enemies.forEach { it.dispose() }
        bullets.forEach { it.dispose() }
        blocks.forEach { it.dispose() }
        stage.dispose()
        batch.dispose()
        font.dispose()
    }

    private fun createFont() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font.ttf"))
        FreeTypeFontGenerator.FreeTypeFontParameter().run {
            size = 48
            color = Color.CYAN
            incremental = true
            magFilter = Texture.TextureFilter.Linear
            minFilter = Texture.TextureFilter.Linear
            borderWidth = 2f
            borderColor = Color.DARK_GRAY
            shadowColor = Color.BROWN
            shadowOffsetX = 7
            shadowOffsetY = 7
            font = generator.generateFont(this)
        }
    }

    /**
     * 弾丸の発射
     * @param x: 弾を打つタンクの中心座標
     * @param y: 弾を打つタンクの中心座標
     * @param angle: 弾の方向　三時の方向から反時計回りにラジアン
     */
    private fun newBullet(x: Float, y: Float, angle: Float) {
        val bullet = bullets.find { !it.hasParent() }
        if (bullet == null) {
            println("弾丸の数が足りず，生成できません")
            return
        }
        bullet.activate(x + 70 * cos(angle), y + 70 * sin(angle), angle)
        bulletsGroup.addActor(bullet)
        //println("shoot ($x, $y)")
    }

    private fun readStage(stageNum: Int) {
        try {
            val file = Gdx.files.internal("stage${"%02d".format(stageNum)}.txt")
            for ((iy, line) in file.reader().readLines().withIndex()) {
                for ((ix, cell) in line.split(",").withIndex()) {
                    val x = STAGE_WIDTH / 2 - 1100 + ix * 100f
                    val y = STAGE_HEIGHT / 2 + 900 - iy * 100f
                    when (cell) {
                        "b" -> Block(x, y).let {
                            blocks.add(it)
                            stage.addActor(it)
                        }
                        "0" -> EnemyType1(x, y).let {
                            enemies.add(it)
                            stage.addActor(it)
                        }
                        "1" -> EnemyType2(x, y).let {
                            enemies.add(it)
                            stage.addActor(it)
                        }
                        "p" -> player.let {
                            it.init(x, y)
                            stage.addActor(it)
                        }
                    }
                }
            }
        } catch (e: GdxRuntimeException) {
            println("ファイルの読み取りに失敗しました ${e.message}")
        }
    }
}