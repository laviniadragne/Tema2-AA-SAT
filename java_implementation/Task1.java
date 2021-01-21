// Copyright 2020
// Author: Matei Simtinică

import out.production.java_implementation.Constants;

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
         * Matricea in care retin muchiile adiacente
         * (familiile de mafioti si relatiile dintre ele)
         */
        private int[][] a;
        
        /**
         * Numarul de noduri (de familii de mafioti)
         */
        private int n;
        
        /**
         * Numarul de muchii (de relatii)
         */
        private int m;
        
        /**
         * Numarul de culori (spionii disponibili)
         */
        private int k;
        
        /**
         * Raspunsul dat de oracol (True sau False)
         */
        private String answer;

        /**
         * Lista cu nodurile ce trebuie
         * scrise in fisierul de iesire
         */
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

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    /**
     * Citesc datele din fisierul de input si le stochez in campurile clasei
     */
    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes

        Scanner s = new Scanner(new BufferedReader(new FileReader(inFilename)));

        // Citesc valorile pentru numarul de noduri, muchii si culori
        int n = s.nextInt();
        int m = s.nextInt();
        int k = s.nextInt();

        // Setez dimensiunile si aloc memorie
        // Valorile din matricea a vor fi initializate
        // by default cu 0
        a = new int[n + 1][n + 1];
        setN(n);
        setM(m);
        setK(k);
        setA(a);

        // Completez matricea de adiacenta a grafului
        // cu datele primite
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
     * Reduc problema la o problema SAT
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        // Scrierea in fisierul oracolului
        FileWriter myWriter = new FileWriter(oracleInFilename);

        myWriter.write(Constants.CNF);

        // Numarul total de variabile
        myWriter.write(String.valueOf(getN() * getK()));
        myWriter.write(" ");

        // Numarul de clauze
        int clauze = getN() + getN() * (getK() * (getK() + 1))/2 + getM();
        myWriter.write(String.valueOf(clauze));
        myWriter.write("\n");


        for (int l = 1; l <= getN(); l++) {
            // Fiecare nod e colorat cu minim o culoare
            for (int i = 1; i <= getK(); i++) {
                myWriter.write(String.valueOf((i - 1) * getN() + l));
                myWriter.write(" ");
            }

            // S-a terminat clauza
            myWriter.write(" 0");

            // Urmatoarea clauza
            myWriter.write("\n");

            // Un nod poate avea o singura culoare
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

        // Verific doar pe jumatate din matrice
        // cealalta jumatate fiind simetrica
        for (int i = 1; i < getN(); i++) {
            for (int j = i + 1; j <= getN(); j++) {
                // Daca 2 noduri sunt adiacente,
                // nu pot avea aceeasi culoare
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
    }

    /**
     * Descifrez si memorez raspunsul oracolului
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        // Deschid fisierul de input al oracolului
        Scanner s = new Scanner(new BufferedReader(new FileReader(oracleOutFilename)));

        // Retin raspunsul de la oracol
        setAnswer(s.nextLine());

        if (getAnswer().equals(Constants.TRUE)) {

            // Numarul de variabile returnate de oracol
            int numVars = s.nextInt();

            // Vectorul returnat de oracol (daca raspunsul
            // e True)
            int[] oracleReturnVect = new int[numVars + 1];

            for (int i = 1; i <= numVars; i++)
                oracleReturnVect[i] = s.nextInt();

            // Interpretez raspunsul si il memorez intr-o lista
            outputBuffer = new LinkedList<>();
            for (int i = 1; i <= getN(); i++) {
                for (int j = 1; j <= getK(); j++) {
                    if (oracleReturnVect[(j - 1) * getN() + i] > 0) {
                        outputBuffer.add(j);
                    }
                }
            }
        }
        s.close();
    }

    /**
     * Scriu raspunsul in fisierul de output
     */
    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

        // Scriu raspunsul
        myWriter.write(getAnswer());

        // Daca e True, scriu si vectorul cu nodurile
        // corespunzatoare
        if (getAnswer().equals(Constants.TRUE)) {
            myWriter.write("\n");
            for (Integer integer : outputBuffer) {
                myWriter.write(String.valueOf(integer));
                myWriter.write(" ");
            }
        }
        myWriter.close();
    }
}
