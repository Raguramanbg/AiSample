package com.aipractice;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.time.LocalDate;
import java.util.List;

public class AiService {

    static String localHostUrl = "http://localhost:11434";
    static OllamaChatModel model = OllamaChatModel.builder().
            baseUrl(localHostUrl)
            .modelName("gemma2")
            .build();

    static class BasicAiServices {
        interface Friend {
            @UserMessage("Good Afternoon. Answer using slang. {{it}}")
            String chat(String userMessage);
        }

        public static void main(String[] args) {
            Friend assistant = AiServices.create(Friend.class, model);
            String answer = assistant.chat("Hello");
            System.out.println(answer);
        }
    }

    static class AiServiceWithVariableAndOutput {
        interface TextUtils {

            @SystemMessage("Professional Translator into Language{{language}}")
            @UserMessage("Translate the following text: {{text}}")
            String translate(@V("text") String text, @V("language") String language);

            @SystemMessage("Summarize every message from the user in {{n}} bullet points. provide only bullet points.")
            List<String> summarize(@UserMessage String text, @V("n") int n);

            @UserMessage("Does {{it}} has a positive sentiment?")
            boolean isPositive(String text);
        }


        public static void main(String[] args) {
            TextUtils textUtils = AiServices.create(TextUtils.class, model);
            String translate = textUtils.translate("Good Morning.", "Spanish");
            System.out.println(translate);

            String discoverBehaviours = "We play to win, we are good partners,we succeed together. Discover is the best place to work.";
            List<String> result = textUtils.summarize(discoverBehaviours, 1);
            System.out.println(result);

            boolean isPositive = textUtils.isPositive("Its a Great Day!");
            System.out.println(isPositive);
        }
    }

    enum Priority {

        @Description("Critical issues such as payment gateway failures or security breaches.")
        CRITICAL,

        @Description("High-priority issues like major feature malfunctions or widespread outages.")
        HIGH,

        @Description("Low-priority issues such as minor bugs or cosmetic problems.")
        LOW
    }

    interface PriorityAnalyzer {

        @UserMessage("Analyze the priority of the following issue: {{it}}")
        Priority analyzePriority(String issueDescription);
    }

    public static void main(String[] args) {
        PriorityAnalyzer analyzer = AiServices.create(PriorityAnalyzer.class, model);
        Priority priority = analyzer.analyzePriority("The main payment gateway is down, and customers cannot process transactions.");
        System.out.println(priority);
    }

    static class PersonDataExtractor {

        class Person {
            @Description("first name of a person") // you can add an optional description to help an LLM have a better understanding
            String firstName;
            String lastName;
            LocalDate birthDate;
            Address address;
        }

        @Description("an address") // you can add an optional description to help an LLM have a better understanding
        class Address {
            String street;
            Integer streetNumber;
            String city;
        }

        interface PersonExtractor {

            @UserMessage("Extract information about a person from {{it}}")
            Person extractPersonFrom(String text);
        }

        public static void main(String[] args) {
            PersonExtractor personExtractor = AiServices.create(PersonExtractor.class, model);
            String text = """
            In 1968, amidst the fading echoes of Independence Day,
            a child named John arrived under the calm evening sky.
            This newborn, bearing the surname Doe, marked the start of a new journey.
            He was welcomed into the world at 345 Whispering Pines Avenue
            a quaint street nestled in the heart of Springfield
            an abode that echoed with the gentle hum of suburban dreams and aspirations.
            """;
            Person person = personExtractor.extractPersonFrom(text);
            System.out.println(person.firstName+" "+person.lastName+" "+person.birthDate+" "+person.address.street+" "+person.address.streetNumber+" "+person.address.city);
        }
    }
}
