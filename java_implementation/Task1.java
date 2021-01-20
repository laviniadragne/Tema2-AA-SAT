// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {
    // TODO: define necessary variables and/or data structures

    /**
     * Matricea in care retin graful cu muchii adiacente
     * (familiile de mafioti)
     */
        private int a[][];
        private int n;
        private int m;
        private int k;

        private String answer;
        private int numVars;
        private int vect[];

        private List<Integer> outputBuffer;

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

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }

        public void fullZeros() {
           for (int i = 0; i < n; i ++) {
               for (int j = 0; j < n; j++) {
                   a[i][j] = 0;
               }
           }
        }


    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes

        Scanner s = new Scanner(new BufferedReader(new FileReader(inFilename)));

        // Citesc valorile pentru numarul de noduri, muchii si culori
        int n = s.nextInt();
        int m = s.nextInt();
        int k = s.nextInt();

        // Setez dimensiunile
        a = new int[n + 1][n + 1];
        setN(n);
        setM(m);
        setK(k);
        setA(a);

        // Imi declar o structura de date si umplu matricea cu 0
        fullZeros();

        // Completez matricea de adiacenta a grafului
        while (s.hasNext()) {
            if (s.hasNextInt())
            {
                int x = s.nextInt();
                int y = s.nextInt();
                // Daca perechea sa e deja 1, nu mai e nevoie
                // sa verificam si adiacenta
                if (getA()[y][x] != 1) {
                    getA()[x][y] = 1;
                }
            }
        }

        s.close();

    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        // Scrierea in fisierul oracolului
        FileWriter myWriter = new FileWriter(oracleInFilename);

//        readProblemData();

        myWriter.write("p cnf ");
        // Numarul total de var
        myWriter.write(String.valueOf(getN() * getK()));
        myWriter.write(" ");
        // Numarul de clauze
        int clauze = getK() * getN() + (getK() * (getK() + 1))/2 + getM();
        myWriter.write(String.valueOf(clauze));
        myWriter.write("\n");

        for (int l = 1; l <= getN(); l++) {
            for (int i = 1; i <= getK(); i++) {
                myWriter.write(String.valueOf((i - 1) * getN() + l));
                myWriter.write(" ");
            }

            // S-a terminat clauza
            myWriter.write(" 0");

            // Urmatoarea clauza
            myWriter.write("\n");

            for (int j = 1; j <= k - 1; j++) {
                for (int t = j + 1; t <= k; t++) {
                    myWriter.write(String.valueOf(-((j - 1) * getN() + l)));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(-((t - 1) * getN() + l)));
                    myWriter.write(" 0");
                    myWriter.write("\n");
                }
            }
        }

        for (int i = 1; i <= getN(); i++) {
            for (int j = 1; j <= getN(); j++) {
                // Daca sunt adiacente
                if (getA()[i][j] == 1) {
                    for (int t = 1; t <= getK(); t++) {
                        myWriter.write(String.valueOf(-((t - 1) * getN() + i)));
                        myWriter.write(" ");
                        myWriter.write(String.valueOf(-((t - 1) * getN() + j)));
                        myWriter.write(" 0");
                        myWriter.write("\n");
                    }
                }
            }
        }

        myWriter.close();

//        Scanner sc2 = new Scanner(new FileInputStream(oracleInFilename));
//        while(sc2.hasNextLine()) {
//            String line = sc2.nextLine();
//            System.out.println(line);
//        }
//
//        sc2.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

//        formulateOracleQuestion();

//        Scanner sc2 = new Scanner(new FileInputStream(oracleOutFilename));
//        while(sc2.hasNextLine()) {
//            String line = sc2.nextLine();
//            System.out.println(line);
//        }
//
//        sc2.close();

        // Deschid fisierul de input al oracolului
        Scanner s = new Scanner(new BufferedReader(new FileReader(oracleOutFilename)));

        // Retin raspunsul de la oracol
        this.answer = s.nextLine();
        if (this.answer.equals("True")) {
            this.numVars = s.nextInt();
            this.vect = new int[numVars + 1];
            for (int i = 1; i <= numVars; i++)
                this.vect[i] = s.nextInt();
        }

        // Interpretez raspunsul si il memorez intr-o lista
        if (this.answer.equals("True")) {
            outputBuffer = new LinkedList<>();

            for (int i = 1; i <= getN(); i++) {
                for (int j = 1; j <= getK(); j++) {
                    if (vect[(j - 1) * getN() + i] > 0) {
                       outputBuffer.add(j);
                    }
                }
            }
        }
//        System.out.println(this.answer + " " + this.numVars + "\n");
//        for (int i = 1; i <= numVars; i++) {
//            System.out.print(this.vect[i] + " ");
//        }

        s.close();


    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

//        decipherOracleAnswer();

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

        myWriter.write(answer);

        if (this.answer.equals("True")) {
            myWriter.write("\n");
            for (int i = 0; i < outputBuffer.size(); i++) {
                myWriter.write(String.valueOf(outputBuffer.get(i)));
                myWriter.write(" ");
            }
        }
        myWriter.close();
    }
}
