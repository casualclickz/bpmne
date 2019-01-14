package poc.lbt.bpmne.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import poc.lbt.bpmne.report.CustomerReport;

public class StartKycTask {

	@TargetAggregateIdentifier
	final String id;
	
	final CustomerReport customerReport;

	public StartKycTask(String id, CustomerReport customerReport) {
		this.id = id;
		this.customerReport =  customerReport;
	}

	public String getId() {
		return id;
	}

	public CustomerReport getCustomerReport() {
		return customerReport;
	}
}
