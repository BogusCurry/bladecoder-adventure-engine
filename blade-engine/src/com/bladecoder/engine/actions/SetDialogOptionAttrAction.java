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
package com.bladecoder.engine.actions;

import com.bladecoder.engine.actions.Param.Type;
import com.bladecoder.engine.model.CharacterActor;
import com.bladecoder.engine.model.Dialog;
import com.bladecoder.engine.model.DialogOption;
import com.bladecoder.engine.model.Scene;
import com.bladecoder.engine.util.EngineLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@ActionDescription("Change the selected dialog option properties")
public class SetDialogOptionAttrAction implements Action {
	@JsonProperty
	@JsonPropertyDescription("The target actor")
	@ActionPropertyType(Type.SCENE_ACTOR)
	private SceneActorRef actor;

	@JsonProperty(required = true)
	@JsonPropertyDescription("The dialog")
	@ActionPropertyType(Type.STRING)
	private String dialog;

	@JsonProperty(required = true)
	@JsonPropertyDescription("The option")
	@ActionPropertyType(Type.INTEGER)
	private int option;

	@JsonProperty
	@JsonPropertyDescription("Show/Hide the dialog option")
	@ActionPropertyType(Type.BOOLEAN)
	private Boolean visible;

	@Override
	public boolean run(ActionCallback cb) {
		final Scene s = actor.getScene();

		CharacterActor a = (CharacterActor) s.getActor(actor.getActorId(), true);

		Dialog d = a.getDialog(dialog);

		if (d == null) {
			EngineLogger.error("SetDialogOptionAttrAction: Dialog '" + dialog + "' not found");
			return false;
		}

		DialogOption o = d.getOptions().get(option);

		if (o == null) {
			EngineLogger.error("SetDialogOptionAttrAction: Option '" + option + "' not found");
			return false;
		}

		if (visible != null)
			o.setVisible(visible);

		return false;
	}

}
