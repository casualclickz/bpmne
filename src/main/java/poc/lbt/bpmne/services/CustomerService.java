package poc.lbt.bpmne.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.lbt.bpmne.dao.AlertEntryDao;
import poc.lbt.bpmne.dao.CustomerReportDao;
import poc.lbt.bpmne.dao.KycReportDao;
import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.report.CustomerReport;
import poc.lbt.bpmne.report.KycReport;

@Service
public class CustomerService {

	@Autowired
	private CustomerReportDao customerReportDao;
	
	@Autowired
	private KycReportDao kycReportDao;
	
	@Autowired
	private AlertEntryDao alertEntryDao;
	
	public CustomerReport addNewCustomerReport(String customerName, String address, Long age, String status, String phone, String eventId, String associationId) {
		CustomerReport customerReport = new CustomerReport(customerName,address,age,status,phone,eventId,associationId);
		customerReportDao.save(customerReport);
		return customerReport;
	}
	
	public KycReport addNewKycReport(String kycTaskAggregateId, String kycTaskId, Long customerReportId, String taskUrl, boolean active) {
		KycReport kycReport = new KycReport(kycTaskAggregateId, kycTaskId, customerReportId, taskUrl, active);
		kycReportDao.save(kycReport);
		return kycReport;
	}
	
	public long getCountOfKycReport() {
		return kycReportDao.count();
	}
	
	public long getCountOfCustomerReport() {
		return customerReportDao.count();
	}
	
	public Optional<CustomerReport> findCustomerReportById(Long id) {
		return customerReportDao.findById(id);
	}
	
	public Optional<KycReport> findKycReportById(Long id) {
		return kycReportDao.findById(id);
	}
	
	public Optional<AlertEntry> findAlertEntryById(Long id) {
		return alertEntryDao.findById(id);
	}
	
	public CustomerReport findCustomerReportByEventId(String eventId) {
		List<CustomerReport> customerReports = customerReportDao.findByEventId(eventId);
		if(customerReports.isEmpty()) 
			return null; 
		else
			return customerReports.get(0);
	}
	
	public KycReport findKycReportByEventId(String eventId) {
		List<KycReport> kycReports = kycReportDao.findByEventId(eventId);
		if(kycReports.isEmpty()) 
			return null; 
		else
			return kycReports.get(0);
	}
	
	public List<AlertEntry> findAlertEntryByCustomerReportId(Long customerReportId) {
		return alertEntryDao.findByCustomerReportId(customerReportId);
	}
	
	public List<KycReport> findKycReportByCustomerReportId(Long customerReportId) {
		return kycReportDao.findByCustomerReportId(customerReportId);
	}
	
	public boolean customerReportDoesNotExists(String eventId) {
		return customerReportDao.findByEventId(eventId).isEmpty();
	}
	
	public boolean kycReportDoesNotExists(String eventId) {
		return kycReportDao.findByEventId(eventId).isEmpty();
	}
	
	public void updateCustomerReport(CustomerReport customerReport) {
		customerReportDao.save(customerReport);
	}
	
	public List<CustomerReport> findAllCustomerReports() {
		return customerReportDao.findAll();
	}
	
	public AlertEntry addNewAlertEntry(Date scheduledOn, boolean status, Long customerReportId) {
		AlertEntry alertEntry = new AlertEntry(scheduledOn, status, customerReportId);
		alertEntryDao.save(alertEntry);
		return alertEntry;
	}
	
	public void updateAlertEntry(Long customerReportId) {
		List<AlertEntry> alertEntrys = findAlertEntryByCustomerReportId(customerReportId);
		alertEntrys.forEach(proAlertEntry -> doUpdateAlertEntry(proAlertEntry, alertEntryDao));
	}
	
	public void updateKycReport(Long customerReportId) {
		List<KycReport> kycReports = findKycReportByCustomerReportId(customerReportId);
		kycReports.forEach(proKycReport -> doUpdateKycReport(proKycReport, kycReportDao));
	}
	
	private void doUpdateAlertEntry(AlertEntry alertEntry, AlertEntryDao alertEntryDao) {
		alertEntry.setActive(false);
		alertEntryDao.save(alertEntry);
	}
	
	private void doUpdateKycReport(KycReport kycReport, KycReportDao kycReportDao) {
		kycReport.setActive(false);
		kycReportDao.save(kycReport);
	}
}
