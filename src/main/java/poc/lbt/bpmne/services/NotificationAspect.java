package poc.lbt.bpmne.services;

import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import poc.lbt.bpmne.dao.AlertEntryDao;
import poc.lbt.bpmne.domain.AlertEntry;
import poc.lbt.bpmne.events.KycTaskStarted;
import poc.lbt.bpmne.report.CustomerReport;

@Aspect
@Configuration
public class NotificationAspect {
	
	@Autowired
	AlertEntryDao alertEntryDao;
	
	//@Pointcut("execution (* poc.lbt.bpmne.report.*.*(poc.lbt.bpmne.events.KycTaskStarted) ) ")
	public void taskScheduledMethod() {}
	
	//@Pointcut("execution (java.util.List<poc.lbt.bpmne.report.CustomerReport> poc.lbt.bpmne.services.*.findAllCustomerReports() ) ")
	public void allCustomerReportsReadMethod() {}
	
	//@Around("taskScheduledMethod()")
	public Object whenTaskIsScheduled(ProceedingJoinPoint method) throws Throwable {
	
		Object returnValue = method.proceed();
		
		List<Object> objs = Arrays.asList(method.getArgs());
		if(!objs.isEmpty()) {
			Object o = objs.get(0);
			if(o instanceof KycTaskStarted) {
				KycTaskStarted kycTaskStarted = (KycTaskStarted)o;
				Long customerReportId = kycTaskStarted.getCustomerReport().getId();
				if(alertEntryDao.findById(customerReportId) == null) {
					alertEntryDao.save(new AlertEntry(new Date(), true, customerReportId));
				}
			}
		}		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	//@Around("allCustomerReportsReadMethod()")
	public Object whenAllCustomerReportsRead(ProceedingJoinPoint method) throws Throwable {
		List<CustomerReport> returnValue = (List<CustomerReport>)method.proceed();
		returnValue.forEach(currentCustomerReport -> doReturnValue(currentCustomerReport));		
		return returnValue;
	}
	
	private void doReturnValue(CustomerReport customerReport) {
		if(alertEntryDao.findById(customerReport.getId()) != null) {
			customerReport.setRaiseKycNotification(true);
		}
	}
}
