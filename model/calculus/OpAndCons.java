package model.calculus;

import java.io.*;
public interface OpAndCons{
  final String omega="((λx.(xx))(λx.(xx))";
  final String pair="(λfsb.((bf)s))";
  final String fst="(λp.(p(λab.a)))";
  final String snd="(λp.(p(λab.b)))";
  final String tru="(λab.a)";
  final String fals="(λab.b)";
  final String and="(λxy.((xy)x))";
  final String or="(λxy.((xx)y))";
  //final String or="(λxy.((x(λuv.u))y))";
  final String not="(λx.((x(λuv.v))(λab.a)))";
  final String iszero="(λm.((m(λx.false)) true))";
  final String leq="(λmn.("+iszero+"(m-n)))";
  final String neq="(λab.((!(a<=b)) || (!(b<=a))))";
  final String geq="(λab.(b<=a))";
  final String lt="(λab.(!(b<=a)))";
  final String gt="(λab.(!(a<=b)))";
  final String eq="(λmn.((m<=n) && (n<=m)))";
  final String iF="(λabc.((ab)c))";
  final String fix="(λy.((λx.(y(xx)))(λx.(y(xx)))))";
  final String succ="(λnfx.(f((nf)x)))";
  final String plus="(λnm.(λfy.((nf)((mf)y))))";
 final String pred="(λnfx.( ( ( n  (λgh.( h (gf) ) )  ) (λu.x) )  (λu.u) ) )";
 final String minus="(λma.((a"+pred+")m))";
  final String mult="(λnmf.(n(mf)))";
  final String exp="(λnm.(mn))";

  //final String fix="(λf.((λx.(f(λy.((xx)y))))(λx.(f(λy.((xx)y))))))";


  static LambdaTerm parse(String s) throws Exception{
    Lexer lexer=new Lexer(new StringReader(s));
    LookAhead1 look=new LookAhead1(lexer);
    return new Parser(look).progNotTerm(0).term();
  }

  static Abstraction tru(){
    return new Abstraction('a',new Abstraction('b',new Variable('a')));
  }

  static Abstraction fals(){
    return new Abstraction('a',new Abstraction('b',new Variable('b')));
  }

  static LambdaTerm fac() throws Exception{
    String h="(λfn.if (n=0) then 1 else (f*(g(n-1))))";
    return new Application(parse(fix),parse(h));
  }

  static String div() throws Exception{
    String div="(λgqab.(((a<b) (("+pair+" q) a)) (((g (q+1))(a-b)) b)) )";
  //  String div="(λcnmfx.((λd.((("+iszero+"d)((0f)x)) (f((((cd)m)f)x)))) (n-m) ))";
  //  String res="(λn.(("+fix+div+")(n+1)))";
  String res="(("+fix+div+")0)";
    return res;
  }

  static String intDiv(){
    String res="(λab.("+fst+"(a/rb)))";
    return res;
  }

  static String mod(){
    String res="(λab.("+snd+"(a/rb)))";
    return res;
  }

  static LambdaTerm fib() throws Exception{
    //String fib="(λn.((((n(λfab.((fb)(a+b)))) true)0)1))";
    String fib="(λrn.if (n<=2) then 1 else ((r(n-1)) + (r(n-2))))";
    return new Application(parse(fix),parse(fib));
  }

  static String numeral(int number){
    String s="(λfx.";
    for(int i=0;i<number;i++) s+="(f";
    s+="x)";
    for(int i=0;i<number;i++) s+=")";
    return s;
  }



}
