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
package com.bladecoder.engineeditor.scneditor;

import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bladecoder.engineeditor.Ctx;
import com.bladecoder.engineeditor.utils.RunProccess;

public class ScnEditor extends Table {
	ScnWidget scnWidget;
	CheckBox inSceneCb;
	CheckBox animCb;
	TextButton testButton;
	TextButton walkZoneButton;

	public ScnEditor(Skin skin) {
		super(skin);

		scnWidget = new ScnWidget(skin);

		inSceneCb = new CheckBox("In Scene Anims", skin);
		inSceneCb.setChecked(false);
		animCb = new CheckBox("Animation", skin);
		animCb.setChecked(true);
		testButton = new TextButton("Test", skin);
		walkZoneButton = new TextButton("Walk Zone", skin);

		add(scnWidget).expand().fill();
		row();

		Table bottomTable = new Table(skin);
		bottomTable.left();
		// bottomTable.setBackground("background");
		add(bottomTable).fill();

		bottomTable.add(walkZoneButton);
		bottomTable.add(inSceneCb);
		bottomTable.add(animCb);
		bottomTable.add(testButton);

		walkZoneButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
				if (walkZoneButton.isChecked())
					getScnWidget().showEditWalkZoneWindow();
				else
					getScnWidget().hideEditWalkZoneWindow();
			}
		});

		inSceneCb.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
				scnWidget.setInSceneSprites(inSceneCb.isChecked());
			}
		});

		animCb.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
				scnWidget.setAnimation(animCb.isChecked());
			}
		});

		testButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
				test();
				event.cancel();
			}
		});
	}

	public ScnWidget getScnWidget() {
		return scnWidget;
	}

	private void test() {

		if (Ctx.project.getSelectedScene() == null) {
			String msg = "There are no scenes in this chapter.";
			Ctx.msg.show(getStage(), msg, 3);
			return;
		}

		try {
			Ctx.project.saveProject();
		} catch (Exception ex) {
			String msg = "Something went wrong while saving the project.\n\n" + ex.getClass().getSimpleName() + " - "
					+ ex.getMessage();
			Ctx.msg.show(getStage(), msg, 2);
		}

		new Thread(new Runnable() {
			Stage stage = getStage();

			@Override
			public void run() {
				Ctx.msg.show(stage, "Running scene...", 3);

				try {
					if (!RunProccess.runBladeEngine(Ctx.project.getProjectDir(), Ctx.project.getChapter()
							.getId(), Ctx.project.getSelectedScene().getId()))
						Ctx.msg.show(stage, "There was a problem running the scene", 3);
				} catch (IOException e) {
					Ctx.msg.show(stage, "There was a problem running the scene: " + e.getMessage(), 3);
				}

			}
		}).start();

	}

	public void dispose() {
		scnWidget.dispose();
	}
}
