package com.distraction.sandbox

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.distraction.sandbox.states.GSM
import com.distraction.sandbox.states.PlayState

fun AssetManager.getAtlas(str: String = "sandboxpack.atlas") = get(str, TextureAtlas::class.java)
fun log(str: String) = Gdx.app.log("tag", str)
inline fun <T> Iterable<T>.forEachIndexed(action: (index: Int, T) -> Unit): Unit {
    var index = 0
    for (item in this) action(index++, item)
}

class MainGame : ApplicationAdapter() {
    private lateinit var sb: SpriteBatch
    private lateinit var gsm: GSM

    override fun create() {
        val assets = AssetManager()
        gsm = GSM()
        val context = Context()
        context.assets = assets
        context.gsm = gsm
        assets.load("sandboxpack.atlas", TextureAtlas::class.java)
        assets.finishLoading()

        sb = SpriteBatch()
        gsm.push(PlayState(context, 1))
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gsm.update(Gdx.graphics.deltaTime)
        gsm.render(sb)
    }

    override fun dispose() {
        sb.dispose()
    }
}
