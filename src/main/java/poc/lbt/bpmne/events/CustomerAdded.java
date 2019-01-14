package poc.lbt.bpmne.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerAdded {

	final private String id;
	final private String customerName;
	final private String address;
	final private Long age;
	final private String phone;
	
	@JsonCreator
	public CustomerAdded(
		@JsonProperty("id") String id, 
		@JsonProperty("customerName") String customerName, 
		@JsonProperty("address") String address, 
		@JsonProperty("age") Long age,
		@JsonProperty("phone") String phone) {
		
		this.id = id;
		this.customerName =  customerName;
		this.address = address;
		this.age = age;
		this.phone = phone;
	}
	
	public String getId() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getAddress() {
		return address;
	}

	public Long getAge() {
		return age;
	}
	
	public String getPhone() {
		return phone;
	}
}
