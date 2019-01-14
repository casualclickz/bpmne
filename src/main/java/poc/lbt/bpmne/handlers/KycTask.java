package poc.lbt.bpmne.handlers;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import poc.lbt.bpmne.commands.StartKycTask;
import poc.lbt.bpmne.events.KycTaskStarted;

@Aggregate(commandTargetResolver = "kycCommandTargetResolver")
public class KycTask {

	@AggregateIdentifier
	String id;

	public KycTask() {		
	}
	
	@CommandHandler
	public KycTask(StartKycTask command) {
		AggregateLifecycle.apply(new KycTaskStarted(
			command.getId(), command.getCustomerReport()));
	}
	
	/*@CommandHandler
	public void kycTask(StartKycTask command) {
		AggregateLifecycle.apply(new KycTaskStarted(
			command.getId(), command.getCustomerReport()));
	}*/
	
	@EventSourcingHandler
    public void on(KycTaskStarted event) {
		this.id = event.getId();
    }
}