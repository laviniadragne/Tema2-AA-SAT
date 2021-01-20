// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;
    // TODO: define necessary variables and/or data structures

    String answerTask2 = "";
    List<Integer> listTask2 = new ArrayList<>();


    private int a[][];
    private int n;
    private int m;


    private int numVars;
    private int vect[];

    private List<Integer> outNodes;

    private int dimClique;

    private int complement[][];

    public int[][] getA() {
        return a;
    }

    public void setA(int[][] a) {
        this.a = a;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getDimClique() {
        return dimClique;
    }

    public void setDimClique(int dimClique) {
        this.dimClique = dimClique;
    }

    public void fullZeros(int matrix [][]) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);

        // TODO: implement a way of successively querying the oracle (using Task2) about various arrest numbers until you
        //  find the minimum

        readProblemData();

        constructComplement();

        applyTask2(task2Solver);

        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes

        Scanner s = new Scanner(new BufferedReader(new FileReader(inFilename)));

        // Citesc valorile pentru numarul de noduri, muchii si culori
        int n = s.nextInt();
        int m = s.nextInt();

        // Setez dimensiunile
        a = new int[n + 1][n + 1];
        setN(n);
        setM(m);
        setA(a);

        // Imi declar o structura de date si umplu matricea cu 0
        fullZeros(a);

        // Completez matricea de adiacenta a grafului
        while (s.hasNext()) {
            if (s.hasNextInt())
            {
                int x = s.nextInt();
                int y = s.nextInt();
                getA()[x][y] = 1;
                getA()[y][x] = 1;
            }
        }

        s.close();
    }

    /**
     * Construieste graful complementar celui primit
     */
    public void constructComplement() {

        // Setez dimensiunile
        complement = new int[n + 1][n + 1];

        fullZeros(complement);

        // Construiesc complementara
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (a[i][j] == 0 && i != j) {
                    complement[i][j] = 1;
                    complement[j][i] = 1;
                }
            }
        }

//        // Afisez complementul
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                System.out.print(a[i][j] + " ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//
//
//        // Afisez complementul
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                System.out.print(complement[i][j] + " ");
//            }
//            System.out.println();
//        }

    }

    public void applyTask2(Task2 task2Solver) throws IOException, InterruptedException {


        // Initial dimensiunea maximala a clicii este n, numarul de noduri
        int k = n;
        // Nu am gasit inca o clica
        int ok = 0;

        // Cat timp o clica poate avea dimensiunea mai mare de 2
        // si nu am gasit inca o clica
        while (k >= 2 && ok == 0) {
            // Scriu input-ul pentru metoda 1
            reduceToTask2(k);

            // Rezolv task-ul 2
            task2Solver.solve();

            extractAnswerFromTask2(k);

            // Am gasit o clica
            if (answerTask2.equals("True")) {
                ok = 1;
            }
            k--;
        }
        k++;
        this.dimClique = k;

    }

    // Construiesc input-ul pentru Task-ul 2
    public void reduceToTask2(int k) throws IOException {
        // TODO: reduce the current problem to Task2

        // Scrierea in fisierul de input al task-ului 2
        FileWriter myWriter = new FileWriter(task2InFilename);

        // Restul de muchii pana la un graf complet
        int muchiiComplement = getN()*(getN() - 1) / 2 - getM();

        myWriter.write(String.valueOf(this.n));
        myWriter.write(" ");
        myWriter.write(String.valueOf(muchiiComplement));
        myWriter.write(" ");
        myWriter.write(String.valueOf(k));
        myWriter.write("\n");

        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (complement[i][j] == 1) {
                    myWriter.write(String.valueOf(i));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(j));
                    myWriter.write(" ");
                    myWriter.write("\n");
                }
            }
        }

        myWriter.close();


    }

    // Extrag intr-un vector raspunsul din fisierul de output al task-ului 2
    public void extractAnswerFromTask2(int k) throws FileNotFoundException {
        // TODO: extract the current problem's answer from Task2's answer

        // Deschid fisierul de output al task-ului 2
        Scanner s = new Scanner(new BufferedReader(new FileReader(task2OutFilename)));

        listTask2.clear();

        // Retin raspunsul
        this.answerTask2 = s.nextLine();
        if (this.answerTask2.equals("True")) {
            for (int i = 1; i <= k; i++)
                listTask2.add(s.nextInt());
        }

        s.close();

    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

        for (int i = 1; i <= n; i++) {
            if (!listTask2.contains(i)) {
                myWriter.write(String.valueOf(i));
                myWriter.write(" ");
            }
        }
        myWriter.close();

    }
}
