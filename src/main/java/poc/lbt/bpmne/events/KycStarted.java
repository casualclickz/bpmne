package poc.lbt.bpmne.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KycStarted {

	final private String id;
	final private String kycId;
	final private String customerId;
	final private String taskUrl;
	
	@JsonCreator
	public KycStarted(
		@JsonProperty("id") String id, 
		@JsonProperty("kycId") String kycId,
		@JsonProperty("customerId") String customerId,
		@JsonProperty("taskUrl") String taskUrl) {
		
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
