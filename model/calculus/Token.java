package model.calculus;

class Token{
  protected Sym sym;

  public Token(Sym sym){
    this.sym=sym;
  }

  public Sym getSym(){
    return this.sym;
  }

  public String toString(){
    return (" "+sym);
  }
}
class NumberToken extends Token{
  protected int value;

  public NumberToken(int value){
    super(Sym.NUM);
    this.value=value;
  }

  public int getValue(){
    return this.value;
  }

}

class WordToken extends Token{
  protected String content;

  public WordToken(Sym sym,String content){
    super(sym);
    this.content=content;
  }

  public String getContent(){
    return content;
  }

  public String toString(){
    return super.toString() + " " + content;
  }
}
