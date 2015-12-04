package org.alma.middleware.coffeescript;

/**
 * Created by Maxime on 04/12/2015.
 */
public class BuyCoffeeRequest {

   private String token;
    private String vendingMachine;

    public BuyCoffeeRequest() {}


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(String vendingMachine) {
        this.vendingMachine = vendingMachine;
    }
}
