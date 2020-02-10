package com.drakkonnys.dr4kk0_assistant_test.Modules;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    private StringBuilder useCase = new StringBuilder();
    private StringBuilder parameters = new StringBuilder();
    private StringBuilder examples = new StringBuilder();


    public String getFullHelp() {
        List<String> cases = new ArrayList<>();

        cases.add("Type 'help {command}' to see full details of each command\n");

        cases.add("echo {parameter}");
        cases.add("clear");
        cases.add("search {parameter}");
        cases.add("wikipedia {param}");
        cases.add("math {number1} {operation} {number2}");
        cases.add("translate {param}");
        cases.add("table {num}");
        cases.add("database {command} {param}");
        cases.add("time");
        cases.add("miletokm {param}");
        cases.add("kmtomile {param}");
        cases.add("ftoc {param}");
        cases.add("ctof {param}");
        cases.add("lumos");
        cases.add("nox");
        cases.add("alarm {hour} {minutes}");
        cases.add("reminder {hour} {minutes}");
        cases.add("reminder {minutes}");

        StringBuilder sanitized = new StringBuilder();
        for (String methodHelp : cases)
            sanitized.append(methodHelp).append("\n");

        return sanitized.toString();
    }

    public String[] getHelp(String command) {

        ClearAll();
        SetAll("Use case: ", "Parameters: ", "Examples: ");

        switch (command) {
            case "echo":
                SetAll("echo {parameter}", "{Text parameter}", "echo Hello World! How are you ?");
                break;
            case "clear":
                SetAll("clear", "{none}", "clear");
                break;
            case "search":
                SetAll("search {parameter}", "{Text parameter}", "search Horses running free");
                break;
            case "wikipedia":
                SetAll("wikipedia {parameter}", "{Text parameter}", "wikipedia Elon Musk");
                break;
            case "math":
                SetAll("math {number1} {operation} {number2}", "{Integer number1: [Number], Text operation: [+, -, *, /], Integer number2: [number]}", "math 10 + 10");
                break;
            case "translate":
                SetAll("translate {parameter}", "{Text parameter}", "translate Hola, como esta ?");
                break;
            case "table":
                SetAll("table {number}", "{Integer number: [number]}", "table 5");
                break;
            case "database":
                SetAll("[USE CASE 1]: database {Object command} {parameter}\n[USE CASE 2]: database {Object command}",
                        "{Object command: ['read' | 'add' | 'update' | 'remove'], [Text | Integer] parameter: ['Study' | 5]",
                        "[EXAMPLE 1]: database read\n[EXAMPLE 2]: database add Meeting at 5 pm\n[EXAMPLE 3]: database update 1 Meeting at 4 pm\n[EXAMPLE 4]: database remove 1");
                break;
            case "time":
                SetAll("time", "{none}", "time");
                break;
            case "miletokm":
                SetAll("miletokm {number}", "{[Integer | DecimalPoint] number: [25 | 4.45 | 30.21491]}", "miletokm 40.3");
                break;
            case "kmtomile":
                SetAll("kmtomile {number}", "{[Integer | DecimalPoint] number: [12 | 38.1 | 1491.20]}", "kmtomile 832.1");
                break;
            case "ftoc":
                SetAll("ftoc {number}", "{[Integer | DecimalPoint] number: [90 | 48.4 | 110.2]}", "ftoc 32");
                break;
            case "ctof":
                SetAll("ftoc {number}", "{[Integer | DecimalPoint] number: [2 | 80.4 | 41.23]}", "ctof 72.4");
                break;
            case "lumos":
                SetAll("lumos", "{none}", "lumos");
                break;
            case "knox":
                SetAll("knox", "{none}", "knox");
                break;
            case "alarm":
                SetAll("[USE CASE 1]: alarm {hour} {minutes}\n[USE CASE 2]: alarm {minutes}", "{Integer hours: [ 1 ~ 24 ], Integer minutes: [ 1 ~ 59 ]}", "[EXAMPLE 1]: alarm 10 50\n[EXAMPLE 2]: alarm 8");
                break;
            case "reminder":
                SetAll("[USE CASE 1]: reminder {hour} {minutes}\n[USE CASE 2]: reminder {minutes}", "{Integer hours: [ 1 ~ 24 ], Integer minutes: [ 1 ~ 59 ]}", "[EXAMPLE 1]: reminder 23 20\n[EXAMPLE 2]: reminder 40");
                break;
        }

        return new String[] {getUseCase().toString(), getParameters().toString(), getExamples().toString()};
    }

    private void SetAll(String useCase, String parameters, String examples) {
        this.useCase.append(useCase).append("\n");
        this.parameters.append(parameters).append("\n");
        this.examples.append(examples).append("\n");
    }

    private StringBuilder getUseCase() {
        return useCase;
    }

    private StringBuilder getParameters() {
        return parameters;
    }

    private StringBuilder getExamples() {
        return examples;
    }

    private void ClearAll() { // Cleaning the output since the append method is used
        this.useCase.delete(0, useCase.capacity());
        this.parameters.delete(0, parameters.capacity());
        this.examples.delete(0, examples.capacity());

    }
}
