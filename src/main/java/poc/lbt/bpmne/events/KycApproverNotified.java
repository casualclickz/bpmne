package poc.lbt.bpmne.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import poc.lbt.bpmne.report.CustomerReport;

public class KycApproverNotified {

	final private String id;	
	final private String kycId;
	final private String staffEmail;
	final private String taskUrl;
	final private CustomerReport customerReport;
	
	@JsonCreator
	public KycApproverNotified(
		@JsonProperty("id") String id, 
		@JsonProperty("kycId") String kycId, 
		@JsonProperty("staffEmail") String staffEmail, 
		@JsonProperty("taskUrl") String taskUrl,
		@JsonProperty("customerReport") CustomerReport customerReport) {
		
		this.id = id;
		this.kycId =  kycId;
		this.staffEmail = staffEmail;
		this.taskUrl = taskUrl;
		this.customerReport = customerReport;
	}

	public String getId() {
		return id;
	}

	public String getKycId() {
		return kycId;
	}

	public String getStaffEmail() {
		return staffEmail;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public CustomerReport getCustomerReport() {
		return customerReport;
	}
}
