package view;

import java.util.Scanner;

public class Concole implements View {
    @Override
    public void write(final String massage) {
        System.out.println(massage);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}