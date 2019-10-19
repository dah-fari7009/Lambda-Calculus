import java.io.*;

public class Main {

    public static void main(String[] args)
      throws Exception {
    	if (args.length <1) {
    		System.out.println("java Main <namefile> [<reduction>] \n 0 for the normal order and anything else for the applicative order");
    		System.exit(1);
    	}
    	File input = new File(args[0]);
    	Reader reader = new FileReader(input);
    	Lexer lexer = new Lexer(reader);
        LookAhead1 look = new LookAhead1(lexer);

        Parser parser = new Parser(look);
        try {
          int i;
          if(args.length==1) i=0;
          else i=Integer.parseInt(args[1]);
        	Tree tree=parser.progNotTerm(i);
          tree.eval();
        }
        catch (Exception e){
        	System.out.println("The expression is not correct");
          System.out.println(e);
        	e.printStackTrace();
        }
      }
    }
