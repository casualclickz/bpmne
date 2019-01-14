package poc.lbt.bpmne;

import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.modelling.command.AnnotationCommandTargetResolver;
import org.axonframework.modelling.command.CommandTargetResolver;
import org.axonframework.modelling.saga.ResourceInjector;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import poc.lbt.bpmne.commands.AddCustomer;
import poc.lbt.bpmne.commands.NotifyTaskOwner;
import poc.lbt.bpmne.commands.StartKycTask;
import poc.lbt.bpmne.dao.AlertEntryDao;
import poc.lbt.bpmne.dao.CustomerReportDao;
import poc.lbt.bpmne.report.CustomerReport;
import poc.lbt.bpmne.report.CustomerSaga;
import poc.lbt.bpmne.report.KycReport;
import poc.lbt.bpmne.services.CustomerService;
import poc.lbt.bpmne.services.NotificationAspect;

@Configuration
public class BpmneConfig {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerSaga customerSaga;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	ProcessEngine processEngine;
    
    @Bean
    public CommandTargetResolver customerCommandTargetResolver() {
    	AnnotationCommandTargetResolver annotationCommandTargetResolver = new AnnotationCommandTargetResolver();
    	annotationCommandTargetResolver.resolveTarget(
    		GenericCommandMessage.asCommandMessage(new AddCustomer("","","",new Long("0"),"")));
        return annotationCommandTargetResolver;
    }
    
    @Bean
    public CommandTargetResolver kycCommandTargetResolver() {
    	AnnotationCommandTargetResolver annotationCommandTargetResolver = new AnnotationCommandTargetResolver();
    	annotationCommandTargetResolver.resolveTarget(
    		GenericCommandMessage.asCommandMessage(new StartKycTask("", new CustomerReport("","",0L,"","","",""))));
        return annotationCommandTargetResolver;
    }
    
    @Bean
    public CommandTargetResolver notificationCommandTargetResolver() {
    	AnnotationCommandTargetResolver annotationCommandTargetResolver = new AnnotationCommandTargetResolver();
    	annotationCommandTargetResolver.resolveTarget(
    		GenericCommandMessage.asCommandMessage(new NotifyTaskOwner("", new CustomerReport("","",0L,"","","",""), new KycReport("","",0L,"",true))));
        return annotationCommandTargetResolver;
    }
    
    
    private SagaStore customerSagaStore(EntityManagerProvider entityManagerProvider) {
    	SagaStore sagaStore = JpaSagaStore.builder().entityManagerProvider(entityManagerProvider).build();
    	return sagaStore;
    }
    
    @Bean
    public org.axonframework.config.Configuration customerSagaConfiguration(EntityManagerProvider entityManagerProvider, TransactionManager transactionManager) {
    	
    	SagaStore<CustomerSaga> sagaStore = customerSagaStore(entityManagerProvider);
    	
    	Configurer configurer = DefaultConfigurer.defaultConfiguration();
        configurer
        	.eventProcessing(eventProcessingConfigurer -> eventProcessingConfigurer
        	.registerSaga(CustomerSaga.class, sagaConfigurer -> sagaConfigurer
        	.configureSagaStore(c -> sagaStore)));

        configurer.registerComponent(EntityManagerProvider.class, c -> entityManagerProvider);
        configurer.registerComponent(TransactionManager.class, c -> transactionManager);
        configurer.registerComponent(poc.lbt.bpmne.services.CustomerService.class, c -> customerService);
        configurer.registerComponent(RuntimeService.class, c -> runtimeService);
        configurer.registerComponent(ProcessEngine.class, c -> processEngine);
        //configurer.registerComponent(NotificationAspect.class, c -> notificationAspect);
        //configurer.registerComponent(CustomerService.class, c -> customerService);
        
        org.axonframework.config.Configuration configuration = configurer.buildConfiguration();
        ResourceInjector resourceInjector = configuration.resourceInjector();
        resourceInjector.injectResources(customerSaga);
        
        configuration.start();
    	return configuration;
    }
}
