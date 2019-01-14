package poc.lbt.bpmne.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class StartKyc {

	@TargetAggregateIdentifier
	final String id;
	
	final String kycId;
	final String customerId;
	final String taskUrl;

	public StartKyc(String id, String kycId, String customerId, String taskUrl) {
		this.id = id;
		this.kycId =  kycId;
		this.customerId = customerId;
		this.taskUrl = taskUrl;
	}

	public String getId() {
		return id;
	}

	public String getKycId() {
		return kycId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getTaskUrl() {
		return taskUrl;
	}
}
