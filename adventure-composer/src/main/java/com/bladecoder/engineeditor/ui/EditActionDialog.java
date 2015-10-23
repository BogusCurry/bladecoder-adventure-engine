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
package com.bladecoder.engineeditor.ui;

import java.util.Arrays;

import org.w3c.dom.Element;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.bladecoder.engine.actions.Action;
import com.bladecoder.engine.actions.ActionFactory;
import com.bladecoder.engine.actions.Param;
import com.bladecoder.engine.model.Dialog;
import com.bladecoder.engine.model.DialogOption;
import com.bladecoder.engine.model.Verb;
import com.bladecoder.engine.util.ActionUtils;
import com.bladecoder.engineeditor.Ctx;
import com.bladecoder.engineeditor.model.BaseDocument;
import com.bladecoder.engineeditor.ui.components.EditModelDialog;
import com.bladecoder.engineeditor.ui.components.InputPanel;
import com.bladecoder.engineeditor.ui.components.InputPanelFactory;
import com.bladecoder.engineeditor.utils.I18NUtils;

public class EditActionDialog extends EditModelDialog<Verb, Action> {
	private static final String CUSTOM_ACTION_STR = "CUSTOM ACTION";

	private static final String CUSTOM_INFO = "Custom action definition";

	private InputPanel actionPanel;
	private InputPanel classPanel;

	private InputPanel parameters[] = new InputPanel[0];

	@SuppressWarnings("unchecked")
	public EditActionDialog(Skin skin, Verb parent, Action e) {
		super(skin);

		String[] actions = ActionFactory.getActionList();
		Arrays.sort(actions);
		String[] actions2 = new String[actions.length + 1];
		System.arraycopy(actions, 0, actions2, 0, actions.length);
		actions2[actions2.length - 1] = CUSTOM_ACTION_STR;

		actionPanel = InputPanelFactory
				.createInputPanel(skin, "Action", "Select the action to create.", actions2, true);

		classPanel = InputPanelFactory.createInputPanel(skin, "Class", "Select the class for the custom action.", true);

		((SelectBox<String>) actionPanel.getField()).addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setAction();
			}
		});

		((TextField) classPanel.getField()).addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (!event.isFocused())
					setAction();
			}
		});

//		if (e != null) {
//			classPanel.setText(e.getAttribute("class"));
//
//			if (!e.getAttribute("action_name").isEmpty()) {
//				actionPanel.setText(e.getAttribute("action_name"));
//			}
//
//			if (!e.getAttribute("class").isEmpty()) {
//				actionPanel.setText(CUSTOM_ACTION_STR);
//			}
//
//		}

		init(parent, e, parameters);

		setAction();

//		if (e != null) {
//			for (int pos = 0; pos < a.length; pos++) {
//				InputPanel input = i[pos];
//				if (I18NUtils.mustTraslateAttr(a[pos])) {
//					input.setText(doc.getTranslation(e.getAttribute(a[pos])));
//				} else {
//					input.setText(e.getAttribute(a[pos]));
//				}
//			}
//		}
	}

	private String[] getAttrs() {
		String inputs[] = new String[parameters.length];

		for (int j = 0; j < parameters.length; j++) {
			InputPanel i = parameters[j];
			inputs[j] = i.getTitle();
		}

		return inputs;
	}

	private void setAction() {
		String id = actionPanel.getText();

		getCenterPanel().clear();
		addInputPanel(actionPanel);

		Action ac = null;

		if (id.equals(CUSTOM_ACTION_STR)) {
			addInputPanel(classPanel);
			if (!classPanel.getText().trim().isEmpty())
				ac = ActionFactory.createByClass(classPanel.getText(), null);
		} else {
			ac = ActionFactory.create(id, null);
		}

		if (ac != null) {
			setInfo(ActionUtils.getInfo(ac));

			Param[] params = ActionUtils.getParams(ac);

			parameters = new InputPanel[params.length];

			for (int i = 0; i < params.length; i++) {
				if (params[i].options instanceof Enum[]) {
					parameters[i] = InputPanelFactory.createInputPanel(getSkin(), params[i].name, params[i].desc,
							params[i].type, params[i].mandatory, params[i].defaultValue, (Enum[]) params[i].options);
				} else {
					parameters[i] = InputPanelFactory.createInputPanel(getSkin(), params[i].name, params[i].desc,
							params[i].type, params[i].mandatory, params[i].defaultValue, (String[]) params[i].options);
				}

				addInputPanel(parameters[i]);

				if ((parameters[i].getField() instanceof TextField && params[i].name.toLowerCase().endsWith("text")) ||
						parameters[i].getField() instanceof ScrollPane) {
					parameters[i].getCell(parameters[i].getField()).fillX();
				}
			}

//			i = parameters;
//			a = getAttrs();
		} else {
			setInfo(CUSTOM_INFO);
//			i = new InputPanel[0];
//			a = new String[0];
		}

		// ((ScrollPane)(getContentTable().getCells().get(1).getActor())).setWidget(getCenterPanel());
	}
	
	@Override
	protected void inputsToModel(boolean create) {
		
//		if(create) {
//			e = new DialogOption();
//		}
//		
//		e.setText(text.getText());
//		e.setResponseText(responseText.getText());
//		e.setVerbId(verb.getText());
//		e.setNext(next.getText());
//		e.setVisible(Boolean.parseBoolean(visible.getText()));
//		e.setOnce(Boolean.parseBoolean(once.getText()));
//		
//		if(create) {
//			parent.addOption(e);
//		}
//
//		// TODO UNDO OP
////		UndoOp undoOp = new UndoAddElement(doc, e);
////		Ctx.project.getUndoStack().add(undoOp);
//		
//		Ctx.project.getSelectedChapter().setModified(e);
//		
//		
//		// Remove previous params
//		while (e.getAttributes().getLength() > 0) {
//			e.removeAttribute(e.getAttributes().item(0).getNodeName());
//		}
//
//		String id = actionPanel.getText();
//
//		if (id.equals(CUSTOM_ACTION_STR)) {
//			e.setAttribute("class", classPanel.getText());
//		} else {
//			e.setAttribute("action_name", id);
//		}
//
//		super.fill();
	}

	@Override
	protected void modelToInputs() {
//		text.setText(e.getText());
//		responseText.setText(e.getResponseText());
//		verb.setText(e.getVerbId());
//		next.setText(e.getNext());
//		
//		visible.setText(Boolean.toString(e.isVisible()));
//		once.setText(Boolean.toString(e.isOnce()));
	}		
}
