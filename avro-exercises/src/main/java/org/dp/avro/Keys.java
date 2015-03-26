/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.dp.avro;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Keys extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Keys\",\"namespace\":\"org.dp.avro\",\"fields\":[{\"name\":\"values\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.util.List<java.lang.CharSequence> values;

  /**
   * Default constructor.
   */
  public Keys() {}

  /**
   * All-args constructor.
   */
  public Keys(java.util.List<java.lang.CharSequence> values) {
    this.values = values;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return values;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: values = (java.util.List<java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'values' field.
   */
  public java.util.List<java.lang.CharSequence> getValues() {
    return values;
  }

  /**
   * Sets the value of the 'values' field.
   * @param value the value to set.
   */
  public void setValues(java.util.List<java.lang.CharSequence> value) {
    this.values = value;
  }

  /** Creates a new Keys RecordBuilder */
  public static org.dp.avro.Keys.Builder newBuilder() {
    return new org.dp.avro.Keys.Builder();
  }
  
  /** Creates a new Keys RecordBuilder by copying an existing Builder */
  public static org.dp.avro.Keys.Builder newBuilder(org.dp.avro.Keys.Builder other) {
    return new org.dp.avro.Keys.Builder(other);
  }
  
  /** Creates a new Keys RecordBuilder by copying an existing Keys instance */
  public static org.dp.avro.Keys.Builder newBuilder(org.dp.avro.Keys other) {
    return new org.dp.avro.Keys.Builder(other);
  }
  
  /**
   * RecordBuilder for Keys instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Keys>
    implements org.apache.avro.data.RecordBuilder<Keys> {

    private java.util.List<java.lang.CharSequence> values;

    /** Creates a new Builder */
    private Builder() {
      super(org.dp.avro.Keys.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(org.dp.avro.Keys.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing Keys instance */
    private Builder(org.dp.avro.Keys other) {
            super(org.dp.avro.Keys.SCHEMA$);
      if (isValidValue(fields()[0], other.values)) {
        this.values = data().deepCopy(fields()[0].schema(), other.values);
        fieldSetFlags()[0] = true;
      }
    }

    /** Gets the value of the 'values' field */
    public java.util.List<java.lang.CharSequence> getValues() {
      return values;
    }
    
    /** Sets the value of the 'values' field */
    public org.dp.avro.Keys.Builder setValues(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[0], value);
      this.values = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'values' field has been set */
    public boolean hasValues() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'values' field */
    public org.dp.avro.Keys.Builder clearValues() {
      values = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public Keys build() {
      try {
        Keys record = new Keys();
        record.values = fieldSetFlags()[0] ? this.values : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
