// Copyright 2020
// Author: Radu Nichita

#include <iostream>
#include <algorithm>
#include "task.h"
#include "task1.h"
#include "task2.h"
#include "task3.h"
#include "bonus.h"


int main(int argc, char const *argv[]) {
    if (argc < 6) {
        std::cerr << "Usage: ./main <task> <input_file> <SAT_input_file> <SAT_output_file> <output_file>\n";
        return -1;
    }

    Task* task;
    auto task_number = std::string(argv[1]);
    auto input = std::string(argv[2]);
    auto sat_input = std::string(argv[3]);
    auto sat_output = std::string(argv[4]);
    auto output = std::string(argv[5]);

    if (task_number == "task1") {
        task = new Task1();
    } else if (task_number == "task2") {
        task = new Task2();
    } else if (task_number == "task3") {
        task = new Task3();
    } else if (task_number == "bonus") {
        task = new Bonus();
    } else {
        std::cerr << "Not a valid task\n";
        return -1;
    }

    task->add_files(input, sat_input, sat_output, output);
    task->solve();
    delete task;

    return 0;
}
