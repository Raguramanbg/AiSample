package com.aipractice;

import dev.langchain4j.model.ollama.OllamaChatModel;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    // Ollama serve locally on port 11434
    private static final String LOCALHOST = "http://localhost:11434";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        var model = OllamaChatModel.builder()
                .baseUrl(LOCALHOST)
                .modelName("gemma2")
                .build();
        while (true) {
            System.out.print("""
                    Type 'exit' to quit the program.
                    Enter your prompt:\s""");
            String userPrompt = scanner.nextLine();
            if (userPrompt.equals("exit")) {
                break;
            }
            String response = model.generate(userPrompt);
            System.out.printf("Response: %s%n", response);
        }
    }
}