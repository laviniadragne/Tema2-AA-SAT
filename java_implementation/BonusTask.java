// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Bonus Task
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class BonusTask extends Task {
    // TODO: define necessary variables and/or data structures

    /**
     * Matricea in care retin graful cu muchii adiacente
     * (familiile de mafioti)
     */
    private int a[][];
    private int n;
    private int m;
    private int k;

    private int answer;
    private int numVars;
    private int vect[];

    private List<Integer> outputBuffer;

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

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void fullZeros(int matrix[][]) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }
    }


    public int getNumVars() {
        return numVars;
    }

    public void setNumVars(int numVars) {
        this.numVars = numVars;
    }

    public int[] getVect() {
        return vect;
    }

    public void setVect(int[] vect) {
        this.vect = vect;
    }

    public List<Integer> getOutputBuffer() {
        return outputBuffer;
    }

    public void setOutputBuffer(List<Integer> outputBuffer) {
        this.outputBuffer = outputBuffer;
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
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        constructComplement();

        // Aplic algoritmul pentru obtinerea unei clici maximale
        // pe complementul grafului primit

        // Scrierea in fisierul oracolului
        FileWriter myWriter = new FileWriter(oracleInFilename);


        myWriter.write("p wcnf ");

        // Numarul total de var
        myWriter.write(String.valueOf(getN()));
        myWriter.write(" ");

        // Numarul de clauze
        int clauze = getN() + getM();
        myWriter.write(String.valueOf(clauze));
        myWriter.write(" ");

        // Suma tuturor clauzelor optionale
        // Sunt n clauze soft si toate au ponderea 1
        myWriter.write(String.valueOf(getN() + 1));

        myWriter.write("\n");


        int vars = 0;

        // Clauzele soft sunt de forma xi
        // si au ponderea 1
        for (int i = 1; i <= getN(); i++) {

            // Ponderea 1
            myWriter.write("1 ");
            myWriter.write(String.valueOf(i));
            // S-a terminat clauza
            myWriter.write(" 0");

            // Urmatoarea clauza
            myWriter.write("\n");

            vars++;
        }

        // (¬xi ∨ ¬xj) :{vi,vj} !∈ E(G)}
        for (int i = 1; i <= n - 1; i++) {
            for (int j = i + 1; j <= n; j++) {
                // Daca nu e muchie, e clauza obligatorie
                if (complement[i][j] == 0) {
                    myWriter.write(String.valueOf(n + 1));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(-i));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(-j));
                    myWriter.write(" 0\n");
                }
            }
        }


        myWriter.close();

//        System.out.println(vars);
//        System.out.println(vars);
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
        // Cate variabile sunt
        s.nextInt();
        // Cate variabile fac parte din clica maximala
        this.answer = s.nextInt();
        this.vect = new int[n + 1];
        for (int i = 1; i <= n; i++)
                this.vect[i] = s.nextInt();

        // Interpretez raspunsul si il memorez intr-o lista
        outputBuffer = new LinkedList<>();

        // Aleg variabilele care nu sunt in raspunsul oracolului
        // Acelea var parte din familia maxima
            for (int i = 1; i <= getN(); i++) {
                    if (vect[i] < 0) {
                        outputBuffer.add(i);
                    }
                }

        s.close();
//
//
//        System.out.println( "//////////" + this.answer + " " + this.numVars + "\n");
//        for (int i = 1; i <= numVars; i++) {
//            System.out.print(this.vect[i] + " ");
//        }

    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

            for (int i = 0; i < outputBuffer.size(); i++) {
                myWriter.write(String.valueOf(outputBuffer.get(i)));
                myWriter.write(" ");
            }

        myWriter.close();
    }
}
