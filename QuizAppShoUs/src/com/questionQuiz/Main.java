package com.questionQuiz;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();
        quiz.startApp();
    }
}

class Quiz {
    Scanner sc = new Scanner(System.in);
    private Map<String, List<Integer>> scoreHistory = new HashMap<>();
    private List<Question> allQuestions = new ArrayList<>();

    // Load questions from file
    public Quiz() {
//        loadQuestions("question.txt");
    	loadQuestions("src/com/questionQuiz/question.txt");


    }

    private void loadQuestions(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String category = parts[0].trim();
                    String question = parts[1].trim();
                    String opt1 = parts[2].trim();
                    String opt2 = parts[3].trim();
                    String opt3 = parts[4].trim();
                    String opt4 = parts[5].trim();
                    char answer = parts[6].trim().toUpperCase().charAt(0);
                    allQuestions.add(new Question(category, question, opt1, opt2, opt3, opt4, answer));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading questions file: " + e.getMessage());
        }
    }

    public void startApp() {
        boolean playAgain = true;

        while (playAgain) {
            System.out.println("=============================================");
            System.out.println("        🎯 WELCOME TO SHOUS QUIZ APP 🎯");
            System.out.println("=============================================");
            System.out.println("Choose a Category:");
            System.out.println("1️⃣  Core Java");
            System.out.println("2️⃣  Advanced Java");
            System.out.println("3️⃣  Aptitude");
            System.out.print("Enter your choice (1/2/3): ");

            int choice = sc.nextInt();
            String category = "";

            switch (choice) {
                case 1: category = "Core Java"; break;
                case 2: category = "Advanced Java"; break;
                case 3: category = "Aptitude"; break;
                default:
                    System.out.println("❌ Invalid choice! Try again.");
                    continue;
            }

            // Filter questions by selected category
            Map<Question, Character> quizSet = new LinkedHashMap<>();
            for (Question q : allQuestions) {
                if (q.getCategory().equalsIgnoreCase(category)) {
                    quizSet.put(q, q.getAnswer());
                }
            }

            if (quizSet.isEmpty()) {
                System.out.println("❌ No questions available in this category.");
            } else {
                int score = runQuiz(quizSet, category);
                recordScore(category, score);
            }

            System.out.print("\n🔁 Do you want to play again? (yes/no): ");
            String again = sc.next().toLowerCase();
            playAgain = again.equals("yes");
        }

        showScoreboard();
        System.out.println("\n👋 Thanks for playing, Shoaib!");
        System.out.println("=============================================");
    }

    private int runQuiz(Map<Question, Character> quizSet, String category) {
        int score = 0;
        System.out.println("\n🧠 Starting " + category + " Quiz (" + quizSet.size() + " Questions)");

        for (Map.Entry<Question, Character> entry : quizSet.entrySet()) {
            Question q = entry.getKey();
            System.out.println("\n" + q.getQuestion());
            System.out.println(q.getOption1());
            System.out.println(q.getOption2());
            System.out.println(q.getOption3());
            System.out.println(q.getOption4());
            System.out.print("Your Answer: ");
            char ans = Character.toUpperCase(sc.next().charAt(0));

            if (ans == entry.getValue()) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong! Correct: " + entry.getValue());
            }
        }

        double pct = (score * 100.0) / quizSet.size();
        System.out.println("\n🎯 " + category + " Quiz Completed!");
        System.out.println("✅ Score: " + score + "/" + quizSet.size());
        System.out.printf("📊 Percentage: %.2f%%\n", pct);
        System.out.println(feedback(pct));
        return score;
    }

    private void recordScore(String category, int score) {
        scoreHistory.putIfAbsent(category, new ArrayList<>());
        scoreHistory.get(category).add(score);
    }

    private void showScoreboard() {
        System.out.println("\n🏆 SCOREBOARD SUMMARY");
        for (Map.Entry<String, List<Integer>> entry : scoreHistory.entrySet()) {
            String category = entry.getKey();
            List<Integer> scores = entry.getValue();
            double avg = scores.stream().mapToInt(Integer::intValue).average().orElse(0);
            System.out.println("\nCategory: " + category);
            System.out.println("Attempts: " + scores.size() + " | Scores: " + scores + " | Average: " + avg);
        }
    }

    private String feedback(double pct) {
        if (pct == 100) return "🏆 Perfect!";
        else if (pct >= 85) return "💪 Excellent!";
        else if (pct >= 70) return "👍 Good!";
        else if (pct >= 50) return "🙂 Fair!";
        else return "📘 Keep Learning!";
    }
}

// Question class
class Question {
    private String category;
    private String question, option1, option2, option3, option4;
    private char answer;

    public Question(String category, String question, String option1, String option2, String option3, String option4, char answer) {
        this.category = category;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
    }

    public String getCategory() { return category; }
    public String getQuestion() { return question; }
    public String getOption1() { return option1; }
    public String getOption2() { return option2; }
    public String getOption3() { return option3; }
    public String getOption4() { return option4; }
    public char getAnswer() { return answer; }
}
