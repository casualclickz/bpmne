package poc.lbt.bpmne.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class AddCustomer {

	@TargetAggregateIdentifier
	final String id;
	
	final String customerName;
	final String address;
	final Long age;
	final String phone;

	public AddCustomer(String id, String customerName, String address, Long age, String phone) {
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
