package com.aipractice;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;


import static dev.langchain4j.data.message.UserMessage.userMessage;

public class ChatMemoryExample {

    public static void main(String[] args) {

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(300);

        String LOCALHOST = "http://localhost:11434";
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl(LOCALHOST)
                .modelName("gemma2")
                .build();
        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.

        chatMemory.add(userMessage("Hello, my name is Klaus"));
        AiMessage answer = model.generate(chatMemory.messages()).content();
        System.out.println(answer.text()); // Hello Klaus! How can I assist you today?
        chatMemory.add(answer);

        chatMemory.add(userMessage("i want to know about Java"));
        AiMessage answerForPhoneNumber = model.generate(chatMemory.messages()).content();
        System.out.println(answerForPhoneNumber.text()); // Hello Klaus! How can I assist you today?
        chatMemory.add(answerForPhoneNumber);


        chatMemory.add(userMessage("What is my name?"));
        AiMessage answerWithName = model.generate(chatMemory.messages()).content();
        System.out.println(answerWithName.text()); // Your name is Klaus.
        chatMemory.add(answerWithName);

        chatMemory.add(userMessage("did we discuss about java"));
        AiMessage responseForName = model.generate(chatMemory.messages()).content();
        System.out.println(responseForName.text()); // Your name is Klaus.
        chatMemory.add(responseForName);
    }
}
