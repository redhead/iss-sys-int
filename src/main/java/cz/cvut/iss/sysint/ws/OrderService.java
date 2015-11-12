package cz.cvut.iss.sysint.ws;

import javax.jws.WebService;


@WebService
public interface OrderService {

    public void makeOrder(cz.cvut.iss.sysint.ws.Order order);

}