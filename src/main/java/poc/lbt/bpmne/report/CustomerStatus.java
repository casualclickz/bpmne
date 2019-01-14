package poc.lbt.bpmne.report;

public enum CustomerStatus {
	
	NEWCUSTOMER("New"),
	AWAITINGKYC("Awaiting KYC"),
	VERIFIEDCUSTOMER("Verified");
	
	private String status;
	
	CustomerStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
