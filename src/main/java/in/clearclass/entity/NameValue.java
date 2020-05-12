package in.clearclass.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameValue {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Value")
	private String value;
	
	@Override
	public String toString() {
		return "[name=" + name + ", value=" + value + "]";
	}
	public String getValue() {
		return value;
	}
}