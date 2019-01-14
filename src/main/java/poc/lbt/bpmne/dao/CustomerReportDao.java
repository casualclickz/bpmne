package poc.lbt.bpmne.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poc.lbt.bpmne.report.CustomerReport;

public interface CustomerReportDao extends JpaRepository<CustomerReport, Long> {
	
	@Query("SELECT o from Customers o order by o.id desc")
	List<CustomerReport> getLastCustomer(Pageable pageable);
	
	public List<CustomerReport> findByEventId(String eventId);
}
