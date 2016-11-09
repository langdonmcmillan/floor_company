/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author apprentice
 */
public class TaxDAOInMem implements TaxDAO {
    HashMap<String, Double> taxMap;

    public TaxDAOInMem() {
        taxMap = new HashMap<>();
        setMap();
    }
    @Override
    public double getRate(String state) {
        double rate = -10;
        if (taxMap.containsKey(state)) {
            rate = taxMap.get(state);
        }
        return rate;
    }
    
    @Override
    public List<String> getAllStates() {
        List<String> stateList = taxMap.keySet().stream().collect(Collectors.toList());
        return stateList;
    }
    
    private void setMap () {
        taxMap.put("OH", 6.25);
        taxMap.put("PA", 6.75);
        taxMap.put("MI", 5.75);
        taxMap.put("IN", 6.00);
    }
   
}
