// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {
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
     * Dimensiunea clicii cautate (familiei extinse)
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

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes

        Scanner s = new Scanner(new BufferedReader(new FileReader(inFilename)));

        // Citesc valorile pentru numarul de noduri, muchii si
        // dimensiunea familiei extinse
        int n = s.nextInt();
        int m = s.nextInt();
        int k = s.nextInt();

        // Setez dimensiunile matricei
        // Aceasta va fi initializata by default cu 0
        a = new int[n + 1][n + 1];
        setN(n);
        setM(m);
        setK(k);
        setA(a);


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

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        // Scrierea in fisierul oracolului
        FileWriter myWriter = new FileWriter(oracleInFilename);

        myWriter.write("p cnf ");

        // Numarul total de variabile
        myWriter.write(String.valueOf(getN() * getK()));
        myWriter.write(" ");

        // Numarul de clauze
        int clauze = getK() +  getN() * (getK() * (getK() - 1) / 2)
                + (getN() * getN() - getN() - 2 * getM()) * getK() * (getK() - 1) / 2;
        myWriter.write(String.valueOf(clauze));
        myWriter.write("\n");


        // Pentru fiecare pozitie din clica sigur e asignat
        // un nod
        for (int l = 1; l <= getK(); l++) {
            for (int i = 1; i <= getN(); i++) {
                myWriter.write(String.valueOf((l - 1) * getN() + i));
                myWriter.write(" ");
            }

            // S-a terminat clauza
            myWriter.write("0");

            // Urmatoarea clauza
            myWriter.write("\n");
        }

        // Daca nu exista muchie intre 2 noduri atunci ele
        // nu pot face parte din aceeasi clica
          for (int u = 1; u <= getN(); u++) {
              for (int w = 1; w <= getN(); w++) {
                  if (a[u][w] == 0 && u != w) {
                      for (int i = 1; i <= getK() - 1; i++) {
                          for (int j = i + 1; j <= getK(); j++) {

                              myWriter.write(String.valueOf(-((i - 1) * getN() + u)));
                              myWriter.write(" ");
                              myWriter.write(String.valueOf(-((j - 1) * getN() + w)));
                              myWriter.write(" 0");
                              myWriter.write("\n");
                          }
                      }
                  }
              }
          }

        // Un nod nu poate fi pe 2 pozitii diferite in clica
        for (int v = 1; v <= getN(); v++) {
            for (int i = 1; i <= getK() - 1; i++) {
               for (int j = i + 1; j <= getK(); j++) {

                   myWriter.write(String.valueOf(-((i - 1) * getN() + v)));
                   myWriter.write(" ");
                   myWriter.write(String.valueOf(-((j - 1) * getN() + v)));
                   myWriter.write(" 0");
                   myWriter.write("\n");
               }
            }
       }
        myWriter.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        // Deschid fisierul de input al oracolului
        Scanner s = new Scanner(new BufferedReader(new FileReader(oracleOutFilename)));

        // Retin raspunsul de la oracol
        setAnswer(s.nextLine());

        if (getAnswer().equals("True")) {
            int numVars = s.nextInt();
            int[] oracleReturnVect = new int[numVars + 1];
            for (int i = 1; i <= numVars; i++)
                oracleReturnVect[i] = s.nextInt();

            // Interpretez raspunsul si il memorez intr-o lista
            outputBuffer = new LinkedList<>();
            for (int i = 1; i <= getN(); i++) {
                for (int j = 1; j <= getK(); j++) {
                    if (oracleReturnVect[(j - 1) * getN() + i] > 0) {
                        outputBuffer.add(i);
                    }
                }
            }
        }

        s.close();
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

        // Scriu raspunsul
        myWriter.write(getAnswer());

        // Daca e true scriu vectorul corespunzator
        if (getAnswer().equals("True")) {
            myWriter.write("\n");
            for (Integer integer : outputBuffer) {
                myWriter.write(String.valueOf(integer));
                myWriter.write(" ");
            }
        }

        myWriter.close();
    }
}
