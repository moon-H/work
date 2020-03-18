/**
 * Created by liwx on 2020-03-16.
 */
public class YeYe {
    static {
        System.out.println("YeYe静态代码块");
    }
}

class Father extends YeYe {
    public final static String strFather = "HelloJVM_Father";

    static {
        System.out.println("Father静态代码块");
    }
}

class Son extends Father {
    public static String strSon = "HelloJVM_Son";

    static {
        System.out.println("Son静态代码块");
    }
}

class InitiativeUse {
   public static void main(String[] args) {
       System.out.println(Son.strFather);
   }

}
