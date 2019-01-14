package poc.lbt.bpmne.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import poc.lbt.bpmne.report.CustomerReport;

public class NotifyKycApprover {

	@TargetAggregateIdentifier
	final String id;
	
	final String kycId;
	final String staffEmail;
	final String taskUrl;
	final CustomerReport customerReport;

	public NotifyKycApprover(String id, String kycId, String staffEmail, String taskUrl, CustomerReport customerReport) {
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
