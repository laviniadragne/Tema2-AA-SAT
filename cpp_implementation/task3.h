// Copyright 2020
// Authors: Radu Nichita, Matei Simtinică

#ifndef TASK3_H_
#define TASK3_H_

#include "task.h"
#include "task1.h"
#include "task2.h"

/*
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
class Task3 : public Task {
 private:
    std::string task2_in_filename;
    std::string task2_out_filename;
    // TODO: define necessary variables and/or data structures

 public:
    void solve() override {
        task2_in_filename = in_filename + "_t2";
        task2_out_filename = out_filename + "_t2";
        Task2 task2_solver = Task2();
        task2_solver.add_files(task2_in_filename, oracle_in_filename, oracle_out_filename, task2_out_filename);
        read_problem_data();

        // TODO: implement a way of successively querying the oracle (using Task2) about various arrest numbers until you
        //  find the minimum

        write_answer();
    }

    void read_problem_data() override {
        // TODO: read the problem input (in_filename) and store the data in the object's attributes
    }

    void reduce_to_task2() {
        // TODO: reduce the current problem to Task2
    }

    void extract_answer_from_task2() {
        // TODO: extract the current problem's answer from Task2's answer
    }

    void write_answer() override {
        // TODO: write the answer to the current problem (out_filename)
    };
};

#endif  // TASK3_H_
