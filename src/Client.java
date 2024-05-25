package operation;
import java.util.Scanner;

public class Client
{
    public static void main (String... args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("(C) Gabriel Calizaya, 2020");
        System.out.println("Type in your differential equation.");
        
        System.out.println("\nRules: ");
            System.out.println(" - Only use doubles in your formula (2.0 instead of 2).");
            System.out.println(" - Only x and y variables are accepted.");
            System.out.println(" - Supported operations: +, -, *, /, ^, sin, cos, tan, ln, log.");
            System.out.println(" - Implicit operators are not allowed (2x should be 2.0*x, and (x+1)(x+2) should be (x+1)*(x+2)");
            System.out.println(" - Note: Parentheses and order of operations are always respected.");
            
        System.out.print("\ndy/dx = ");
        String s =  sc.nextLine();
        System.out.print("\nStepsize = ");
        double sS =  sc.nextDouble();
        System.out.print("Initial x value = ");
        double iX =  sc.nextDouble();
        System.out.print("Initial y value = ");
        double iY =  sc.nextDouble();
        System.out.print("Target x value = ");
        double pX =  sc.nextDouble();
        sc.nextLine();
        String mode = "";
        
        System.out.print("Recursion (r) or Iteration (i)?: ");
        
        outer:
        while (true) {
            switch (sc.nextLine()) {
                case "r": 
                    mode = "r";
                    break outer;
                case "i":
                    mode = "i";
                    break outer;
                default:
                    System.out.println ("Error\n");
                    System.out.print("Recursion (r) or Iteration (i)?: ");
                    break;
            }
        }
        
        System.out.println("\nMethods available (from least to most accurate): \n - Euler (eu) \n - Enhanced Euler (+eu) \n - 4th order Runge Kutta (rk) \n");
        System.out.print("Method: ");
        double value = 0;
        
        outer:
        while (true) {
            if (mode.equals("r")) {
                switch (sc.nextLine()) {
                    case "eu":
                        value = operation.RecursionTools.rClassicEuler(s,sS,iX,iY,pX);
                        break outer;
                    case "+eu":
                        value = operation.RecursionTools.rEnhancedEuler(s,sS,iX,iY,pX);
                        break outer;
                    case "rk":
                        value = operation.RecursionTools.rClassicRungeKutta(s,sS,iX,iY,pX);
                        break outer;
                    default:
                        System.out.println ("Error\n");
                        System.out.print("Method: ");
                        break;
                }
            }
            
            else if (mode.equals("i")) {
                switch (sc.nextLine()) {
                    case "eu":
                        value = operation.RecursionTools.classicEuler(s,sS,iX,iY,pX);
                        break outer;
                    case "+eu":
                        value = operation.RecursionTools.enhancedEuler(s,sS,iX,iY,pX);
                        break outer;
                    case "rk":
                        value = operation.RecursionTools.classicRungeKutta(s,sS,iX,iY,pX);
                        break outer;
                    default:
                        System.out.println ("Error\n");
                        System.out.print("Method: ");
                        break;
                }
            }
        }
        System.out.println("\ny(" + pX + ") ~= " + value);
    }
}
