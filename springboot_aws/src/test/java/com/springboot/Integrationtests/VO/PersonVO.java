package com.springboot.Integrationtests.VO;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import jakarta.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class PersonVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    @JsonProperty("first_name")
    @JacksonXmlProperty(localName = "first_name")
    //FIXME this is not working. If i use firstName the property name is not changed in the xml
	private String first_name;

    @JsonProperty("last_name")
    @JacksonXmlProperty(localName = "last_name")
	private String last_name;
	private String address;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String gender;
	
	public PersonVO() {}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String firstName) {
		this.first_name = firstName;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String lastName) {
		this.last_name = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, first_name, gender, id, last_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonVO other = (PersonVO) obj;
		return Objects.equals(address, other.address) && Objects.equals(first_name, other.first_name)
				&& Objects.equals(gender, other.gender) && Objects.equals(id, other.id)
				&& Objects.equals(last_name, other.last_name);
	}
	
	




}