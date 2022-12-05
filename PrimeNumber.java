package testCodeForDNQDec;

import java.util.Scanner;

@FunctionalInterface
interface CheckIfPrime{
    void show();
}

public class PrimeNumber {
    public static void main(String [] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("enter the number you want to check if its a prime number, the number MUST BE GREATER THAN 500");
        int input = scan.nextInt();
        while (input <= 500){
            System.out.println("not greater than 500");
            input = scan.nextInt();
        }

        int finalInput = input;

        CheckIfPrime number = () -> {
            if (finalInput %2 == 0){ System.out.println("false, its not a prime"); }
            else { System.out.println("true, it is a prime"); }
        };

        number.show();

    }
}
