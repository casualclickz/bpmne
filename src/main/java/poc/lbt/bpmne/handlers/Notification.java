package poc.lbt.bpmne.handlers;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import poc.lbt.bpmne.commands.NotifyTaskOwner;
import poc.lbt.bpmne.events.TaskOwnerNotified;

@Aggregate(commandTargetResolver = "notificationCommandTargetResolver")
public class Notification {

	@AggregateIdentifier
	String id;

	public Notification() {		
	}
	
	@CommandHandler
	public Notification(NotifyTaskOwner command) {
		AggregateLifecycle.apply(new TaskOwnerNotified(
			command.getId(), command.getCustomerReport(), command.getKycReport()));
	}
	
	@EventSourcingHandler
    public void on(TaskOwnerNotified event) {
		this.id = event.getId();
    }
}