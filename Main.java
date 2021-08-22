package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static char[][] generateField(int nMines) {
        Random random = new Random();
        char[] str = new char[81];
        char[][] str2 = new char[9][9];
        int mineIndex;
        for (int i = 0; i < 81; i++)
            str[i] = '.';
        for (int i = 0; i < nMines; i++) {
            mineIndex = random.nextInt(81);
            if (str[mineIndex] == '.')
                str[mineIndex] = 'X';
            else
                i--;
        }
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                str2[i][j] = str[count];
                count++;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (str2[i][j] == '.')
                    str2[i][j] = countMines(str2, i, j);
            }
        }
        return str2;
    }

    public static void printField(char[][] field, boolean lose) {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < 9; j++) {
                if (field[i][j] == 'X' && !lose)
                    System.out.print('.');
                else
                    System.out.print(field[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static char countMines(char[][] field, int i, int j) {
        int n = 0;
        if (i < 8 && field[i + 1][j] == 'X')
            n++;
        if (j < 8 && field[i][j + 1] == 'X')
            n++;
        if (i < 8 && j < 8 && field[i + 1][j + 1] == 'X')
            n++;
        if (i > 0 && j > 0 && field[i - 1][j - 1] == 'X')
            n++;
        if (j > 0 && field[i][j - 1] == 'X')
            n++;
        if (i < 8 && j > 0 && field[i + 1][j - 1] == 'X')
            n++;
        if (i > 0 && field[i - 1][j] == 'X')
            n++;
        if (i > 0 && j < 8 && field[i - 1][j + 1] == 'X')
            n++;
        if (n == 0)
            return '.';
        return (char)(n + '0');
    }

    public static boolean checkWin(char[][] userField, char[][] field) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] == 'X' && userField[i][j] == '.')
                    return false;
            }
        }
        return true;
    }

    public static void doMine(char[][] userField, int x, int y) {
        if (userField[y - 1][x - 1] == '*')
            userField[y - 1][x - 1] = '.';
        else
            userField[y - 1][x - 1] = '*';
    }

    public static void doFree(char[][] userField, int x, int y) {
        if (countMines(userField, x, y) != '0')
            userField[y - 1][x - 1] = '/';
        else
            userField[y - 1][x - 1] = countMines(userField, x, y);
        doFree(userField, x + 1, y);
        doFree(userField, x, y + 1);
        doFree(userField, x - 1, y);
        doFree(userField, x, y - 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many mines do you want on the field?");
        int nMines = scanner.nextInt();
        char[][] emptyField = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                emptyField[i][j] = '.';
            }
        }
        printField(emptyField, false);
        int x;
        int y;
        String command;
        char[][] field;
        char[][] userField;
        while (true) {
            System.out.println("Set/unset mines marks or claim a cell as free:");
            x = scanner.nextInt();
            y = scanner.nextInt();
            command = scanner.nextLine();
            field = generateField(nMines);
            userField = field.clone();
            if (command.equals("mine"))
                doMine(userField, x, y);
            if (command.equals("free")) {
                if (userField[y - 1][x - 1] == 'X') {
                    printField(userField, true);
                    System.out.println("You stepped on a mine and failed!");
                    break;
                }
                doFree(userField, x, y);
            }
            printField(userField, false);
            if (checkWin(userField, field)) {
                System.out.println("Congratulations! You found all the mines!");
                break;
            }
        }
    }
}
