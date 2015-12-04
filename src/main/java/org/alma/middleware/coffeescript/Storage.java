package org.alma.middleware.coffeescript;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.io.Serializable;
import java.security.Identity;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Maxime on 04/12/2015.
 */
public class Storage {

    private static final String MAP_COFFEES = "coffees";
    private static final String MAP_TOKENS = "tokens";

    private DB db;
    private Map<String, Integer> coffees;
    private Map<String,TimeoutString> tokens;

    public Storage() {
        this.db = DBMaker.fileDB(new File("storage.db"))
                .closeOnJvmShutdown()
                .transactionDisable()// no need to commit to save
                .make();

        if(this.db.exists(MAP_COFFEES)) {
            this.coffees = this.db.treeMap(MAP_COFFEES);
        } else {
            this.coffees = this.db.treeMapCreate(MAP_COFFEES)
                    .keySerializer(Serializer.STRING)
                    .makeOrGet();
        }
        if(this.db.exists(MAP_TOKENS)) {
            this.tokens = this.db.treeMap(MAP_TOKENS);
        } else {
            this.tokens = this.db.treeMapCreate(MAP_TOKENS)
                    .keySerializer(Serializer.STRING)
                    .makeOrGet();
        }
    }

    public Integer getCoffee(String user) {
        return this.coffees.get(user);
    }

    public Integer putCoffee(String user, Integer coffee) {
        return this.coffees.put(user, coffee);
    }

    public String getNameFromToken(String token) {
        TimeoutString str = this.tokens.get(token);
        if(str != null) {
            Calendar now = Calendar.getInstance();//.getTimeInMillis()
            Calendar timeoutDate = Calendar.getInstance();
            timeoutDate.setTimeInMillis(str.timeoutDate);//.getTimeInMillis()
            if(now.before(timeoutDate)) {
                return str.value;
            } else {
                return null;
            }
        }
        return null;
    }

    public String putToken(String token, String name) {
        Calendar now = Calendar.getInstance();
        Calendar timeoutDate = Calendar.getInstance();
        timeoutDate.add(Calendar.MINUTE,1);
        TimeoutString str = new TimeoutString(name,timeoutDate.getTimeInMillis());
        TimeoutString previousValue = this.tokens.put(token,str);
        if(previousValue != null) {
            Calendar previousTimeoutDate = Calendar.getInstance();
            previousTimeoutDate.setTimeInMillis(previousValue.timeoutDate);
            if(now.before(previousTimeoutDate)) {
                return previousValue.value;
            } else {
                return null;
            }
        }
        return null;
    }

    public void close() {
        this.db.close();
    }

    private static class TimeoutString implements Serializable {
        String value;
        long timeoutDate;

        public TimeoutString(String value, long timeoutDate) {
            this.value = value;
            this.timeoutDate = timeoutDate;
        }
    }

}

