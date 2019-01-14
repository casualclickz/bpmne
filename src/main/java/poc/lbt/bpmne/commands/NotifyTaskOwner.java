package poc.lbt.bpmne.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import poc.lbt.bpmne.report.CustomerReport;
import poc.lbt.bpmne.report.KycReport;

public class NotifyTaskOwner {

	@TargetAggregateIdentifier
	final String id;
	
	final CustomerReport customerReport;
	final KycReport kycReport;

	public NotifyTaskOwner(String id, CustomerReport customerReport, KycReport kycReport) {
		this.id = id;
		this.customerReport =  customerReport;
		this.kycReport = kycReport;
	}

	public String getId() {
		return id;
	}

	public CustomerReport getCustomerReport() {
		return customerReport;
	}
	
	public KycReport getKycReport() {
		return kycReport;
	}
}
