package ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 *
 * @author apprentice
 */
public class ConsoleIO {
    Scanner sc = new Scanner(System.in);
    
    public void printString(String prompt) {
        System.out.print(prompt);
    }
    
    public void printStringln(String prompt) {
        System.out.println(prompt);
    }
    
    public String getString(String prompt) { 
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                return input;
            } else {
                errorEmpty();
            }
        }
    }
    
    public String getString(String prompt, String[] acceptableInputs) {
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            String input = sc.nextLine();
            if (input.isEmpty()) {
                errorEmpty();
            }
            for (String acceptable: acceptableInputs) {
                if (input.equalsIgnoreCase(acceptable)) {
                    return input;
                }
            }
            errorString();
        }
    }
    
    public String getString(String prompt, String[] acceptableInputs, String errorMessage) {
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            String input = sc.nextLine();
            if (input.isEmpty()) {
                errorEmpty();
            }
            for (String acceptable: acceptableInputs) {
                if (input.equalsIgnoreCase(acceptable)) {
                    return input;
                }
            }
            printStringln(errorMessage);
        }
    }
    
    public char getCharacter(String prompt, String acceptableInputs) { 
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                if (input.length() == 1 && acceptableInputs.contains(input)) {
                    return input.charAt(0);
                } else {
                errorString();
                }
            } else {
                errorEmpty();
            }
        }
    }
    
    public char getCharacter(String prompt, String acceptableInputs, String errorMessage) { 
        while (true) {
            System.out.println(prompt);
            System.out.print("> ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                if (input.length() == 1 && acceptableInputs.contains(input)) {
                    return input.charAt(0);
                } else {
                printStringln(errorMessage);
                }
            } else {
                errorEmpty();
            }
        }
    }

    public int getInteger(String prompt) {
        while (true) {
            String input = getString(prompt);
            if (isInteger(input)) {
                int number = Integer.parseInt(input);
                return number;
            }
        }
    }
    
    public int getInteger(String prompt, int min, int max) {
        while (true) {
            String input = getString(prompt);
            if (isInteger(input)) {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    public int getIntegerWithMin(String prompt, int min) {
        while (true) {
            String input = getString(prompt);
            if (isInteger(input)) {
                int number = Integer.parseInt(input);
                if (number >= min) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            errorNumber();
            return false;
        }
        return true;
    }
    
    public double getDouble(String prompt) {
        while (true) {
            String input = getString(prompt);
            if (isDouble(input)) {
                double number = Double.parseDouble(input);
                return number;
            }
        }
    }
    
    public double getDouble(String prompt, double min, double max) {
        while (true) {
            String input = getString(prompt);
            if (isDouble(input)) {
                double number = Double.parseDouble(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    public double getDoubleWithMin(String prompt, double min) {
        while (true) {
            String input = getString(prompt);
            if (isDouble(input)) {
                double number = Double.parseDouble(input);
                if (number >= min) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    private boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            errorNumber();
            return false;
        }
        return true;
    }
    
    public float getFloat(String prompt) {
        while (true) {
            String input = getString(prompt);
            if (isFloat(input)) {
                float number = Float.parseFloat(input);
                return number;
            }
        }
    }
    
    public float getFloat(String prompt, float min, float max) {
        while (true) {
            String input = getString(prompt);
            if (isFloat(input)) {
                float number = Float.parseFloat(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    public float getFloatWithMin(String prompt, float min) {
        while (true) {
            String input = getString(prompt);
            if (isFloat(input)) {
                float number = Float.parseFloat(input);
                if (number >= min) {
                    return number;
                } else {
                    errorNumber();
                }
            }
        }
    }
    
    private boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
        } catch (NumberFormatException e) {
            errorNumber();
            return false;
        }
        return true;
    }
    
    private void errorNumber() {
        printStringln("That is not a valid number!");
    }
    
    private void errorEmpty() {
        printStringln("You did not enter anything!");
    }  
    
    private void errorString() {
        printStringln("That is not an available option!");
    }
    
    public String enterToContinue() {
        System.out.println("Press enter to continue.");
        String input = sc.nextLine();
        return input;
    }
}