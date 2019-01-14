package poc.lbt.bpmne.events;

import org.springframework.stereotype.Component;

import poc.lbt.bpmne.report.CustomerReport;

public class KycTaskStarted {

	final private String id;
	final private CustomerReport customerReport;
	
	public KycTaskStarted(
		String id,
		CustomerReport customerReport) {
		
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
