/*******************************************************************************
 * Copyright 2014 Rafael Garcia Moreno.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bladecoder.engineeditor.ui.components;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bladecoder.engine.actions.Param;
import com.bladecoder.engine.anim.AnimationDesc;
import com.bladecoder.engine.loader.XMLConstants;
import com.bladecoder.engineeditor.Ctx;

public class ActorAnimationInputPanel extends InputPanel {
	EditableSelectBox<String> animation;
	EditableSelectBox<String> actor;
	Table panel;

	ActorAnimationInputPanel(Skin skin, String title, String desc,
			boolean mandatory, String defaultValue) {
		panel = new Table(skin);
		animation = new EditableSelectBox<>(skin);
		actor = new EditableSelectBox<>(skin);

		panel.add(new Label(" Actor ", skin));
		panel.add(actor);
		panel.add(new Label("  Animation ", skin));
		panel.add(animation);

		NodeList actors = Ctx.project.getSelectedChapter().getActors(
				Ctx.project.getSelectedChapter().getSceneById(Ctx.project.getSelectedScene().getId()));

		ArrayList<String> values = new ArrayList<String>();

		// values.add("");

		for (int i = 0; i < actors.getLength(); i++) {
			String id = ((Element) actors.item(i)).getAttribute(XMLConstants.ID_ATTR);
			String type = ((Element) actors.item(i)).getAttribute(XMLConstants.TYPE_ATTR);

			if (type.equals(XMLConstants.SPRITE_VALUE) || type.equals(XMLConstants.CHARACTER_VALUE)) {
				values.add(id);
			}
		}

		actor.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				actorSelected();
			}
		});

		init(skin, title, desc, panel, mandatory, defaultValue);
		actor.setItems(values.toArray(new String[values.size()]));

		if (values.size() > 0) {
			if (defaultValue != null)
				setText(defaultValue);
			else
				actor.setSelected("");
		}

	}

	private void actorSelected() {
		String s = actor.getSelected();
		Element a = Ctx.project.getSelectedChapter().getActor(
				Ctx.project.getSelectedChapter().getSceneById(Ctx.project.getSelectedScene().getId()), Ctx.project.getSelectedActor().getId());
		
		ArrayList<String> values = new ArrayList<String>();

		if (s != null && !s.isEmpty()) {
			a = Ctx.project.getSelectedChapter().getActor(
					Ctx.project.getSelectedChapter().getSceneById(Ctx.project.getSelectedScene().getId()), s);
		}

		if (a != null) {

			NodeList animations = Ctx.project.getSelectedChapter()
					.getAnimations(a);

			if (!isMandatory()) {
				values.add("");
			}

			for (int i = 0; i < animations.getLength(); i++) {
				values.add(((Element) animations.item(i)).getAttribute("id"));

				String flipped = AnimationDesc.getFlipId(((Element) animations
						.item(i)).getAttribute("id"));

				if (!flipped.isEmpty()) {
					values.add(flipped);
				}
			}
		}
		
		animation.setItems(values.toArray(new String[values.size()]));

		if (values.size() > 0)
			animation.setSelected("");

	}

	public String getText() {
		return Param
				.toStringParam(actor.getSelected(), animation.getSelected());
	}

	public void setText(String s) {
		String out[] = Param.parseString2(s);

		if(out[0] == null)
			out[0] = "";
			
		actor.setSelected(out[0]);
		actorSelected();
		animation.setSelected(out[1]);
	}
}
