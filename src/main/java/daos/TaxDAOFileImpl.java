/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author apprentice
 */
public class TaxDAOFileImpl implements TaxDAO {

    HashMap<String, Double> taxMap;
    private final String DELIMITER = "::";
    private final String FILE_PATH = "References/State_Taxes.txt";

    public TaxDAOFileImpl() {
        taxMap = new HashMap<>();
        loadTaxMap();
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

    private void loadTaxMap() {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_PATH)));
            while (sc.hasNextLine()) {
                String[] taxArray = sc.nextLine().split(DELIMITER);
                taxMap.put(taxArray[0], Double.parseDouble(taxArray[1]));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error loading tax table");
        }
    }
}
