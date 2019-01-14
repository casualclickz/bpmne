package poc.lbt.bpmne.events;

import poc.lbt.bpmne.report.CustomerReport;
import poc.lbt.bpmne.report.KycReport;

public class TaskOwnerNotified {

	final private String id;
	final private CustomerReport customerReport;
	final private KycReport kycReport;
	
	public TaskOwnerNotified(
		String id,
		CustomerReport customerReport,
		KycReport kycReport) {
		
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
