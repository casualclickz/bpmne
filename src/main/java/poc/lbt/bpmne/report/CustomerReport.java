package poc.lbt.bpmne.report;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Customers")
public class CustomerReport {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	//@JsonIgnore
	private long id;

	String customerName;
	String address;
	Long age;
	String status;
	String phone;
	
	String eventId;
	String associationId;
	
	boolean raiseKycNotification;
	
	public CustomerReport() {
	}
	
	public CustomerReport(String customerName, String address, Long age, String status, String phone, String eventId, String associationId) {
		this.customerName = customerName;
		this.address = address;
		this.age = age;
		this.status = status;
		this.phone = phone;
		this.eventId = eventId;
		this.associationId = associationId;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAssociationId() {
		return associationId;
	}
	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	public boolean isRaiseKycNotification() {
		return raiseKycNotification;
	}
	public void setRaiseKycNotification(boolean raiseKycNotification) {
		this.raiseKycNotification = raiseKycNotification;
	}
}
