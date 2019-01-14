package poc.lbt.bpmne.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.GenericCommandResultMessage;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import poc.lbt.bpmne.commands.AddCustomer;
import poc.lbt.bpmne.dao.AlertEntryDao;
import poc.lbt.bpmne.dao.CustomerReportDao;
import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.handlers.Customer;
import poc.lbt.bpmne.report.CustomerReport;
import poc.lbt.bpmne.report.CustomerStatus;
import poc.lbt.bpmne.report.KycReport;
import poc.lbt.bpmne.services.CustomerService;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	CommandGateway commandGateway;
	
	@Autowired
	AlertEntryDao alertEntryDao;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	ProcessEngine processEngine;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView customers(HttpServletRequest request) {
		
		long count = customerService.getCountOfCustomerReport();
		String query = request.getQueryString() != null ? request.getQueryString().trim() : "";
		if(count < 1 || query.indexOf("add") > -1) {
			CustomerReport customer = new CustomerReport("John Doe","5, Joel Ogunnaike Street, Ikeja",30L,CustomerStatus.NEWCUSTOMER.getStatus(), "08012345678", "","");
			return new ModelAndView("customer", "customer", customer);			
		} else {
			List<CustomerReport> customers = customerService.findAllCustomerReports();
			customers.forEach(currentCustomerReport -> applyAlert(currentCustomerReport));
			ModelAndView mav = new ModelAndView();
			mav.addObject("customers", customers);
			return mav;
		}
	}
	
	private void applyAlert(CustomerReport customerReport) {
		Long customerReportId = customerReport.getId();
		List<AlertEntry> alertEntrys = customerService.findAlertEntryByCustomerReportId(customerReportId);
		alertEntrys.forEach(proAlertEntry -> doApplyAlert(proAlertEntry,customerReport,customerService));
	}
	
	private void doApplyAlert(AlertEntry alertEntry, CustomerReport customerReport, CustomerService customerService) {
		if(alertEntry.isActive() == true) {
			customerReport.setRaiseKycNotification(true);
		}		
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView customers(@ModelAttribute CustomerReport customerReport) {
		AddCustomer addCustomer = new AddCustomer(
			IdentifierFactory.getInstance().generateIdentifier(), customerReport.getCustomerName(), customerReport.getAddress(), customerReport.getAge(), customerReport.getPhone());
		FutureCallback<AddCustomer, Object> callback = new FutureCallback<>();
		commandGateway.send(addCustomer, callback);
		// mimick near real time eventual consistency
		try { Thread.sleep(1000); } catch (Exception e) {}
				
		return new ModelAndView("redirect:/customers");
	}
	
	/*private CompletableFuture doCallBack(CommandResultMessage commandResultMessage, Throwable exception) {
		
		return null;
	}*/

	@RequestMapping(value="/customer/{customerid}/resolveKYC", method = RequestMethod.POST)
	public ModelAndView customer(@PathVariable("customerid")String customerReportid, HttpServletRequest request) {
		
		CustomerReport customerReport = customerService.findCustomerReportById(new Long(customerReportid)).get();		
		KycReport kycReport = customerService.findKycReportByEventId(customerReport.getAssociationId());
		
		String kycDecision = request.getParameter("kycDecision") != null ? request.getParameter("kycDecision").trim() : "";
		if(kycDecision.isEmpty()) {
			return new ModelAndView("resolution-center", "kycReport", kycReport);
		} else {
			if("Yes".equals(kycDecision)) {
				String taskId = kycReport.getTaskId();				
				TaskService taskService = processEngine.getTaskService();				
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult(); // actual task object.
				taskService.claim(taskId, "IdentifiedCustomerAgent");
				taskService.setVariable(taskId, "passedKYC", "Yes");
				taskService.complete(taskId);
				customerReport.setStatus(CustomerStatus.VERIFIEDCUSTOMER.getStatus());
				customerService.updateCustomerReport(customerReport);
				// should be applied externally
				customerService.updateKycReport(customerReport.getId());
				customerService.updateAlertEntry(customerReport.getId());
			}
			return new ModelAndView("redirect:/customers");
		}
	}
}
