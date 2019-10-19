abstract class LambdaTerm implements Cloneable{

  public abstract boolean contains(char target);

  public abstract void rename(char target,char act);

  public abstract LambdaTerm substitute(Variable target,LambdaTerm sub);

  public abstract boolean freeocc(char targ);

  public abstract boolean isInBnf();

  public abstract String toString();

  public abstract void bondWith(String adress,Abstraction v,boolean first);

  public  void bond(boolean first){
    bondWith("",null,first);
  }

  public abstract int size();

  public abstract LambdaTerm clone() throws CloneNotSupportedException;

  public abstract boolean contains(Variable v);

  public abstract boolean isAlphaEquivalent(LambdaTerm l);

  public Integer churchN() throws Exception{
    throw new Exception("Not a constant");
  }

  public Integer churchN(String adress1, String adress2) throws Exception{
      throw new Exception("Not a constant");
  }

}

class Variable extends LambdaTerm implements Cloneable{
  protected char name;
  protected String adress;

  public Variable(char n){
    name=n;
    adress=null;
  }

  public boolean isFree(){
    return adress==null;
  }

  public void setAdress(String s){
    adress=s;
  }

  public String adress(){
    return adress;
  }

  public boolean freeocc(char target){
    return contains(target);
  }

  public  boolean contains(char target){
    return target==(name);
  }

  public  boolean contains(Variable target){
    if(adress==null) return target.adress==null && contains(target.name());
    return adress.equals(target.adress);
  }

  public void rename(char t, char a){
    if(name==t) name=a;
  }

  public Integer churchN(String adress1, String adress2) throws Exception{
    if(adress!=null && adress.equals(adress2)) return 0;
    throw new Exception("Not a constant");
  }

  public LambdaTerm substitute(Variable target,LambdaTerm sub){
    LambdaTerm res=this;
    try{
      if(this.contains(target))res=sub.clone();
    }
    catch(CloneNotSupportedException e){
      System.exit(1);
    }
    return res;

  }

  public boolean isInBnf(){
    return true;
  }

  public char name(){
    return name;
  }

  public String toString(){
    return String.valueOf(name);
  }

  public Variable clone() throws CloneNotSupportedException{
    Variable v=new Variable(this.name);
    v.adress=this.adress;
    return v;
  }

  public void bondWith(String adr,Abstraction a,boolean first){
    if(a!=null && name==a.binding().name()){
      if(first && adress==null || !first && adress!=null) adress=a.binding().adress;
    }
  }

  public int size(){
    return 1;
  }

  public boolean isAlphaEquivalent(LambdaTerm l){
    if(!(l instanceof Variable)) return false;
    Variable v=(Variable)l;
    if(adress==null) return name==v.name();
    return adress.equals(v.adress);
  }

}

class Abstraction extends LambdaTerm implements Cloneable{
  protected Variable binding;
  protected LambdaTerm body;

  public Abstraction(char s, LambdaTerm b){
    this(new Variable(s),b);
  }

  public Abstraction(Variable s, LambdaTerm b){
    binding=s;
    body=b;
  }

  public Variable binding(){
    return binding;
  }

  public LambdaTerm body(){
    return body;
  }

  public  boolean contains(char target){
    return binding.contains(target) || body.contains(target);
  }

  public boolean contains(Variable target){
      return binding.contains(target) || body.contains(target);
  }


  public boolean freeocc(char target){
    return !binding.contains(target) && body.freeocc(target);
  }

  public void bondWith(String adress,Abstraction a,boolean first){
    binding.setAdress(adress+"0");
    if(a!=this) body.bondWith(adress+"1",a,first);
    body.bondWith(adress+"1",this,first);

  }

  public Abstraction clone() throws CloneNotSupportedException{
    return new Abstraction(binding.clone(),body.clone());
  }

  public int size(){
    return 2+body.size();
  }


public void rename(char t, char a){
    binding.rename(t,a);
    body.rename(t,a);
  }

  public Abstraction substitute(Variable target,LambdaTerm sub){
   /*if(!binding.contains(target.name()) && sub.freeocc(binding.name())){
      char tar='a';
      while(body.contains(tar) || sub.freeocc(tar)){
        tar ++;
        //better this
      }
      this.rename(binding.name(),tar);
    }*/
    return new Abstraction(binding,body.substitute(target,sub));
}


  public boolean isInBnf(){
    return body.isInBnf();
  }

  public String toString(){
    return "(λ"+binding.toString()+"."+body.toString()+")";
  }

  public boolean isAlphaEquivalent(LambdaTerm l){
    if(!(l instanceof Abstraction)) return false;
    Abstraction a=(Abstraction)l;
    return binding.isAlphaEquivalent(a.binding) && body.isAlphaEquivalent(a.body);
  }

  public Integer churchN() throws Exception{
    String adr1=binding.adress();
    if(!(body instanceof Abstraction)) throw new Exception("Not a constant");
    Abstraction res=(Abstraction)body;
    String adr2=res.binding.adress();
    return res.body.churchN(adr1,adr2);
  }
}

class Application extends LambdaTerm implements Cloneable{
  protected LambdaTerm func,arg;

  public Application(LambdaTerm t1,LambdaTerm t2){
    func=t1;
    arg=t2;
  }

  public  boolean contains(char target){
    return func.contains(target)  || arg.contains(target);
  }

  public  boolean contains(Variable target){
    return func.contains(target)  || arg.contains(target);
  }


  public boolean freeocc(char target){
    return func.freeocc(target) && arg.freeocc(target);
  }

  public void rename(char t, char a){
      func.rename(t,a);
      arg.rename(t,a);
    }

    public Application clone() throws CloneNotSupportedException{
      return new Application(func.clone(),arg.clone());
    }

  public Application substitute(Variable target,LambdaTerm sub){
    return new Application(func.substitute(target,sub),arg.substitute(target,sub));
  }

    public LambdaTerm func(){
      return func;
    }

    public LambdaTerm arg(){
      return arg;
    }

    public boolean isInBnf(){
      if(func instanceof Abstraction) return false;
      return func.isInBnf() && arg.isInBnf();
    }

  public String toString(){
    return "("+func.toString()+arg.toString()+")";
  }

  public void bondWith(String adress,Abstraction a,boolean first){
    func.bondWith(adress,a,first);
    arg.bondWith(adress,a,first);
  }

  public boolean isAlphaEquivalent(LambdaTerm l){
    if(!(l instanceof Application)) return false;
    Application a=(Application)l;
    return func.isAlphaEquivalent(a.func) && arg.isAlphaEquivalent(a.arg);
  }

  public Integer churchN(String adress1,String adress2) throws Exception{
    if(adress1==null || adress2==null) throw new Exception("Not a constant");
        if(func instanceof Variable){
          Variable v=(Variable)func;
          if(adress1.equals(v.adress())) return 1+arg.churchN(adress1,adress2);
      }
       throw new Exception("Not a constant");
    }

    public int size(){
      return func.size()+arg.size();
    }



}
