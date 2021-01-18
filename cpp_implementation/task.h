// Copyright 2020
// Authors: Radu Nichita, Matei Simtinică

#ifndef TASK_H_
#define TASK_H_

#include <iostream>
#include <cstdio>
#include <fstream>
#include <array>
#include <utility>
#include <string>

class Task {
 protected:
    std::string in_filename;
    std::string oracle_in_filename;
    std::string oracle_out_filename;
    std::string out_filename;

 public:
    virtual ~Task() = default;

    virtual void solve() = 0;
    virtual void read_problem_data() = 0;
    void formulate_oracle_question() {};
    void decipher_oracle_answer() {};
    virtual void write_answer() = 0;

    /*
     * Stores the files paths as class attributes.
     *
     * @param in_filename:         the file containing the problem input
     * @param oracle_in_filename:  the file containing the oracle input
     * @param oracle_out_filename: the file containing the oracle output
     * @param out_filename:        the file containing the problem output
     */
    void add_files(std::string in, std::string oracle_in, std::string oracle_out, std::string out) {
        in_filename = std::move(in);
        oracle_in_filename = std::move(oracle_in);
        oracle_out_filename = std::move(oracle_out);
        out_filename = std::move(out);
    }

    /*
     * Asks the oracle for an answer to the formulated question.
     */
    void ask_oracle() {
        auto command = "python3 sat_oracle.py " + oracle_in_filename + " " + oracle_out_filename;
        std::array<char, 512> buffer{};
        std::string output;

        auto pipe = popen(command.c_str(), "r");

        while (!feof(pipe)) {
            if (fgets(buffer.data(), 512, pipe) != nullptr) {
                output += buffer.data();
            }
        }

        auto rc = pclose(pipe);

        if (rc != EXIT_SUCCESS) {
            std::cerr << "Error encountered while running oracle" << std::endl;
            std::cerr << output << std::endl;
            exit(-1);
        }
    }
};

#endif  // TASK_H_
