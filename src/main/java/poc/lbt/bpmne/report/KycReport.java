package poc.lbt.bpmne.report;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Kycs")
public class KycReport {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	//@JsonIgnore
	private long id;

	String taskId;
	String processKey;
	Long customerReportId;
	String eventId;
	boolean active;

	public KycReport() {
	}

	public KycReport(String taskId, String processKey, Long customerReportId, String eventId, boolean active) {
		super();
		this.taskId = taskId;
		this.processKey = processKey;
		this.customerReportId = customerReportId;
		this.eventId = eventId;
		this.active = active;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public Long getCustomerReportId() {
		return customerReportId;
	}
	public void setCustomerReportId(Long customerReportId) {
		this.customerReportId = customerReportId;
	}

	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "KycReport [id=" + id + ", taskId=" + taskId + ", processKey=" + processKey + ", customerReportId="
				+ customerReportId + ", eventId=" + eventId + ", active=" + active + "]";
	}	
}
