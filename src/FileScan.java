import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileScan {
    public static void main(String[] args) {
        File selectedFile = null;


        if (args.length > 0) {
            String filename = args[0];
            selectedFile = new File(filename);

            if (!selectedFile.exists() || selectedFile.isDirectory()) {
                System.err.println("Error: Cannot find file '" + filename + "' in the src directory.");
                System.err.println("Make sure the file exists in the 'src' folder of this project.");
                return;
            }
        }

        else {
            JFileChooser chooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir") + File.separator + "src");
            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            } else {
                System.out.println("No file selected!!! exiting.");
                return;
            }
        }

        Path file = selectedFile.toPath();
        String rec;
        int lineCount = 0;
        int wordCount = 0;
        int charCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Files.newInputStream(file, StandardOpenOption.CREATE)))) {

            System.out.println("\n=== Contents of: " + selectedFile.getName() + " ===\n");

            while (reader.ready()) {
                rec = reader.readLine();
                System.out.printf("Line %4d: %s%n", ++lineCount, rec);
                charCount += rec.length();

                String[] words = rec.trim().split("\\s+");
                if (!(words.length == 1 && words[0].isEmpty())) {
                    wordCount += words.length;
                }
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("FILE SUMMARY REPORT");
            System.out.println("=".repeat(60));
            System.out.printf("%-25s %s%n", "File Name:", selectedFile.getName());
            System.out.printf("%-25s %,d%n", "Number of Lines:", lineCount);
            System.out.printf("%-25s %,d%n", "Number of Words:", wordCount);
            System.out.printf("%-25s %,d%n", "Number of Characters:", charCount);
            System.out.println("=".repeat(60));

        } catch (FileNotFoundException e) {
            System.err.println("File not found!!!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}