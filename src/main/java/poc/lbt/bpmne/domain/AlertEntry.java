package poc.lbt.bpmne.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="AlertEntrys")
public class AlertEntry {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long id;

	@Column(name = "ScheduledOn")
	@Temporal(TemporalType.TIMESTAMP)
	Date scheduledOn;
	boolean active;
	Long customerReportId;

	public AlertEntry() {
	}

	public AlertEntry(Date date, boolean active, Long customerReportId) {
		this.scheduledOn = date;
		this.active = active;
		this.customerReportId = customerReportId;
	}
	
	public Long getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "AlertEntry [id=" + id + ", scheduledOn=" + scheduledOn + ", active=" + active + ", customerReportId="
				+ customerReportId + "]";
	}	
}
