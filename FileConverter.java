package fileconverter;

import java.io.*;
import java.util.Scanner;

/*
Louis-Charles Marquis
COMP249
Assignment # 3
March 25th, 2022
*/

public class FileConverter {

    //This program converts any CSV file to HTML
    public static void main(String[] args) throws IOException, CSVAttributeMissing, CSVDataMissing { //throws the class exception

        Scanner sc = new Scanner(System.in);
        // Welcome the user
        System.out.println("***Welcome to CSV/HTML converter***");
        // Enter the CSV file
        System.out.println("Enter your CSV file path");
        String file = sc.nextLine(); 
        Scanner inputFile = null;
        try {
            //Creates the file reader
            inputFile = new Scanner(new FileInputStream(file));
        }
        //catches the file not found exception
        catch(FileNotFoundException e) {
                //print error message
        	System.out.println("Could not open input file" + file + " for reading. ");
        	System.out.println("Please check that the file exists and is readable. This program will terminate after closing any opened files.");
        	return;
        }
        //Creates the file before using it
        System.out.println("Enter your new file name");
        String name = sc.nextLine();
        File html_file = new File(name+".html");

        //Checks if the name is already taken or not and if so giving one more chance before terminating
        if(html_file.createNewFile()){
            System.out.println("File created is named: " + html_file.getName());
        }
        else{
            //prompts the user to enter a valid file name
            System.out.println("This file name is already taken. Please enter a valid file name: ");
            String name2 = sc.nextLine();
            //create the file
            html_file = new File(name2+".html");
            if(html_file.createNewFile()){
                System.out.println("File Created with: " + html_file.getName());
            }
            else{
                System.out.println("File name is invalid again. Program is terminating.");
                //ends the program
                System.exit(0);
                }
        }
        //creates a printWriter
        PrintWriter writer= new PrintWriter(new FileOutputStream(html_file));
        
        //calls the ConvertCSVtoHTML method
        ConvertCSVtoHTML(html_file, writer, inputFile);

        //asks the user which file is to be opened
        System.out.println("Enter the name of the file you want to open");
        String openFile = sc.nextLine();
        BufferedReader fileReader = null;
        try {
            //create new bufferedReader
            fileReader = new BufferedReader(new FileReader(openFile));
        }
        //catch the exception if file name isnt correct
        catch(FileNotFoundException e) { 
            //display a message of error
            System.out.println("Could not open input file" + openFile + " for reading. ");
            System.out.println("Please check that the file exists and is readable. This program will terminate after closing any opened files.");
            return;
        }
        //displays the all content of the file if no exception is caught
        String line = "";
        while ((line = fileReader.readLine()) != null) {
            System.out.println(line);
        }
        //close the buffered reader, print writer and scanner
        fileReader.close();
        writer.close();
        inputFile.close(); 
        System.out.println("");
    }
    
    public static void ConvertCSVtoHTML(File f, PrintWriter fileWriter, Scanner sc) throws CSVAttributeMissing, CSVDataMissing, IOException{
        //initializing variables
        String line = "";
        int nb_line = 0;
        FileWriter logWriter = new FileWriter("Exceptions.log"); //File writer for the exceptions 
        
        //go through the file
        while ((sc.hasNextLine())) {
           
            line = sc.nextLine();
            //stores the line in array of strings
            String[] tokens = line.split(","); 
         
            if(nb_line == 0){ 
                fileWriter.write("<html>  \n  <body>  \n  <table> \n <caption>"+line+"</caption>"+ "\n");
            }
            //This part throws an exception if an attribute is missing.
            if(nb_line == 1){ 
                for(String token: tokens){
                    if(token == ""){
                        logWriter.write("ERROR: In file covidStatistics.CSV. Missing attribute.File is not converted to HTML.");
                        throw new CSVAttributeMissing("ERROR: In file covidStatistics.CSV. Missing attribute.File is not converted to HTML.");
                    }
                }
                fileWriter.write("<tr>  \n");
                for(String token: tokens){
                    fileWriter.write("   <th>" + token + "</th> \n");
                }
                fileWriter.write("</tr>  ");
            }

            fileWriter.write("<tr> \n");
            int index=0;
            //Loop through all tokens to find the last line.
            for(String token: tokens){ 
                if(nb_line == 0){//title's line (skip)
                    continue; 
                }
                if(nb_line == 1){//value's line (skip)
                    continue; 
                }
                if(token == ""){
                    //If something is missing, it throws an exception which is handled by the appropriate exception class. 
                    logWriter.write("WARNNING: In file covidStatistics.CSV line " + nb_line + " is not converted to HTML: missing data:" + index);
                }
                //We are looking for "Note" because that would mean its the last line of CSV.
                if(token.startsWith("Note")){
                    fileWriter.write("</table> \n <span>"+token+"</span>  \n  </body> \n </html>");
                    break;
                }
                fileWriter.write("   <td>" + token + "</td> \n ");
                index++;
            }
            fileWriter.write("</tr> \n ");

            nb_line ++; 
        }
        fileWriter.close();
        logWriter.close();
	}
}


