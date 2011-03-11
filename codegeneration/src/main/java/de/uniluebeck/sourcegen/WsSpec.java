/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the
 * name of the University of Luebeck nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.uniluebeck.sourcegen;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.uniluebeck.sourcegen.nature.Nature;

/**
 * 
 * @author Daniel Bimschas
 */
public class WsSpec {

	public static final String DATATYPE_BOOLEAN = "boolean";

	public static final String DATATYPE_BYTEARRAY = "bytearray";

	public static final String DATATYPE_DOUBLE = "double";

	public static final String DATATYPE_FLOAT = "float";

	public static final String DATATYPE_INT = "int";

	public static final String DATATYPE_LONG = "long";

	public static final String DATATYPE_STRING = "string";

	public static final boolean DEF_BOOLEAN = false;

	public static final byte[] DEF_BYTEARRAY = new byte[0];

	public static final double DEF_DOUBLE = Double.MIN_VALUE;

	public static final float DEF_FLOAT = Float.MIN_VALUE;

	public static final int DEF_INT = Integer.MIN_VALUE;

	public static final long DEF_LONG = Long.MIN_VALUE;

	public static final String DEF_STRING = "";

	public static final int INDEX_DATATYPE = 1;

	public static final int INDEX_ID = 0;

	public static final int INDEX_META_KEY = 3;

	public static final int INDEX_SPEC_KEY = 4;

	public static final int INDEX_SPEC_NATURE = 3;

	public static final int INDEX_SPEC_OR_META = 2;

	public static final String KEY_ACTIVE = "active";

	public static final String KEY_C_FILENAME = "c_filename";

	public static final String KEY_J_PKG_PREFIX = "java_package";

	public static final String KEY_PROJECTDIR = "project_dir";

	public static final String KEY_PROJECTSUBDIR = "project_subdir";

	public static final String META_KEY_SPEC_NAME = "specName";

	public static final String PROP_KEY_META = "meta";

	public static final String PROP_KEY_SEP_REGEXP = "\\.";

	public static final String PROP_KEY_SEPARATOR = ".";

	public static final String PROP_KEY_SPEC = "spec";

	/**
	 * Checks if there's a <code>WsSpec</code> instance within <code>map</code> with key <code>id</code>.
	 * 
	 * @param map
	 *            the map instance to check
	 * @param id
	 *            the id to check for
	 * @return <code>true</code> if <code>map</code> contains <code>id</code>, <code>false</code> otherwise.
	 */
	private static boolean containsSpec(HashMap<Long, WsSpec> map, long id) {
		for (Long l : map.keySet())
			if (l.longValue() == id)
				return true;
		return false;
	}

	/**
	 * Loads an array of <code>WsSpec</code> instances from a properties file.
	 * 
	 * @param schemaFile
	 *            the schema file to use
	 * @param propertiesFile
	 *            the property file to use
	 * @return an array of <code>WsSpec</code> instances
	 * @throws IOException
	 *             if the property file can't be read
	 * @throws Exception
	 *             if the property file contains invalid values
	 */
	public static WsSpec[] load(File schemaFile, Properties props) throws Exception {

		HashMap<Long, WsSpec> specs = new HashMap<Long, WsSpec>();

		WsSpec spec;
		String[] keyArr;
		Nature nature;
		String propKey;
		String key;
		long currId;

		// move through all key value pairs
		Enumeration<?> e = props.propertyNames();
		while (e.hasMoreElements()) {

			key = (String) e.nextElement();
			keyArr = key.split(PROP_KEY_SEP_REGEXP);
			currId = Long.parseLong(keyArr[INDEX_ID]);

			if (!containsSpec(specs, currId)) {
				spec = new WsSpec(schemaFile.toString(), currId);
				specs.put(currId, spec);
			} else {
				spec = specs.get(new Long(currId));
			}

			// read a spec info
			if (PROP_KEY_SPEC.equals(keyArr[INDEX_SPEC_OR_META])) {

				nature = Nature.valueOf(keyArr[INDEX_SPEC_NATURE]);
				if (nature == null)
					throw new Exception("Invalid nature string in properties file");
				propKey = keyArr[INDEX_SPEC_KEY];

				if (DATATYPE_STRING.equals(keyArr[INDEX_DATATYPE]))
					spec.put(nature, propKey, props.getProperty(key, DEF_STRING));

				else if (DATATYPE_BOOLEAN.equals(keyArr[INDEX_DATATYPE]))
					spec.putBoolean(nature, propKey, Boolean.parseBoolean(props.getProperty(key, "" + DEF_BOOLEAN)));

				else if (DATATYPE_INT.equals(keyArr[INDEX_DATATYPE]))
					spec.putInt(nature, propKey, Integer.parseInt(props.getProperty(key, "" + DEF_INT)));

				else if (DATATYPE_DOUBLE.equals(keyArr[INDEX_DATATYPE]))
					spec.putDouble(nature, propKey, Double.parseDouble(props.getProperty(key, "" + DEF_DOUBLE)));

				else if (DATATYPE_FLOAT.equals(keyArr[INDEX_DATATYPE]))
					spec.putFloat(nature, propKey, Float.parseFloat(props.getProperty(key, "" + DEF_FLOAT)));

				else if (DATATYPE_LONG.equals(keyArr[INDEX_DATATYPE]))
					spec.putLong(nature, propKey, Long.parseLong(props.getProperty(key, "" + DEF_LONG)));

				else if (DATATYPE_BYTEARRAY.equals(keyArr[INDEX_DATATYPE]))
					spec.putByteArray(nature, propKey, props.getProperty(key, "" + DEF_BYTEARRAY).getBytes());

			}

			// read a meta info
			else if (PROP_KEY_META.equals(keyArr[INDEX_SPEC_OR_META])) {

				propKey = keyArr[INDEX_META_KEY];

				if (DATATYPE_STRING.equals(keyArr[INDEX_DATATYPE]))
					spec.putMeta(propKey, props.getProperty(key, DEF_STRING));

				else if (DATATYPE_BOOLEAN.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaBoolean(propKey, Boolean.parseBoolean(props.getProperty(key, "" + DEF_BOOLEAN)));

				else if (DATATYPE_INT.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaInt(propKey, Integer.parseInt(props.getProperty(key, "" + DEF_INT)));

				else if (DATATYPE_DOUBLE.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaDouble(propKey, Double.parseDouble(props.getProperty(key, "" + DEF_DOUBLE)));

				else if (DATATYPE_FLOAT.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaFloat(propKey, Float.parseFloat(props.getProperty(key, "" + DEF_FLOAT)));

				else if (DATATYPE_LONG.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaLong(propKey, Long.parseLong(props.getProperty(key, "" + DEF_LONG)));

				else if (DATATYPE_BYTEARRAY.equals(keyArr[INDEX_DATATYPE]))
					spec.putMetaByteArray(propKey, props.getProperty(key, "" + DEF_BYTEARRAY).getBytes());

			}
		}

		return specs.values().toArray(new WsSpec[specs.size()]);
	}

	/**
	 * Parses the id from the given key.
	 * 
	 * @param key
	 *            the key string to parse
	 * @return the id or <code>Long.MIN_VALUE</code> on parsing error
	 */
	public static long parseIdFromKey(String key) {
		try {
			return Long.parseLong(key.split(PROP_KEY_SEP_REGEXP)[INDEX_ID]);
		} catch (NumberFormatException e) {
			return Long.MIN_VALUE;
		}
	}

	private long id;

	private HashMap<String, Boolean> metaOptionsBoolean = new HashMap<String, Boolean>();

	private HashMap<String, byte[]> metaOptionsByteArray = new HashMap<String, byte[]>();

	private HashMap<String, Double> metaOptionsDouble = new HashMap<String, Double>();

	private HashMap<String, Float> metaOptionsFloat = new HashMap<String, Float>();

	private HashMap<String, Integer> metaOptionsInt = new HashMap<String, Integer>();

	private HashMap<String, Long> metaOptionsLong = new HashMap<String, Long>();

	private HashMap<String, String> metaOptionsString = new HashMap<String, String>();

	private HashMap<Nature, HashMap<String, Boolean>> optionsBoolean = new HashMap<Nature, HashMap<String, Boolean>>();

	private HashMap<Nature, HashMap<String, byte[]>> optionsByteArray = new HashMap<Nature, HashMap<String, byte[]>>();

	private HashMap<Nature, HashMap<String, Double>> optionsDouble = new HashMap<Nature, HashMap<String, Double>>();

	private HashMap<Nature, HashMap<String, Float>> optionsFloat = new HashMap<Nature, HashMap<String, Float>>();

	private HashMap<Nature, HashMap<String, Integer>> optionsInt = new HashMap<Nature, HashMap<String, Integer>>();

	private HashMap<Nature, HashMap<String, Long>> optionsLong = new HashMap<Nature, HashMap<String, Long>>();

	private HashMap<Nature, HashMap<String, String>> optionsString = new HashMap<Nature, HashMap<String, String>>();

	private String schema;

	public WsSpec(String schema) {
		this(schema, System.currentTimeMillis());
	}

	public WsSpec(String schema, long id) {
		this.id = id;
		this.schema = schema;
	}

	/**
	 * Copy Constructor.
	 * 
	 * @param spec
	 */
	public WsSpec(WsSpec spec) {
		this.id = spec.id;
		this.optionsBoolean = new HashMap<Nature, HashMap<String, Boolean>>(spec.optionsBoolean);
		this.optionsByteArray = new HashMap<Nature, HashMap<String, byte[]>>(spec.optionsByteArray);
		this.optionsDouble = new HashMap<Nature, HashMap<String, Double>>(spec.optionsDouble);
		this.optionsFloat = new HashMap<Nature, HashMap<String, Float>>(spec.optionsFloat);
		this.optionsInt = new HashMap<Nature, HashMap<String, Integer>>(spec.optionsInt);
		this.optionsLong = new HashMap<Nature, HashMap<String, Long>>(spec.optionsLong);
		this.optionsString = new HashMap<Nature, HashMap<String, String>>(spec.optionsString);
		this.schema = new String(spec.schema);
		this.metaOptionsBoolean = new HashMap<String, Boolean>(spec.metaOptionsBoolean);
		this.metaOptionsByteArray = new HashMap<String, byte[]>(spec.metaOptionsByteArray);
		this.metaOptionsDouble = new HashMap<String, Double>(spec.metaOptionsDouble);
		this.metaOptionsFloat = new HashMap<String, Float>(spec.metaOptionsFloat);
		this.metaOptionsInt = new HashMap<String, Integer>(spec.metaOptionsInt);
		this.metaOptionsLong = new HashMap<String, Long>(spec.metaOptionsLong);
		this.metaOptionsString = new HashMap<String, String>(spec.metaOptionsString);
	}

	public String get(Nature nature, String key, String def) {
		if (optionsString.get(nature) == null)
			return def;
		String value = optionsString.get(nature).get(key);
		return value != null ? value : def;
	}

	public boolean getBoolean(Nature nature, String key, boolean def) {
		if (optionsBoolean.get(nature) == null)
			return def;
		Boolean value = optionsBoolean.get(nature).get(key);
		return value != null ? value.booleanValue() : def;
	}

	public byte[] getByteArray(Nature nature, String key, byte[] def) {
		if (optionsByteArray.get(nature) == null)
			return def;
		byte[] value = optionsByteArray.get(nature).get(key);
		return value != null ? value : def;
	}

	public double getDouble(Nature nature, String key, double def) {
		if (optionsDouble.get(nature) == null)
			return def;
		Double value = optionsDouble.get(nature).get(key);
		return value != null ? value.doubleValue() : def;
	}

	public float getFloat(Nature nature, String key, float def) {
		if (optionsFloat.get(nature) == null)
			return def;
		Float value = optionsFloat.get(nature).get(key);
		return value != null ? value.floatValue() : def;
	}

	public long getId() {
		return id;
	}

	public int getInt(Nature nature, String key, int def) {
		if (optionsInt.get(nature) == null)
			return def;
		Integer value = optionsInt.get(nature).get(key);
		return value != null ? value.intValue() : def;
	}

	protected String getKey(String dataType, Nature nature, String key) {
		return id + PROP_KEY_SEPARATOR + dataType + PROP_KEY_SEPARATOR + PROP_KEY_SPEC + PROP_KEY_SEPARATOR
				+ nature.toString() + PROP_KEY_SEPARATOR + key;
	}

	private String getMetaKey(String dataType, String key) {
		return id + PROP_KEY_SEPARATOR + dataType + PROP_KEY_SEPARATOR + PROP_KEY_META + PROP_KEY_SEPARATOR + key;
	}

	public long getLong(Nature nature, String key, long def) {
		if (optionsLong.get(nature) == null)
			return def;
		Long value = optionsLong.get(nature).get(key);
		return value != null ? value.longValue() : def;
	}

	public String getMeta(String key, String def) {
		return this.metaOptionsString.get(key) != null ? this.metaOptionsString.get(key) : def;
	}

	public boolean getMetaBoolean(String key, boolean def) {
		return this.metaOptionsBoolean.get(key) != null ? this.metaOptionsBoolean.get(key).booleanValue() : def;
	}

	public byte[] getMetaByteArray(String key, byte[] def) {
		return this.metaOptionsByteArray.get(key) != null ? this.metaOptionsByteArray.get(key) : def;
	}

	public double getMetaDouble(String key, double def) {
		return this.metaOptionsDouble.get(key) != null ? this.metaOptionsDouble.get(key).doubleValue() : def;
	}

	public float getMetaFloat(String key, float def) {
		return this.metaOptionsFloat.get(key) != null ? this.metaOptionsFloat.get(key).floatValue() : def;
	}

	public int getMetaInt(String key, int def) {
		return this.metaOptionsInt.get(key) != null ? this.metaOptionsInt.get(key).intValue() : def;
	}

	public long getMetaLong(String key, long def) {
		return this.metaOptionsLong.get(key) != null ? this.metaOptionsLong.get(key).longValue() : def;
	}

	public HashMap<String, Boolean> getMetaOptionsBoolean() {
		return this.metaOptionsBoolean;
	}

	public HashMap<String, byte[]> getMetaOptionsByteArray() {
		return this.metaOptionsByteArray;
	}

	public HashMap<String, Double> getMetaOptionsDouble() {
		return this.metaOptionsDouble;
	}

	public HashMap<String, Float> getMetaOptionsFloat() {
		return this.metaOptionsFloat;
	}

	public HashMap<String, Integer> getMetaOptionsInt() {
		return this.metaOptionsInt;
	}

	public HashMap<String, Long> getMetaOptionsLong() {
		return this.metaOptionsLong;
	}

	public HashMap<String, String> getMetaOptionsString() {
		return this.metaOptionsString;
	}

	public HashMap<Nature, HashMap<String, Boolean>> getOptionsBoolean() {
		return optionsBoolean;
	}

	public HashMap<Nature, HashMap<String, byte[]>> getOptionsByteArray() {
		return optionsByteArray;
	}

	public HashMap<Nature, HashMap<String, Double>> getOptionsDouble() {
		return optionsDouble;
	}

	public HashMap<Nature, HashMap<String, Float>> getOptionsFloat() {
		return optionsFloat;
	}

	public HashMap<Nature, HashMap<String, Integer>> getOptionsInt() {
		return optionsInt;
	}

	public HashMap<Nature, HashMap<String, Long>> getOptionsLong() {
		return optionsLong;
	}

	public HashMap<Nature, HashMap<String, String>> getOptionsString() {
		return optionsString;
	}

	public String getSchema() {
		return schema;
	}

	/**
	 * Returns a list of the natures used in this specification.
	 * 
	 * @return a list of natures
	 */
	public Set<Nature> getUsedNatures() {
		HashSet<Nature> natures = new HashSet<Nature>();
		for (Nature n : optionsBoolean.keySet()) {
			if (optionsBoolean.get(n).get(KEY_ACTIVE).booleanValue())
				natures.add(n);
		}
		return natures;
	}

	public void put(Nature nature, String key, String value) {
		if (optionsString.get(nature) == null)
			optionsString.put(nature, new HashMap<String, String>());
		optionsString.get(nature).put(key, value);
	}

	public void putBoolean(Nature nature, String key, boolean value) {
		if (optionsBoolean.get(nature) == null)
			optionsBoolean.put(nature, new HashMap<String, Boolean>());
		optionsBoolean.get(nature).put(key, value);
	}

	public void putByteArray(Nature nature, String key, byte[] value) {
		if (optionsByteArray.get(nature) == null)
			optionsByteArray.put(nature, new HashMap<String, byte[]>());
		optionsByteArray.get(nature).put(key, value);
	}

	public void putDouble(Nature nature, String key, double value) {
		if (optionsDouble.get(nature) == null)
			optionsDouble.put(nature, new HashMap<String, Double>());
		optionsDouble.get(nature).put(key, value);
	}

	public void putFloat(Nature nature, String key, float value) {
		if (optionsFloat.get(nature) == null)
			optionsFloat.put(nature, new HashMap<String, Float>());
		optionsFloat.get(nature).put(key, value);
	}

	public void putInt(Nature nature, String key, int value) {
		if (optionsInt.get(nature) == null)
			optionsInt.put(nature, new HashMap<String, Integer>());
		optionsInt.get(nature).put(key, value);
	}

	public void putLong(Nature nature, String key, long value) {
		if (optionsLong.get(nature) == null)
			optionsLong.put(nature, new HashMap<String, Long>());
		optionsLong.get(nature).put(key, value);
	}

	public void putMeta(String key, String value) {
		this.metaOptionsString.put(key, value);
	}

	public void putMetaBoolean(String key, boolean value) {
		this.metaOptionsBoolean.put(key, new Boolean(value));
	}

	public void putMetaByteArray(String key, byte[] value) {
		this.metaOptionsByteArray.put(key, value);
	}

	public void putMetaDouble(String key, double value) {
		this.metaOptionsDouble.put(key, new Double(value));
	}

	public void putMetaFloat(String key, float value) {
		this.metaOptionsFloat.put(key, new Float(value));
	}

	public void putMetaInt(String key, int value) {
		this.metaOptionsInt.put(key, new Integer(value));
	}

	public void putMetaLong(String key, long value) {
		this.metaOptionsLong.put(key, new Long(value));
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * Returns a <code>Properties</code> instance containing all information of this <code>WsSpec</code> instance.<br/>
	 * <br/>
	 * The key, value format in the <code>Properties</code> instance is defined as follows:<br/>
	 * <br/>
	 * specId.datatype.'spec'.nature.key = value<br/>
	 * specId.datatype.'meta'.key = value<br/>
	 * <br/>
	 * Example:<br/>
	 * 12345.string.spec.language_c.outpath = c:\ 12345.string.meta.specName = George's Java Serializer
	 * 
	 * @return a <code>Properties</code> instance
	 */
	public Properties toProperties() {

		Properties props = new Properties();

		/*** SPEC PROPERTIES ***/

		for (Nature nature : optionsBoolean.keySet())
			for (String key : optionsBoolean.get(nature).keySet())
				props.put(getKey(DATATYPE_BOOLEAN, nature, key), optionsBoolean.get(nature).get(key).toString());

		for (Nature nature : optionsByteArray.keySet())
			for (String key : optionsByteArray.get(nature).keySet())
				props.put(getKey(DATATYPE_BYTEARRAY, nature, key),
						String.valueOf(optionsByteArray.get(nature).get(key)));

		for (Nature nature : optionsDouble.keySet())
			for (String key : optionsDouble.get(nature).keySet())
				props.put(getKey(DATATYPE_DOUBLE, nature, key), optionsDouble.get(nature).get(key).toString());

		for (Nature nature : optionsFloat.keySet())
			for (String key : optionsFloat.get(nature).keySet())
				props.put(getKey(DATATYPE_FLOAT, nature, key), optionsFloat.get(nature).get(key).toString());

		for (Nature nature : optionsInt.keySet())
			for (String key : optionsInt.get(nature).keySet())
				props.put(getKey(DATATYPE_INT, nature, key), optionsInt.get(nature).get(key).toString());

		for (Nature nature : optionsLong.keySet())
			for (String key : optionsLong.get(nature).keySet())
				props.put(getKey(DATATYPE_LONG, nature, key), optionsLong.get(nature).get(key).toString());

		for (Nature nature : optionsString.keySet())
			for (String key : optionsString.get(nature).keySet())
				props.put(getKey(DATATYPE_STRING, nature, key), optionsString.get(nature).get(key));

		/*** META PROPERTIES ***/

		for (String key : metaOptionsBoolean.keySet())
			props.put(getMetaKey(DATATYPE_BOOLEAN, key), metaOptionsBoolean.get(key).toString());

		for (String key : metaOptionsByteArray.keySet())
			props.put(getMetaKey(DATATYPE_BYTEARRAY, key), String.valueOf(metaOptionsByteArray.get(key)));

		for (String key : metaOptionsDouble.keySet())
			props.put(getMetaKey(DATATYPE_DOUBLE, key), metaOptionsDouble.get(key).toString());

		for (String key : metaOptionsFloat.keySet())
			props.put(getMetaKey(DATATYPE_FLOAT, key), metaOptionsFloat.get(key).toString());

		for (String key : metaOptionsInt.keySet())
			props.put(getMetaKey(DATATYPE_INT, key), metaOptionsInt.get(key).toString());

		for (String key : metaOptionsLong.keySet())
			props.put(getMetaKey(DATATYPE_LONG, key), metaOptionsLong.get(key).toString());

		for (String key : metaOptionsString.keySet())
			props.put(getMetaKey(DATATYPE_STRING, key), metaOptionsString.get(key));

		return props;
	}

	@Override
	public String toString() {
		return this.metaOptionsString.containsKey(META_KEY_SPEC_NAME) ? this.metaOptionsString.get(META_KEY_SPEC_NAME)
				: super.toString();
	}

}
