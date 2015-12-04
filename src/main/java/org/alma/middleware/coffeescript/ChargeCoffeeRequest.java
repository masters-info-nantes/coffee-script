package org.alma.middleware.coffeescript;

/**
 * Created by Maxime on 04/12/2015.
 */
public class ChargeCoffeeRequest {

    private String token;
    private Integer nbCoffee;
    private String vendingMachine;

    public ChargeCoffeeRequest() {}


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getNbCoffee() {
        return nbCoffee;
    }

    public void setNbCoffee(Integer nbCoffee) {
        this.nbCoffee = nbCoffee;
    }

    public String getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(String vendingMachine) {
        this.vendingMachine = vendingMachine;
    }
}
