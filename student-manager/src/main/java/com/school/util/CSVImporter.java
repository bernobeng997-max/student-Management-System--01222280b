package com.school.util;

import com.school.domain.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {

    public static List<Student> importFile(String path){

        List<Student> list = new ArrayList<>();

        try{
            File dir = new File("data");
            if(!dir.exists()) dir.mkdir();

            BufferedReader br = new BufferedReader(new FileReader(path));
            FileWriter error = new FileWriter("data/import_errors.csv");

            // CSV Header for error file
            error.write("Row,Error Message\n");

            br.readLine(); // skip header

            String line;
            int row = 1;

            while((line = br.readLine()) != null){

                row++;

                try{

                    String[] p = line.split(",");

                    if(p.length < 9){
                        error.write(row + ",Missing fields\n");
                        continue;
                    }

                    Student s = new Student(
                            p[0].trim(),
                            p[1].trim(),
                            p[2].trim(),
                            Integer.parseInt(p[3].trim()),
                            Double.parseDouble(p[4].trim()),
                            p[5].trim(),
                            p[6].trim(),
                            p[7].trim(),
                            p[8].trim()
                    );

                    list.add(s);

                }catch(Exception ex){

                    error.write(row + ",Invalid data format\n");
                }
            }

            br.close();
            error.close();

        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return list;
    }
}
