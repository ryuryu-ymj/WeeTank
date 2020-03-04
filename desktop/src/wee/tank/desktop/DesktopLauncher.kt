package wee.tank.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import wee.tank.WeeTank

object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = LwjglApplicationConfiguration().apply {
            resizable = false
            width = 1280
            height = 720
        }
        LwjglApplication(WeeTank(), config)
    }
}