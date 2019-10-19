%%
%public
%class Lexer
%unicode
%type Token
%line
%column

%yylexthrow LexerException

%{
  class LexerException extends Exception{
    public LexerException(int line, int column, String c){
      super("Line :"+line+" Column : "+column+" Character : "+c);
    }
  }

  public String getPosition(){
       return "Reading at line "+yyline+", column "+yycolumn;
    }
%}


blank=" "|"\n"|"\t"|"\r"
var=[a-z]
c_func=[A-Z]
number=[0-9]+



%%
"λ" {return new Token(Sym.LAMBDA);}
"." {return new Token(Sym.DOT);}
"(" {return new Token(Sym.LPAR);}
")" {return new Token(Sym.RPAR);}
{number} {return new NumberToken(Integer.parseInt(yytext()));}
"+" {return new Token(Sym.PLUS);}
"-" {return new Token(Sym.MINUS);}
"/" {return new Token(Sym.DIVI);}
"/r" {return new Token(Sym.DIV);}
"%" {return new Token(Sym.MOD);}
"*" {return new Token(Sym.MULT);}
"^" {return new Token(Sym.EXP);}
"<" {return new Token(Sym.LT);}
"<=" {return new Token(Sym.LEQ);}
">" {return new Token(Sym.GT);}
">=" {return new Token(Sym.GEQ);}
"=" {return new Token(Sym.EQ);}
"!=" {return new Token(Sym.NEQ);}
"!" {return new Token(Sym.NOT);}
"&&" {return new Token(Sym.AND);}
"||" {return new Token(Sym.OR);}
"true" {return new Token(Sym.TRUE);}
"false" {return new Token(Sym.FALSE);}
"fac" {return new Token(Sym.FAC);}
"fib" {return new Token(Sym.FIB);}
"pred" {return new Token(Sym.PRED);}
"if" {return new Token(Sym.IF);}
"then" {return new Token(Sym.THEN);}
"else" {return new Token(Sym.ELSE);}
{var} {return new WordToken(Sym.VAR,yytext());}
{c_func} {return new WordToken(Sym.FUNC,yytext());}
{blank} {}
[^] {throw new LexerException(yyline, yycolumn,yytext());}
