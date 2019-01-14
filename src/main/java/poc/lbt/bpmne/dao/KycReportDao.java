package poc.lbt.bpmne.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.report.KycReport;

public interface KycReportDao extends JpaRepository<KycReport, Long> {	
	public List<KycReport> findByEventId(String eventId);
	public List<KycReport> findByCustomerReportId(Long customerReportId);
}
