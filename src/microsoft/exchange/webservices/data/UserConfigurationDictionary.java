/**************************************************************************
 * copyright file="UserConfigurationDictionary.java" company="Microsoft"
 *     Copyright (c) Microsoft Corporation.  All rights reserved.
 * 
 * Defines the UserConfigurationDictionary.java.
 **************************************************************************/
package microsoft.exchange.webservices.data;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import microsoft.exchange.webservices.data.exceptions.ServiceLocalException;
import microsoft.exchange.webservices.data.exceptions.ServiceXmlSerializationException;

/**
 * Represents a user configuration's Dictionary property.
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public final class UserConfigurationDictionary extends ComplexProperty
		implements Iterable<Object> {

	// TODO: Consider implementing IsDirty mechanism in ComplexProperty.

	/** The dictionary. */
	private Map<Object, Object> dictionary;

	/** The is dirty. */
	private boolean isDirty = false;

	/**
	 * Initializes a new instance of "UserConfigurationDictionary" class.
	 */
	protected UserConfigurationDictionary() {
		super();
		this.dictionary = new HashMap<Object, Object>();
	}

	/**
	 * Gets the element with the specified key.
	 * 
	 * @param key
	 *            The key of the element to get or set.
	 * @return The element with the specified key.
	 */
	public Object getElements(Object key) {
		return this.dictionary.get(key);
	}

	/**
	 * Sets the element with the specified key.
	 * 
	 * @param key
	 *            The key of the element to get or set
	 * @param value
	 *            the value
	 * @throws Exception
	 *             the exception
	 */
	public void setElements(Object key, Object value) throws Exception {
		this.validateEntry(key, value);
		this.dictionary.put(key, value);
		this.changed();
	}

	/**
	 * Adds an element with the provided key and value to the user configuration
	 * dictionary.
	 * 
	 * @param key
	 *            The object to use as the key of the element to add.
	 * @param value
	 *            The object to use as the value of the element to add.
	 * @throws Exception
	 *             the exception
	 */
	public void addElement(Object key, Object value) throws Exception {
		this.validateEntry(key, value);
		this.dictionary.put(key, value);
		this.changed();
	}

	/**
	 * Determines whether the user configuration dictionary contains an element
	 * with the specified key.
	 * 
	 * @param key
	 *            The key to locate in the user configuration dictionary.
	 * @return true if the user configuration dictionary contains an element
	 *         with the key; otherwise false.
	 */
	public boolean containsKey(Object key) {
		return this.dictionary.containsKey(key);
	}

	/**
	 * Removes the element with the specified key from the user configuration
	 * dictionary.
	 * 
	 * @param key
	 *            The key of the element to remove.
	 * @return true if the element is successfully removed; otherwise false.
	 */
	public boolean remove(Object key) {
		boolean isRemoved = false;
		if (key != null) {
			this.dictionary.remove(key);
			isRemoved = true;
		}

		if (isRemoved) {
			this.changed();
		}

		return isRemoved;
	}

	/**
	 * Gets the value associated with the specified key.
	 * 
	 * @param key
	 *            The key whose value to get.
	 * @param value
	 *            When this method returns, the value associated with the
	 *            specified key, if the key is found; otherwise, null.
	 * @return true if the user configuration dictionary contains the key;
	 *         otherwise false.
	 */
	public boolean tryGetValue(Object key, OutParam<Object> value) {
		if (this.dictionary.containsKey(key)) {
			value.setParam(this.dictionary.get(key));
			return true;
		} else {
			value.setParam(null);
			return false;
		}

	}

	/**
	 * Gets the number of elements in the user configuration dictionary.
	 * 
	 * @return the count
	 */
	public int getCount() {
		return this.dictionary.size();
	}

	/**
	 * Removes all items from the user configuration dictionary.
	 */
	public void clear() {
		if (this.dictionary.size() != 0) {
			this.dictionary.clear();

			this.changed();
		}
	}

	/**
	 * Gets the enumerator.
	 * 
	 * @return the enumerator
	 */
	/**
	 * Returns an enumerator that iterates through 
	 * the user configuration dictionary.
	 * @return An IEnumerator that can be used 
	 * to iterate through the user configuration dictionary.
	 */
	public Iterator<Object> getEnumerator() {
		return (this.dictionary.values().iterator());
	}

	/**
	 * Gets the isDirty flag.
	 * 
	 * @return the checks if is dirty
	 */
	protected boolean getIsDirty() {
		return this.isDirty;
	}

	/**
	 * Sets the isDirty flag.
	 * 
	 * @param value
	 *            the new checks if is dirty
	 */
	protected void setIsDirty(boolean value) {
		this.isDirty = value;
	}

	/***
	 * Instance was changed.
	 */
	@Override
	protected void changed() {
		super.changed();
		this.isDirty = true;
	}

	/**
	 * * Writes elements to XML.
	 * 
	 * @param writer
	 *            accepts EwsServiceXmlWriter
	 * @throws XMLStreamException
	 *             the xML stream exception
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 */
	@Override
	protected void writeElementsToXml(EwsServiceXmlWriter writer)
			throws XMLStreamException, ServiceXmlSerializationException {
		EwsUtilities.EwsAssert(writer != null,
				"UserConfigurationDictionary.WriteElementsToXml",
				"writer is null");
		Iterator<Entry<Object, Object>> it = this.dictionary.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> dictionaryEntry = it.next();
			writer.writeStartElement(XmlNamespace.Types,
					XmlElementNames.DictionaryEntry);
			this.writeObjectToXml(writer, XmlElementNames.DictionaryKey,
					dictionaryEntry.getKey());
			this.writeObjectToXml(writer, XmlElementNames.DictionaryValue,
					dictionaryEntry.getValue());
			writer.writeEndElement();
		}
	}

	/**
	 * Writes a dictionary object (key or value) to Xml.
	 * 
	 * @param writer
	 *            The writer.
	 * @param xmlElementName
	 *            The Xml element name.
	 * @param dictionaryObject
	 *            The object to write.
	 * @throws XMLStreamException
	 *             the xML stream exception
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 */
	private void writeObjectToXml(EwsServiceXmlWriter writer,
			String xmlElementName, Object dictionaryObject)
			throws XMLStreamException, ServiceXmlSerializationException {
		EwsUtilities.EwsAssert(writer != null,
				"UserConfigurationDictionary.WriteObjectToXml",
				"writer is null");
		EwsUtilities.EwsAssert(xmlElementName != null,
				"UserConfigurationDictionary.WriteObjectToXml",
				"xmlElementName is null");
		writer.writeStartElement(XmlNamespace.Types, xmlElementName);

		if (dictionaryObject == null) {
			EwsUtilities.EwsAssert((!xmlElementName
					.equals(XmlElementNames.DictionaryKey)),
					"UserConfigurationDictionary.WriteObjectToXml",
					"Key is null");

			writer.writeAttributeValue(
					EwsUtilities.EwsXmlSchemaInstanceNamespacePrefix,
					XmlAttributeNames.Nil, EwsUtilities.XSTrue);
		} else {
			this.writeObjectValueToXml(writer, dictionaryObject);
		}

		writer.writeEndElement();
	}

	/**
	 * Writes a dictionary Object's value to Xml.
	 * 
	 * @param writer
	 *            The writer.
	 * @param dictionaryObject
	 *            The dictionary object to write.
	 * @throws XMLStreamException
	 *             the xML stream exception
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 */
	private void writeObjectValueToXml(EwsServiceXmlWriter writer,
			Object dictionaryObject) throws XMLStreamException,
			ServiceXmlSerializationException {
		EwsUtilities.EwsAssert(writer != null,
				"UserConfigurationDictionary.WriteObjectValueToXml",
				"writer is null");
		EwsUtilities.EwsAssert(dictionaryObject != null,
				"UserConfigurationDictionary.WriteObjectValueToXml",
				"dictionaryObject is null");

		// This logic is based on
		// Microsoft.Exchange.Services.Core.GetUserConfiguration.
		// ConstructDictionaryObject().
		//
		// Object values are either:
		// . an array of strings
		// . a single value
		//
		// Single values can be:
		// . base64 string (from a byte array)
		// . datetime, boolean, byte, short, int, long, string, ushort, unint,
		// ulong
		//
		// First check for a string array
		String[] dictionaryObjectAsStringArray = null;
		byte[] dictionaryObjectAsByteArray = null;
		if (dictionaryObject != null) {
			dictionaryObjectAsStringArray = (String[]) dictionaryObject;
			dictionaryObjectAsByteArray = (byte[]) dictionaryObject;
		}
		if (dictionaryObjectAsStringArray != null) {
			this.writeEntryTypeToXml(writer,
					UserConfigurationDictionaryObjectType.StringArray);

			for (String arrayElement : dictionaryObjectAsStringArray) {
				this.writeEntryValueToXml(writer, arrayElement);
			}
		} else {
			// if not a string array, all other object values are returned as a
			// single element
			UserConfigurationDictionaryObjectType dictionaryObjectType = 
				UserConfigurationDictionaryObjectType.String;
			String valueAsString = null;

			if (dictionaryObjectAsByteArray != null) {
				// Convert byte array to base64 string
				dictionaryObjectType = UserConfigurationDictionaryObjectType.
				ByteArray;
				valueAsString = Base64EncoderStream
						.encode(dictionaryObjectAsByteArray);

			} else {
				// Handle all other types by TypeCode
				if (dictionaryObject.getClass().equals(Boolean.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.Boolean;
					valueAsString = EwsUtilities
							.boolToXSBool((Boolean) dictionaryObject);

				} else if (dictionaryObject.getClass().equals(Byte.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.Byte;
					valueAsString = ((Byte) dictionaryObject).toString();
				} else if (dictionaryObject.getClass().equals(Date.class)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.DateTime;
					valueAsString = writer.getService()
							.convertDateTimeToUniversalDateTimeString(
									(Date) dictionaryObject);
				} else if (dictionaryObject.getClass().equals(Integer.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.Integer32;
					valueAsString = ((Integer) dictionaryObject).toString();
				} else if (dictionaryObject.getClass().equals(Long.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.Integer64;
					valueAsString = ((Long) dictionaryObject).toString();
				} else if (dictionaryObject.getClass().equals(String.class)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.String;
					valueAsString = (String) dictionaryObject;
				} else if (dictionaryObject.getClass().equals(Integer.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.UnsignedInteger32;
					valueAsString = ((Integer) dictionaryObject).toString();
				} else if (dictionaryObject.getClass().equals(Long.TYPE)) {
					dictionaryObjectType = 
						UserConfigurationDictionaryObjectType.UnsignedInteger64;
					valueAsString = ((Long) dictionaryObject).toString();
				} else {
					EwsUtilities
							.EwsAssert(
									false,
									"UserConfigurationDictionary." +
									"WriteObjectValueToXml",
									"Unsupported type: "
											+ dictionaryObject.getClass()
													.toString());
				}
			}

			this.writeEntryTypeToXml(writer, dictionaryObjectType);
			this.writeEntryValueToXml(writer, valueAsString);
		}
	}

	/**
	 * Writes a dictionary entry type to Xml.
	 * 
	 * @param writer
	 *            The writer.
	 * @param dictionaryObjectType
	 *            Type to write.
	 * @throws XMLStreamException
	 *             the xML stream exception
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 */
	private void writeEntryTypeToXml(EwsServiceXmlWriter writer,
			UserConfigurationDictionaryObjectType dictionaryObjectType)
			throws XMLStreamException, ServiceXmlSerializationException {
		writer.writeStartElement(XmlNamespace.Types, XmlElementNames.Type);
		writer
				.writeValue(dictionaryObjectType.toString(),
						XmlElementNames.Type);
		writer.writeEndElement();
	}

	/**
	 * Writes a dictionary entry value to Xml.
	 * 
	 * @param writer
	 *            The writer.
	 * @param value
	 *            Value to write.
	 * @throws XMLStreamException
	 *             the xML stream exception
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 */
	private void writeEntryValueToXml(EwsServiceXmlWriter writer, String value)
			throws XMLStreamException, ServiceXmlSerializationException {
		writer.writeStartElement(XmlNamespace.Types, XmlElementNames.Value);

		// While an entry value can't be null, if the entry is an array, an
		// element of the array can be null.
		if (value != null) {
			writer.writeValue(value, XmlElementNames.Value);
		}

		writer.writeEndElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * microsoft.exchange.webservices.ComplexProperty#loadFromXml(microsoft.
	 * exchange.webservices.EwsServiceXmlReader,
	 * microsoft.exchange.webservices.XmlNamespace, java.lang.String)
	 */
	@Override
	/**
	 * Loads this dictionary from the specified reader.
	 * @param reader The reader.
	 * @param xmlNamespace The dictionary's XML namespace.
	 * @param xmlElementName Name of the XML element 
	 * representing the dictionary.
	 */
	protected void loadFromXml(EwsServiceXmlReader reader,
			XmlNamespace xmlNamespace, String xmlElementName) throws Exception {
		super.loadFromXml(reader, xmlNamespace, xmlElementName);

		this.isDirty = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * microsoft.exchange.webservices.ComplexProperty#tryReadElementFromXml(
	 * microsoft.exchange.webservices.EwsServiceXmlReader)
	 */
	@Override
	/**
	 * Tries to read element from XML. 
	 * @param reader The reader.
	 * @return True if element was read.
	 */
	protected boolean tryReadElementFromXml(EwsServiceXmlReader reader)
			throws Exception {
		reader.ensureCurrentNodeIsStartElement(this.getNamespace(),
				XmlElementNames.DictionaryEntry);
		this.loadEntry(reader);
		return true;
	}

	/**
	 * Loads an entry, consisting of a key value pair, into this dictionary from
	 * the specified reader.
	 * 
	 * @param reader
	 *            The reader.
	 * @throws Exception
	 *             the exception
	 */
	private void loadEntry(EwsServiceXmlReader reader) throws Exception {
		EwsUtilities.EwsAssert(reader != null,
				"UserConfigurationDictionary.LoadEntry", "reader is null");

		Object key;
		Object value = null;

		// Position at DictionaryKey
		reader.readStartElement(this.getNamespace(),
				XmlElementNames.DictionaryKey);

		key = this.getDictionaryObject(reader);

		// Position at DictionaryValue
		reader.readStartElement(this.getNamespace(),
				XmlElementNames.DictionaryValue);

		String nil = reader.readAttributeValue(XmlNamespace.XmlSchemaInstance,
				XmlAttributeNames.Nil);
		boolean hasValue = (nil == null)
				|| (!nil.getClass().equals(Boolean.TYPE));
		if (hasValue) {
			value = this.getDictionaryObject(reader);
		}
		this.dictionary.put(key, value);
	}

	/**
	 * Extracts a dictionary object (key or entry value) from the specified
	 * reader.
	 * 
	 * @param reader
	 *            The reader.
	 * @return Dictionary object.
	 * @throws Exception
	 *             the exception
	 */
	private Object getDictionaryObject(EwsServiceXmlReader reader)
			throws Exception {
		EwsUtilities.EwsAssert(reader != null,
				"UserConfigurationDictionary.loadFromXml", "reader is null");
		UserConfigurationDictionaryObjectType type = this.getObjectType(reader);
		List<String> values = this.getObjectValue(reader, type);
		return this.constructObject(type, values, reader);
	}

	/**
	 * Extracts a dictionary object (key or entry value) as a string list from
	 * the specified reader.
	 * 
	 * @param reader
	 *            The reader.
	 * @param type
	 *            The object type.
	 * @return String list representing a dictionary object.
	 * @throws Exception
	 *             the exception
	 */
	private List<String> getObjectValue(EwsServiceXmlReader reader,
			UserConfigurationDictionaryObjectType type) throws Exception {
		EwsUtilities.EwsAssert(reader != null,
				"UserConfigurationDictionary.LoadFromXml", "reader is null");

		List<String> values = new ArrayList<String>();

		reader.readStartElement(this.getNamespace(), XmlElementNames.Value);

		do {
			String value = null;

			if (reader.isEmptyElement()) {
				// Only string types can be represented with empty values.
				if (type.equals(UserConfigurationDictionaryObjectType.String)
						|| type
								.equals(UserConfigurationDictionaryObjectType.
										StringArray)) {
					value = "";
				} else {
					EwsUtilities
							.EwsAssert(
									false,
									"UserConfigurationDictionary." +
									"GetObjectValue",
									"Empty element passed for type: "
											+ type.toString());

				}

			}

			else {
				value = reader.readElementValue();
			}

			values.add(value);
			reader.read(); // Position at next element or
			// DictionaryKey/DictionaryValue end element
		} while (reader.isStartElement(this.getNamespace(),
				XmlElementNames.Value));
		return values;
	}

	/**
	 * Extracts the dictionary object (key or entry value) type from the
	 * specified reader.
	 * 
	 * @param reader
	 *            The reader.
	 * @return Dictionary object type.
	 * @throws Exception
	 *             the exception
	 */
	private UserConfigurationDictionaryObjectType getObjectType(
			EwsServiceXmlReader reader) throws Exception {
		EwsUtilities.EwsAssert(reader != null,
				"UserConfigurationDictionary.LoadFromXml", "reader is null");

		reader.readStartElement(this.getNamespace(), XmlElementNames.Type);

		String type = reader.readElementValue();
		return UserConfigurationDictionaryObjectType.valueOf(type);
	}

	/**
	 * Constructs a dictionary object (key or entry value) from the specified
	 * type and string list.
	 * 
	 * @param type
	 *            Object type to construct.
	 * @param value
	 *            Value of the dictionary object as a string list
	 * @param reader
	 *            The reader.
	 * @return Dictionary object.
	 */
	private Object constructObject(UserConfigurationDictionaryObjectType type,
			List<String> value, EwsServiceXmlReader reader) {
		EwsUtilities.EwsAssert(value != null,
				"UserConfigurationDictionary.ConstructObject", "value is null");
		EwsUtilities
				.EwsAssert(
						(value.size() == 1 || type ==
							UserConfigurationDictionaryObjectType.StringArray),
								
						"UserConfigurationDictionary.ConstructObject",
						"value is array but type is not StringArray");
		EwsUtilities
				.EwsAssert(reader != null,
						"UserConfigurationDictionary.ConstructObject",
						"reader is null");

		Object dictionaryObject = null;
		if (type.equals(UserConfigurationDictionaryObjectType.Boolean)) {
			dictionaryObject = Boolean.parseBoolean(value.get(0));
		} else if (type.equals(UserConfigurationDictionaryObjectType.Byte)) {
			dictionaryObject = Byte.parseByte(value.get(0));
		} else if (type.equals(UserConfigurationDictionaryObjectType.ByteArray)) {
			dictionaryObject = Base64EncoderStream.decode(value.get(0));
		} else if (type.equals(UserConfigurationDictionaryObjectType.DateTime)) {
			Date dateTime = reader.getService()
					.convertUniversalDateTimeStringToDate(value.get(0));
			if (dateTime != null) {
				dictionaryObject = dateTime;
			} else {
				EwsUtilities.EwsAssert(false,
						"UserConfigurationDictionary.ConstructObject",
						"DateTime is null");
			}
		} else if (type.equals(UserConfigurationDictionaryObjectType.Integer32)) {
			dictionaryObject = Integer.parseInt(value.get(0));
		} else if (type.equals(UserConfigurationDictionaryObjectType.Integer64)) {
			dictionaryObject = Long.parseLong(value.get(0));
		} else if (type.equals(UserConfigurationDictionaryObjectType.String)) {
			dictionaryObject = String.valueOf(value.get(0));
		} else if (type
				.equals(UserConfigurationDictionaryObjectType.StringArray)) {
			dictionaryObject = value.toArray();
		} else if (type
				.equals(UserConfigurationDictionaryObjectType.
						UnsignedInteger32)) {
			dictionaryObject = Integer.parseInt(value.get(0));
		} else if (type
				.equals(UserConfigurationDictionaryObjectType.
						UnsignedInteger64)) {
			dictionaryObject = Long.parseLong(value.get(0));
		} else {
			EwsUtilities.EwsAssert(false,
					"UserConfigurationDictionary.ConstructObject",
					"Type not recognized: " + type.toString());
		}

		return dictionaryObject;
	}

	/**
	 * Validates the specified key and value.
	 * 
	 * @param key
	 *            The key.
	 * @param value
	 *            The diction dictionary entry key.ary entry value.
	 * @throws Exception
	 *             the exception
	 */
	private void validateEntry(Object key, Object value) throws Exception {
		this.validateObject(key);
		this.validateObject(value);

	}

	/**
	 * Validates the dictionary object (key or entry value).
	 * 
	 * @param dictionaryObject
	 *            Object to validate.
	 * @throws Exception
	 *             the exception
	 */
	private void validateObject(Object dictionaryObject) throws Exception {
		// Keys may not be null but we rely on the internal dictionary to throw
		// if the key is null.
		if (dictionaryObject != null) {
	if(dictionaryObject.getClass().isArray() ){
				int length = Array.getLength(dictionaryObject);
			    Class<? extends Object> wrapperType = Array.get(dictionaryObject, 0).getClass();
			    Object[] newArray = (Object[]) Array.
			    newInstance(wrapperType, length);
			    for (int i = 0; i < length; i++) {
			      newArray[i] = Array.get(dictionaryObject, i);
			    }
				this.validateArrayObject(newArray);
			}			
			else{
				this.validateObjectType(dictionaryObject.getClass());
				
			}

		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Validate the array object.
	 * 
	 * @param dictionaryObjectAsArray
	 *            Object to validate
	 * @throws ServiceLocalException
	 *             the service local exception
	 */
	private void validateArrayObject(Object[] dictionaryObjectAsArray)
			throws ServiceLocalException {
		// This logic is based on
		// Microsoft.Exchange.Data.Storage.ConfigurationDictionary.
		// CheckElementSupportedType().
		// if (dictionaryObjectAsArray is string[])

		if (dictionaryObjectAsArray instanceof String[]) {
			if (dictionaryObjectAsArray.length > 0) {
				for (Object arrayElement : dictionaryObjectAsArray) {
					if (arrayElement == null) {
						throw new ServiceLocalException(
								Strings.NullStringArrayElementInvalid);
					}
				}
			} else {
				throw new ServiceLocalException(Strings.ZeroLengthArrayInvalid);
			}
		} else if (dictionaryObjectAsArray instanceof Byte[]) {
			if (dictionaryObjectAsArray.length <= 0) {
				throw new ServiceLocalException(Strings.ZeroLengthArrayInvalid);
			}
		} else {
			throw new ServiceLocalException(String.format(
					Strings.ObjectTypeNotSupported, dictionaryObjectAsArray
							.getClass()));
		}
	}

	/**
	 * Validates the dictionary object type.
	 * 
	 * @param type
	 *            Type to validate.
	 * @throws ServiceLocalException
	 *             the service local exception
	 */
	private void validateObjectType(Type type) throws ServiceLocalException {
		// This logic is based on
		// Microsoft.Exchange.Data.Storage.ConfigurationDictionary.
		// CheckElementSupportedType().
		boolean isValidType = false;
		if (type.equals(Boolean.TYPE) || type.equals(Byte.TYPE)
				|| type.equals(Date.class) || type.equals(Integer.TYPE)
				|| type.equals(Long.TYPE) || type.equals(String.class)
				|| type.equals(Integer.TYPE) || type.equals(Long.TYPE)) {
			isValidType = true;
		}
		if (!isValidType) {
			throw new ServiceLocalException(String.format("%s,%s",
					Strings.ObjectTypeNotSupported, type));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Object> iterator() {
		return this.dictionary.values().iterator();

	}

}
