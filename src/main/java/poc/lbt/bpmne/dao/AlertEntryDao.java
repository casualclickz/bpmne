package poc.lbt.bpmne.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.report.KycReport;

public interface AlertEntryDao extends JpaRepository<AlertEntry, Long> {
	public List<AlertEntry> findByCustomerReportId(Long customerReportId);
}
