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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.jcip.annotations.Immutable;

import java.util.Arrays;

@Immutable
public class Param {
	public enum Type {
		STRING, BOOLEAN, FLOAT, INTEGER, VECTOR2, VECTOR3, DIMENSION, ACTOR, SCENE, CHAPTER, FILE, OPTION, SCENE_ACTOR, ACTOR_ANIMATION, LAYER, EDITABLE_OPTION,
		TEXT, SMALL_TEXT, BIG_TEXT, COLOR, SOUND, FONT,
		FILE_SOUND, FILE_MUSIC, FILE_ATLAS, FILE_S3D, FILE_SPINE, FILE_IMAGE
	}
	
	public static final String NUMBER_PARAM_SEPARATOR = ",";
	public static final String STRING_PARAM_SEPARATOR = "#";

	private final String id;
	private final String name;
	private final String desc;
	private final Type type;
	private final boolean mandatory;
	private final String defaultValue;
	private final Object[] options; // available values for combos
	private final String link;

	public Param(String id, String name, String desc, Type type, boolean mandatory, String defaultValue, String[] options, String link) {
		this(id, name, desc, type, mandatory, defaultValue, (Object[])options, link);
	}

	public Param(String id, String name, String desc, Type type, boolean mandatory, String defaultValue, Enum[] options, String link) {
		this(id, name, desc, type, mandatory, defaultValue, (Object[])options, link);
	}

	public Param(String id, String name, String desc, Type type, boolean mandatory, String defaultValue, Object[] options, String link) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.mandatory = mandatory;
		this.defaultValue = defaultValue;
		this.options = options;
		this.link = link;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Type getType() {
		return type;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Object[] getOptions() {
		return options;
	}

	public String getLink() {
		return link;
	}

	public static Vector2 parseVector2(String s) {
		
		if(s==null)
			return null;
		
		Vector2 v = null;

		int idx = s.indexOf(NUMBER_PARAM_SEPARATOR.charAt(0));

		if (idx != -1) {
			try {
				float x = Float.parseFloat(s.substring(0,idx));
				float y = Float.parseFloat(s.substring(idx + 1));

				v = new Vector2(x, y);
			} catch (Exception e) {

			}
		}

		return v;
	}

	public static Vector3 parseVector3(String s) {
		Vector3 v = null;

		int idx = s.indexOf(NUMBER_PARAM_SEPARATOR.charAt(0));
		int idx2 = s.lastIndexOf(NUMBER_PARAM_SEPARATOR.charAt(0));

		if (idx != -1 && idx2 != -1 && idx != idx2) {
			try {
				float x = Float.parseFloat(s.substring(0,idx));
				float y = Float.parseFloat(s.substring(idx + 1, idx2));
				float z = Float.parseFloat(s.substring(idx2 + 1));

				v = new Vector3(x, y, z);
			} catch (Exception e) {

			}
		}

		return v;
	}
	
	public static void parsePolygon(Polygon p, String s) {
		
		String[] vs = s.split(NUMBER_PARAM_SEPARATOR);
		
		if(vs.length < 6)
			return;
		
		float verts[] = new float[vs.length];
		
		for(int i = 0; i < vs.length; i++) {
			verts[i] = Float.parseFloat(vs[i]);
		}
		
		p.setVertices(verts);

	}
	
	public static void parsePolygon(Polygon p, String v, String pos) {
		parsePolygon(p, v);
		Vector2 v2 = parseVector2(pos);
		p.setPosition(v2.x, v2.y);
	}
	
	public static String toStringParam(Polygon p) {
		StringBuilder sb = new StringBuilder();
		float[]verts = p.getVertices();
		
		sb.append(verts[0]);
		
		for(int i = 1; i < verts.length; i++) {
			sb.append(NUMBER_PARAM_SEPARATOR);
			sb.append(verts[i]);	
		}
		
		return sb.toString();
	}
	
	public static String[] parseString2(String s) {
		
		if(s==null)
			return null;
		
		String[] v = new String[2];

		int idx = s.indexOf(STRING_PARAM_SEPARATOR.charAt(0));
		
		if (idx != -1) {
			v[0] = s.substring(0,idx);
			v[1] = s.substring(idx + 1); 
		} else {
			v[1] = s;
		}

		return v;
	}

	public static Color parseColor(String color) {
		if (color == null || color.trim().isEmpty()) {
			return Color.BLACK;
		}

		switch (color.trim()) {
			case "black":
				return new Color(0, 0, 0, 1);
			case "white":
				return new Color(1, 1, 1, 1);
			default:
				return Color.valueOf(color);
		}
	}

	public static String toStringParam(Vector2 v) {
		return v.x + NUMBER_PARAM_SEPARATOR + v.y;
	}

	public static String toStringParam(Vector3 v) {
		return v.x + NUMBER_PARAM_SEPARATOR + v.y + NUMBER_PARAM_SEPARATOR + v.z;
	}
	
	public static String toStringParam(String s1, String s2) {
		if( s1==null || s1.isEmpty())
			return s2;
		
		return s1 + STRING_PARAM_SEPARATOR + s2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Param param = (Param) o;

		if (mandatory != param.mandatory) return false;
		if (id != null ? !id.equals(param.id) : param.id != null) return false;
		if (name != null ? !name.equals(param.name) : param.name != null) return false;
		if (desc != null ? !desc.equals(param.desc) : param.desc != null) return false;
		if (type != param.type) return false;
		if (defaultValue != null ? !defaultValue.equals(param.defaultValue) : param.defaultValue != null) return false;
		if (!Arrays.equals(options, param.options)) return false;
		return !(link != null ? !link.equals(param.link) : param.link != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (desc != null ? desc.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (mandatory ? 1 : 0);
		result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
		result = 31 * result + (options != null ? Arrays.hashCode(options) : 0);
		result = 31 * result + (link != null ? link.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Param{" +
				"name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", type=" + type +
				", mandatory=" + mandatory +
				", defaultValue='" + defaultValue + '\'' +
				", options=" + Arrays.toString(options) +
				'}';
	}
}
