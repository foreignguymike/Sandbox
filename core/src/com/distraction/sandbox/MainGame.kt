package com.distraction.sandbox

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.distraction.sandbox.states.GSM
import com.distraction.sandbox.states.TitleState

class MainGame : ApplicationAdapter() {
    private lateinit var sb: SpriteBatch
    private lateinit var gsm: GSM

    override fun create() {
        val assets = AssetManager()
        gsm = GSM()
        val context = Context()
        context.assets = assets
        context.gsm = gsm

        val resolver = InternalFileHandleResolver()
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        assets.load("sandboxpack.atlas", TextureAtlas::class.java)

        assets.finishLoading()

        sb = SpriteBatch()
        gsm.push(TitleState(context))
    }

    override fun render() {
        clearScreen()

        gsm.update(Gdx.graphics.deltaTime)
        gsm.render(sb)
    }

    override fun dispose() {
        sb.dispose()
    }
}
