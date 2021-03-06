// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.ArrayList;
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

    /**
     * Raspunsul primit in urma reducerii de la Task2
     */
    String answerTask2 = Constants.FALSE;

    /**
     * Daca raspunsul de la Task2 a fost True, lista cu nodurile
     * primite
     */
    List<Integer> listTask2 = new ArrayList<>();

    /**
     * Input-ul standard pentru Task2
     */
    String standardInputTask2 = "";

    /**
     * Prima linie standard pentru Task-ul 2
     */
    String standardFirstLine = "";

    /**
     * Prima linie updatata cu valoarea lui k, pentru Task2
     */
    String updateFirstLine = "";

    /**
     * Numarul de noduri (de familii de mafioti)
     */
    private int n;

    /**
     * Numarul de muchii (de relatii)
     */
    private int m;

    /**
     * Dimensiunea clicii cautate
     */
    private int k;

    /**
     * Matricea de adiacenta a grafului complementar celui
     * din input
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

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int[][] getComplement() {
        return complement;
    }

    public String getAnswerTask2() {
        return answerTask2;
    }

    public void setAnswerTask2(String answerTask2) {
        this.answerTask2 = answerTask2;
    }

    /**
     * Umple matricea complement de 1 peste tot,
     * fara diagonala principala
     */
    public void fullOne() {
        for (int i = 1; i <= getN() - 1; i++) {
            for (int j = i + 1; j <= getN(); j++) {
                    complement[i][j] = 1;
                    complement[j][i] = 1;
            }
        }
    }

    /**
     * Scrie prima linie din fisierul de input pentru Task2
     * @param typeLine tipul de prima linie (1 pentru linia standard
     *                   cu k = n si 2 pentru k != n)
     */
    public void writeLine(int typeLine) {
        String line = "";
        // Restul de muchii pana la un graf complet reprezinta
        // numarul de muchii al grafului complementar
        int muchiiComplement = getN()*(getN() - 1) / 2 - getM();

        // Scriu in fisierul de input al Task-ului 2 numarul
        // de noduri, numarul de muchii al grafului ce urmeaza
        // a fi verificat si dimensiunea clicii cautate
        line += String.valueOf(getN());
        line += " ";
        line += String.valueOf(muchiiComplement);
        line += " ";
        // k-ul actual
        line += String.valueOf(getK());
        line += "\n";
        if (typeLine == 1) {
            this.standardFirstLine = line;
        }
        else {
            this.updateFirstLine = line;
        }
    }

    /**
     * Scrie un input standard cu k = n, pe prima linie si relatiile
     * dintre noduri, pe celelalte linii, pentru Task2
     */
    public void writeStandardInput() {

        // Scrie prima linie
        writeLine(Constants.STANDARD_LINE);

        // Prima linie adaugata la input
        this.standardInputTask2 += this.standardFirstLine;

        // Scriu relatiile dintre noduri, in String-ul de input
        for (int i = 1; i < getN(); i++) {
            for (int j = i + 1; j <= getN(); j++) {
                if (complement[i][j] == 1) {
                    this.standardInputTask2 += String.valueOf(i);
                    this.standardInputTask2 += " ";
                    this.standardInputTask2 += (String.valueOf(j));
                    this.standardInputTask2 += " ";
                    this.standardInputTask2 += "\n";
                }
            }
        }
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);


        // Citesc datele problemei si le memorez in structurile de date
        // specifice, construind graful complementar
        readProblemData();

        // Reduc succesiv problema la Task2
        applyTask2(task2Solver);

        // Scriu raspunsul in fisierul de output
        writeAnswer();
    }

    /**
     * Citesc datele din input-ul dat si construiesc graful complementar
     */
    @Override
    public void readProblemData() throws IOException {

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
     * Metoda reduce Task-ul 3 la Task-ul 2, cautand in graful
     * complementar celui primit din input, clica de dimensiune
     * maximala
     * @param task2Solver un obiect de tip task2, folosit in reducere
     */
    public void applyTask2(Task2 task2Solver) throws IOException, InterruptedException {

        // Initial dimensiunea maximala a clicii este n (numarul de noduri)
        int k = getN();

        // k-ul e setat cu n
        setK(k);

        // Scriu Input-ul standard pentru Task2, k-ul fiind n
        writeStandardInput();

        // Cat timp poate exista o clica de dimensiune >= 2
        // si nu am gasit inca una, continui sa reduc la Task2
        while (k >= 2 && getAnswerTask2().equals(Constants.FALSE)) {

            // Setez dimensiunea clicii pe care o caut
            setK(k);

            // Scriu input-ul pentru Task-ul 2
            // cu dimensiunea maximala a clicii fiind k
            reduceToTask2();

            // Rezolv task-ul 2
            task2Solver.solve();

            // Citesc din fisierul de output al Task-ului 2
            // raspunsul
            extractAnswerFromTask2();

            k--;
        }
    }

    /**
     * Construiesc input-ul pentru Task-ul 2
     */
    public void reduceToTask2() throws IOException {

        // Scrierea in fisierul de input al task-ului 2
        FileWriter myWriter = new FileWriter(task2InFilename);

        // Scriu prima linie cu valoarea actualizata a lui k
        writeLine(Constants.UPDATE_LINE);

        // Inlocuiesc prima linie din input cu linia cu k-ul updat-at
        String updateInputTask2 = standardInputTask2.replaceAll(this.standardFirstLine,
                                                                this.updateFirstLine);

        // Scriu noul input in fisier-ul de intrare pentru Task2
        myWriter.write(updateInputTask2);

        myWriter.close();
    }

    /**
     * Extrag intr-o lista raspunsul din fisierul de output al task-ului 2
     */
    public void extractAnswerFromTask2() throws FileNotFoundException {

        // Deschid fisierul de output al task-ului 2
        Scanner s = new Scanner(new BufferedReader(new FileReader(task2OutFilename)));

        // Curat lista anterioara
        listTask2.clear();

        // Retin raspunsul
        setAnswerTask2(s.nextLine());

        // Daca el e True, citesc si combinatia de noduri
        // din clica
        if (getAnswerTask2().equals(Constants.TRUE)) {
            for (int i = 1; i <= getK(); i++)
                listTask2.add(s.nextInt());
        }

        s.close();
    }

    /**
     * Scriu raspunsul obtinut in fisierul de output
     */
    @Override
    public void writeAnswer() throws IOException {

        // Scrierea in fisierul de output
        FileWriter myWriter = new FileWriter(outFilename);

        // Daca nodul nu se afla in raspunsul oracolului
        // din fisierul de output al Task-ului 2, atunci
        // el trebuie eliminat
        for (int i = 1; i <= getN(); i++) {
            if (!listTask2.contains(i)) {
                myWriter.write(String.valueOf(i));
                myWriter.write(" ");
            }
        }

        myWriter.close();
    }

}
