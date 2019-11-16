package model.calculus;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
      throws Exception {
        String lambdaTerm="",reduction="";
        Reader reader=null;
        if (args.length < 1) {
           // System.out.println("You could also use java Main <namefile> [<reduction>] \n 0 for the normal order and anything else for the applicative order");
            System.out.println("Enter a lambda term (with an extra lot of parentheses) \n");
            Scanner scanner = new Scanner(System.in);
            lambdaTerm = scanner.nextLine();
            System.out.println("Choose a reduction strategy : \n 0 for the normal order and anything else for the applicative order");
            reduction=scanner.next();
            reader=new StringReader(lambdaTerm);
        }
       else {
            File input = new File(args[0]);
            reader = new FileReader(input);
        }
            Lexer lexer = new Lexer(reader);
            LookAhead1 look = new LookAhead1(lexer);

            Parser parser = new Parser(look);
            int i=0;
            try{
                if(args.length == 2)
                    i = Integer.parseInt(args[1]);
                else if(args.length == 0) {
                    System.out.println(lambdaTerm);
                    i=Integer.parseInt(reduction);
                }
            }
            catch(NumberFormatException e){
                i = 0;
            }
            finally {
                try{
                    Tree tree = parser.progNotTerm(i);
                    tree.eval();
                }
                catch (Exception e) {
                    System.out.println("The expression is not correct");
                    System.out.println(e);
                    e.printStackTrace();
                }
                }
            }
        }

