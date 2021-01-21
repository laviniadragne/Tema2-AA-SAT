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
     * Numarul de noduri (de familii de mafioti)
     */
    private int n;

    /**
     * Numarul de muchii (de relatii)
     */
    private int m;

    /**
     * Lista cu output-ul primit de la oracol
     */
    private List<Integer> outputBuffer;

    /**
     * Matrice de adiacenta a grafului complementar primit
     */
    private int[][] complement;


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

    public int[][] getComplement() {
        return complement;
    }


    /**
     * Umple matricea complement de 1 peste tot,
     * fara diagonala principala
     */
    public void fullOne() {
        for (int i = 1; i < getN(); i++) {
            for (int j = i + 1; j <= getN(); j++) {
                    complement[i][j] = 1;
                    complement[j][i] = 1;
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

    /**
     * Citesc datele din input si construiesc graful complementar
     * celui primit
     */
    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes
        Scanner s = new Scanner(new BufferedReader(new FileReader(inFilename)));

        // Citesc valorile pentru numarul de noduri si muchii
        int n = s.nextInt();
        int m = s.nextInt();

        setN(n);
        setM(m);

        // Aloc matricea de adiacenta pentru complementul
        // grafului primit
        complement = new int[n + 1][n + 1];

        // O umplu cu 1
        fullOne();

        // Completez matricea de adiacenta a grafului
        // complementar
        while (s.hasNext()) {
            if (s.hasNextInt())
            {
                int x = s.nextInt();
                int y = s.nextInt();
                getComplement()[x][y] = 0;
                getComplement()[y][x] = 0;
            }
        }

        s.close();
    }

    /**
     *  Aplic algoritmul pentru obtinerea unei clici maximale
     *  pe complementul grafului primit
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        // Scrierea in fisierul oracolului
        FileWriter myWriter = new FileWriter(oracleInFilename);

        myWriter.write(Constants.WCNF);

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

        // Clauzele soft sunt de forma xi
        // si au ponderea 1
        // <=> Toate nodurile pot face parte din clica maximala
        for (int i = 1; i <= getN(); i++) {

            // Ponderea 1
            myWriter.write("1 ");
            myWriter.write(String.valueOf(i));
            // S-a terminat clauza
            myWriter.write(" 0");

            // Urmatoarea clauza
            myWriter.write("\n");

        }

        // Daca nu e muchie intre cele 2 noduri
        // ele nu pot face parte din clica maximala
        // <=> (¬xi ∨ ¬xj) :{vi,vj} !∈ E(G)}
        for (int i = 1; i <= getN() - 1; i++) {
            for (int j = i + 1; j <= getN(); j++) {
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
    }

    /**
     * Descifrez raspunsul oracolului si il memorez intr-o lista
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        // Deschid fisierul de input al oracolului
        Scanner s = new Scanner(new BufferedReader(new FileReader(oracleOutFilename)));

        // Cate variabile sunt in total
        s.nextInt();
        // Cate variabile fac parte din clica maximala
        s.nextInt();

        // Interpretez raspunsul si il memorez intr-o lista
        outputBuffer = new LinkedList<>();

        // Aleg variabilele care nu sunt in raspunsul oracolului
        // Acele variabile parte din familia maxima
            for (int i = 1; i <= getN(); i++) {
                    int x = s.nextInt();
                    if (x < 0) {
                        outputBuffer.add(i);
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

        for (Integer integer : outputBuffer) {
            myWriter.write(String.valueOf(integer));
            myWriter.write(" ");
        }

        myWriter.close();
    }
}
