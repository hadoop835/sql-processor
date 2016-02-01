package org.sample.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.sample.model.PersonGender;
import org.sqlproc.engine.annotation.Pojo;

@Pojo
@SuppressWarnings("all")
public class NewPersonRetRsResult implements Serializable {
  private final static long serialVersionUID = 1L;
  
  private String firstName;
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }
  
  public NewPersonRetRsResult _setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }
  
  private String ssn;
  
  public String getSsn() {
    return this.ssn;
  }
  
  public void setSsn(final String ssn) {
    this.ssn = ssn;
  }
  
  public NewPersonRetRsResult _setSsn(final String ssn) {
    this.ssn = ssn;
    return this;
  }
  
  private PersonGender gender;
  
  public PersonGender getGender() {
    return this.gender;
  }
  
  public void setGender(final PersonGender gender) {
    this.gender = gender;
  }
  
  public NewPersonRetRsResult _setGender(final PersonGender gender) {
    this.gender = gender;
    return this;
  }
  
  private Date dateOfBirth;
  
  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }
  
  public void setDateOfBirth(final Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
  
  public NewPersonRetRsResult _setDateOfBirth(final Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }
  
  private Long id;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(final Long id) {
    this.id = id;
  }
  
  public NewPersonRetRsResult _setId(final Long id) {
    this.id = id;
    return this;
  }
  
  private String lastName;
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }
  
  public NewPersonRetRsResult _setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }
  
  public int hashCodeForAttributes() {
    int result = 1;
    result = 31 * result + ((firstName != null) ? firstName.hashCode() : 0);
    result = 31 * result + ((ssn != null) ? ssn.hashCode() : 0);
    result = 31 * result + ((gender != null) ? gender.hashCode() : 0);
    result = 31 * result + ((dateOfBirth != null) ? dateOfBirth.hashCode() : 0);
    result = 31 * result + ((id != null) ? id.hashCode() : 0);
    result = 31 * result + ((lastName != null) ? lastName.hashCode() : 0);
    return result;
  }
  
  @Override
  public String toString() {
    return "NewPersonRetRsResult [firstName=" + firstName + ", ssn=" + ssn + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", id=" + id + ", lastName=" + lastName + "]";
  }
  
  public String toStringFull() {
    return "NewPersonRetRsResult [firstName=" + firstName + ", ssn=" + ssn + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", id=" + id + ", lastName=" + lastName + "]";
  }
  
  public enum Attribute {
    firstName,
    
    ssn,
    
    gender,
    
    dateOfBirth,
    
    id,
    
    lastName;
  }
  
  private Set<String> nullValues =  new java.util.HashSet<String>();
  
  public void setNull(final NewPersonRetRsResult.Attribute... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (Attribute attribute : attributes)
    	nullValues.add(attribute.name());
  }
  
  public NewPersonRetRsResult _setNull(final NewPersonRetRsResult.Attribute... attributes) {
    setNull(attributes);
    return this;
  }
  
  public void clearNull(final NewPersonRetRsResult.Attribute... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (Attribute attribute : attributes)
    	nullValues.remove(attribute.name());
  }
  
  public NewPersonRetRsResult _clearNull(final NewPersonRetRsResult.Attribute... attributes) {
    clearNull(attributes);
    return this;
  }
  
  public void setNull(final String... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (String attribute : attributes)
    	nullValues.add(attribute);
  }
  
  public NewPersonRetRsResult _setNull(final String... attributes) {
    setNull(attributes);
    return this;
  }
  
  public void clearNull(final String... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (String attribute : attributes)
    	nullValues.remove(attribute);
  }
  
  public NewPersonRetRsResult _clearNull(final String... attributes) {
    clearNull(attributes);
    return this;
  }
  
  public Boolean isNull(final NewPersonRetRsResult.Attribute attribute) {
    if (attribute == null)
    	throw new IllegalArgumentException();
    return nullValues.contains(attribute.name());
  }
  
  public Boolean isNull(final String attrName) {
    if (attrName == null)
    	throw new IllegalArgumentException();
    return nullValues.contains(attrName);
  }
  
  public Boolean isDef(final String attrName, final Boolean isAttrNotNull) {
    if (attrName == null)
    	throw new IllegalArgumentException();
    if (nullValues.contains(attrName))
    	return true;
    if (isAttrNotNull != null)
    	return isAttrNotNull;
    return false;
  }
  
  public void clearAllNull() {
    nullValues = new java.util.HashSet<String>();
  }
  
  public int hashCodeForNulls() {
    if (nullValues == null)
    	return 0;
    int result = 1;
    for (Attribute attribute : Attribute.values()) {
    	result = 31 * result + (nullValues.contains(attribute.name()) ? attribute.name().hashCode() : 0);
    }
    return result;
  }
  
  public enum OpAttribute {
    firstName,
    
    ssn,
    
    gender,
    
    dateOfBirth,
    
    id,
    
    lastName;
  }
  
  private Map<String, String> operators =  new java.util.HashMap<String, String>();
  
  public Map<String, String> getOperators() {
    return operators;
  }
  
  public void setOp(final String operator, final NewPersonRetRsResult.OpAttribute... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (OpAttribute attribute : attributes)
    	operators.put(attribute.name(), operator);
  }
  
  public NewPersonRetRsResult _setOp(final String operator, final NewPersonRetRsResult.OpAttribute... attributes) {
    setOp(operator, attributes);
    return this;
  }
  
  public void clearOp(final NewPersonRetRsResult.OpAttribute... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (OpAttribute attribute : attributes)
    	operators.remove(attribute.name());
  }
  
  public NewPersonRetRsResult _clearOp(final NewPersonRetRsResult.OpAttribute... attributes) {
    clearOp(attributes);
    return this;
  }
  
  public void setOp(final String operator, final String... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (String attribute : attributes)
    	operators.put(attribute, operator);
  }
  
  public NewPersonRetRsResult _setOp(final String operator, final String... attributes) {
    setOp(operator, attributes);
    return this;
  }
  
  public void clearOp(final String... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (String attribute : attributes)
    	operators.remove(attribute);
  }
  
  public NewPersonRetRsResult _clearOp(final String... attributes) {
    clearOp(attributes);
    return this;
  }
  
  public void setNullOp(final NewPersonRetRsResult.OpAttribute... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (OpAttribute attribute : attributes)
    	operators.put(attribute.name(), "is null");
  }
  
  public NewPersonRetRsResult _setNullOp(final NewPersonRetRsResult.OpAttribute... attributes) {
    setNullOp(attributes);
    return this;
  }
  
  public void setNullOp(final String... attributes) {
    if (attributes == null)
    	throw new IllegalArgumentException();
    for (String attribute : attributes)
    	operators.put(attribute, "is null");
  }
  
  public NewPersonRetRsResult _setNullOp(final String... attributes) {
    setNullOp(attributes);
    return this;
  }
  
  public void clearAllOps() {
    operators = new java.util.HashMap<String, String>();
  }
  
  public int hashCodeForOperators() {
    if (operators == null)
    	return 0;
    int result = 1;
    for (OpAttribute opAttribute : OpAttribute.values()) {
    	result = 31 * result + (operators.containsKey(opAttribute.name()) ? operators.get(opAttribute.name()).hashCode() : 0);
    }
    return result;
  }
  
  public String getProcessingId(final Object... moreAttributes) {
    StringBuilder result = new StringBuilder();
    result.append(",BASE:").append(hashCodeForAttributes());
    result.append(",DEF:").append(hashCodeForNulls());
    result.append(",OPER:").append(hashCodeForOperators());
    if (moreAttributes != null)
    	result.append(",MORE:").append(java.util.Arrays.hashCode(moreAttributes));
    return result.toString();
  }
}
