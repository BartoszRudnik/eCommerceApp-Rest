package ecommerce.service;

import ecommerce.dao.CustomerRepository;
import ecommerce.dto.Purchase;
import ecommerce.dto.PurchaseResponse;
import ecommerce.entities.Customer;
import ecommerce.entities.Order;
import ecommerce.entities.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository){

        this.customerRepository = customerRepository;

    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        Order order = purchase.getOrder();
        
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();

        orderItems.forEach(order::addOrderItem);

        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        Customer customer = purchase.getCustomer();

        Customer customerFromDB = customerRepository.findByEmail(customer.getEmail());

        if(customerFromDB != null){

            customer = customerFromDB;

        }

        customer.addOrder(order);

        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);

    }

    private String generateOrderTrackingNumber() {

        return UUID.randomUUID().toString();

    }

}
