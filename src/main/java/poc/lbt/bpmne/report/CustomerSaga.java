package poc.lbt.bpmne.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.assertj.core.util.Arrays;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import poc.lbt.bpmne.commands.StartKycTask;
import poc.lbt.bpmne.dao.AlertEntryDao;
import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.events.CustomerAdded;
import poc.lbt.bpmne.events.KycTaskStarted;
import poc.lbt.bpmne.services.CustomerService;

@Saga//(sagaStore = "customerSagaStore")
@Service
@Transactional
public class CustomerSaga implements java.io.Serializable {
	
	@Autowired
	transient CustomerService customerService;
	
	@Autowired
	transient CommandGateway commandGateway;

	@Autowired
	transient RuntimeService runtimeService;

	@Autowired
	transient ProcessEngine processEngine;
	
	public CustomerSaga() {}
	
	/*
	@Autowired
    public CustomerSaga(CustomerDao customerDao, KycDao kycDao) {
        this.customerDao = customerDao;
        this.kycDao = kycDao;
    }
	*/
	
	@SagaEventHandler(associationProperty="id")//, keyName="sagaId")
	@StartSaga(forceNew=true)
	public void on(CustomerAdded event) {
		
		// check if exist as, strangely, saga is being called twice with the same event id
		CustomerReport foundCustomerReport = customerService.findCustomerReportByEventId(event.getId());
		if(foundCustomerReport == null) {
			String id = IdentifierFactory.getInstance().generateIdentifier();
			CustomerReport customerReport = customerService.addNewCustomerReport(
				event.getCustomerName(),
				event.getAddress(),
				event.getAge(),
				CustomerStatus.NEWCUSTOMER.getStatus(),
				event.getPhone(),
				event.getId(),
				id);
			SagaLifecycle.associateWith("id", id);
			StartKycTask startKycTask = new StartKycTask(id, customerReport);		
			FutureCallback<StartKycTask, Object> callback = new FutureCallback<>();
			commandGateway.send(startKycTask, callback);			
		}
	}

	@EndSaga
    @SagaEventHandler(associationProperty="id")
    public void on(KycTaskStarted event) {
		// check if exist as, strangely, saga is being called twice with the same event id.
		KycReport foundKycReport = customerService.findKycReportByEventId(event.getId());
		if(foundKycReport == null) {
	    	Map<String,Object> properties = new HashMap<String,Object>();
	    	properties.put("customerReportId", event.getCustomerReport().getId());
	    	properties.put("customerReportName", event.getCustomerReport().getCustomerName());
	    	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("KYCProcess",properties);	    	
	    	
	    	// list all tasks
			List<Task> tasks = processEngine.getTaskService().createTaskQuery().active().list();
	    	// list active tasks
	    	//List<Task> tasks = processEngine.getTaskService().createTaskQuery().active().processInstanceId(processInstance.getProcessInstanceId()).list();
			tasks.forEach(currentTask -> doCurrentTask(currentTask,processInstance,event));
			
			CustomerReport customerReport = event.getCustomerReport();
			customerReport.setStatus(CustomerStatus.AWAITINGKYC.getStatus());
			customerService.updateCustomerReport(customerReport);
			
			// should be externalized
			Long customerReportId = customerReport.getId();
			customerService.addNewAlertEntry(new Date(), true, customerReportId);			
		}
    }
    
	private void doCurrentTask(Task task, ProcessInstance processInstance, KycTaskStarted event) {		
		System.out.println("Task: " + task.getId() + " : " +task.getName() + " : " + task.getProcessDefinitionId() + " : " + (new SimpleDateFormat("dd-MMM-yyyy 'T' hh:mm:ss.S")).format(task.getCreateTime()) + " : " + task.getProcessInstanceId());
		// cache info about current task
		String taskId = task.getId();
		String processKey = task.getProcessDefinitionId();
		CustomerReport customerReport = event.getCustomerReport();
		Long customerReportId = customerReport.getId();
		String eventId = event.getId();
		
		customerService.addNewKycReport(taskId, processKey, customerReportId, eventId, true);
	}
}
