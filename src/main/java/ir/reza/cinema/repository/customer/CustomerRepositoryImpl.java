package ir.reza.cinema.repository.customer;

import ir.reza.cinema.entity.Customer;
import ir.reza.cinema.repository.base.BaseRepositoryImpl;

public class CustomerRepositoryImpl
        extends BaseRepositoryImpl<Customer, Long>
        implements CustomerRepository {

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }

    @Override
    protected Long getEntityId(Customer customer) {
        return customer.getId();
    }

    @Override
    protected void updateFields(
            Customer existingCustomer,
            Customer newCustomer
    ) {
        existingCustomer.setName(newCustomer.getName());
        existingCustomer.setEmail(newCustomer.getEmail());
    }
}