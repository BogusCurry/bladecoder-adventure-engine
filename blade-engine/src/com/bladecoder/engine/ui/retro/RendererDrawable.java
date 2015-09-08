package com.bladecoder.engine.ui.retro;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.bladecoder.engine.model.ActorRenderer;

public class RendererDrawable extends BaseDrawable {
	
	private ActorRenderer renderer;

	public void setRenderer(ActorRenderer r) {
		renderer = r;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		float scale;
		
		if(renderer == null)
			return;
		
		if(renderer.getWidth() > renderer.getHeight())
			scale = width / renderer.getWidth();
		else
			scale = height / renderer.getHeight();
				
		renderer.draw((SpriteBatch) batch, x,
				y - renderer.getHeight() / 2, 1);		
	}
}
