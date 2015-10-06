package cz.cvut.iss.sysint.ws;

import cz.cvut.iss.sysint.model.Order;

import javax.jws.WebService;


@WebService
public interface OrderService {

    public void makeOrder(Order order);

}