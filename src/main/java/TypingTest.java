import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TypingTest {

    private static String lastInput = "";
    private static Scanner scanner = new Scanner(System.in);
    private static int correctCount = 0;
    private static int incorrectCount = 0;
    private static long totalTimeTaken = 0;
    private static long totalCharacterCount = 0;

    public static class InputRunnable implements Runnable {

        @Override
        public void run() {
            try{
//                System.out.println("The thread name is " + Thread.currentThread().getName());
                lastInput = scanner.nextLine();
                Thread.sleep(100);
            } catch (InterruptedException e) {
//                System.out.println("The thread name is " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public static void testWord(String wordToTest) {
        try {
            System.out.println(wordToTest);
            lastInput = "";

            Thread inputThread = new Thread(new InputRunnable());
            inputThread.start();

            long startTime = System.currentTimeMillis();

            long timeForWord = 1200L;
            long timeout = wordToTest.length() * timeForWord;

            try {
                inputThread.join(timeout);
            } catch (InterruptedException e) {
                System.out.println("***  Interrupted while waiting for input");
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            if (inputThread.isAlive()) {
                inputThread.interrupt();
                System.out.println("⏰ Time's up!");
                //lastInput = "";
            }

            System.out.println();
            System.out.print("You typed: " + lastInput + "  *****  ");
            if (lastInput.toLowerCase().equals(wordToTest.toLowerCase())) {
                correctCount++;
                totalCharacterCount += wordToTest.length();
                System.out.println("Correct");
            } else {
                incorrectCount++;
                System.out.println("Incorrect");
            }

            totalTimeTaken += Math.min(timeTaken, timeout);

//            System.out.println( "asdas    " +inputThread.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void typingTest(List<String> inputList) throws InterruptedException {

        for (int i = 0; i < inputList.size(); i++) {
            String wordToTest = inputList.get(i);
            testWord(wordToTest);
            Thread.sleep(2000); // Pause briefly before showing the next word
        }

        System.out.println("\n--- Test Summary ---");
        System.out.println("Correct words: " + correctCount);
        System.out.println("Incorrect words: " + incorrectCount);
        System.out.println("Total time: " + totalTimeTaken / 1000.0 + " seconds");
        int totalWords = correctCount + incorrectCount;
        if (totalWords > 0) {
            System.out.printf("Average time per word: %.2f seconds%n", (totalTimeTaken / 1000.0) / totalWords);
            System.out.println("Average time per character: "+(totalTimeTaken / 1000.0)/totalCharacterCount + " second") ;
        }
    }

    public static List<String> getWordList(String filePath) {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if(!line.trim().isEmpty()) {
                wordList.add(line);
            }
        }
        } catch (FileNotFoundException e) {
            System.out.println("*** File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("*** I/O exception: " + filePath);
            e.printStackTrace();
        }
        finally {
            return wordList;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> words = new ArrayList<>();
        words = getWordList("src/main/resources/Words.txt");

        if (words.size() == 0) {
            System.out.println("No words found. Exiting.");
            return;
        }

        Collections.shuffle(words);

        System.out.println("App Started ... ");

        words = words.subList(0, 13);

        System.out.println("Press enter to start ");
        scanner.nextLine();

        typingTest(words);

        System.out.println("Press enter to exit.");
        scanner.nextLine();
    }
}