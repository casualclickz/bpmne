package poc.lbt.bpmne.handlers;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateRoot;
import org.axonframework.spring.stereotype.Aggregate;

import poc.lbt.bpmne.commands.AddCustomer;
import poc.lbt.bpmne.events.CustomerAdded;

@Aggregate(commandTargetResolver = "customerCommandTargetResolver")
public class Customer {

	@AggregateIdentifier
	String id;

	public Customer() {		
	}
	
	@CommandHandler
	public Customer(AddCustomer command) {
		AggregateLifecycle.apply(new CustomerAdded(
			command.getId(), command.getCustomerName(), command.getAddress(), command.getAge(), command.getPhone()));
	}
	
	@EventSourcingHandler
    public void on(CustomerAdded event) {
		this.id = event.getId();
    }
}