package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

public class Utils {

    private String pathToDrl = "D:\\Repos_git\\cleanmaven\\src\\main\\resources\\org\\example\\rules\\Sample.drl";

    public void insertRule(String rule) throws Exception {
        File file = new File(pathToDrl);
        String data = " ";
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            data += myReader.nextLine() + "\n";
        }
        rule.replace("\t\t\t\t\t", "\n");
        data += rule + "\n";
        //data += "end";

        myReader.close();

        BufferedWriter writ = new BufferedWriter(new FileWriter(pathToDrl));
        writ.write(data);
        writ.close();

    }

    public void insertRules(List<String> rules) throws Exception {
        File file = new File(pathToDrl);
        FileWriter wChor = new FileWriter(pathToDrl);
        BufferedWriter bChor = new BufferedWriter(wChor);


        String data = " ";
        for (String rule : rules) {
            data += rule;
        }
        String initial = "import java.util.List\n" +
                "import java.util.Arrays\n" +
                "import org.example.utils.BlockchainUtils\n\n";
        bChor.write(initial + data);
        bChor.flush();
        bChor.close();

    }

    public void modifyRule(String rule, String idToChange) throws Exception {
        File file = new File(pathToDrl);
        String data = " ";
        Scanner myReader = new Scanner(file);
        int delete = 0;
        while (myReader.hasNextLine()) {
            String buffer = myReader.nextLine();
            if (delete == 1 && !(buffer.contains("end"))) {
                data += " ";
            } else if (delete == 1 && buffer.contains("end")) {
                delete = 0;
                data += " ";
            } else if (delete == 0 && (buffer.contains(idToChange))) {
                delete = 1;
                data += " ";
            } else {
                //System.out.println(myReader.nextLine());
                data += buffer + "\n";
            }

        }
        myReader.close();
        //System.out.println(data);
        data += rule + "\n";
        data += "end";
        BufferedWriter writ = new BufferedWriter(new FileWriter(pathToDrl));
        writ.write(data);
        writ.close();
    }


    public void removeRule(String ruleID) throws Exception {
        File file = new File(
                "D:\\Repos_git\\cleanmaven\\src\\main\\resources\\org\\example\\rules\\Sample.drl");
        String data = " ";
        Scanner myReader = new Scanner(file);
        int delete = 0;
        while (myReader.hasNextLine()) {
            String buffer = myReader.nextLine();
            if (delete == 1 && !(buffer.contains("end"))) {
                data += " ";
            } else if (delete == 1 && buffer.contains("end")) {
                delete = 0;
                data += " ";
            } else if (delete == 0 && (buffer.contains(ruleID))) {
                delete = 1;
                data += " ";
            } else {
                data += buffer + "\n";
            }

        }
        myReader.close();

        BufferedWriter writ = new BufferedWriter(new FileWriter(
                "D:\\Repos_git\\cleanmaven\\src\\main\\resources\\org\\example\\rules\\Sample.drl"));
        writ.write(data);
        writ.close();
    }

    public String getPathToDrl() {
        return pathToDrl;
    }


}

