package operation;

import java.security.InvalidParameterException;

public class FunctionUtility {
   public static final String[] OPERATIONS = {"^", "*", "/", "+","-"};
   public static final String[] OPEN_SEPARATORS = {"("};
   public static final String[] CLOSED_SEPARATORS = {")"};
   public static final String[] VARIABLES = {"x", "y"};
   public static final String[] SPECIAL_OPERATIONS = {"ln", "log", "sin", "cos", "tan"};
   public static final String[] SPECIAL_OPERATION_STARTERS = {"l", "s", "c", "t"};

   public static double solve (String g, double x, double y) {return 0;}

   public enum Type {
      OPR("operation"), VAR ("variable"), OP ("operation"), S_OP ("special operation"), NUM ("number"), TEMP ("temporary"), BS ("Error");
      private final String t;
      private Type (String c) {t = c;}
      public String toString () {return t;}
   }

   public enum Variable {
      X ("x"), Y ("y");
      private final String t;
      private Variable (String c) {t = c;}
      public String toString () {return t;}
   }

   public enum Operator {
      ADD ("+"), SUB ("-"), DIV ("/"), MULT ("*"), EXP ("^");
      public final static int numOfOrders = 3;
      private final String t;
      private Operator (String c) {t = c;}
      public int getOrder () {
         if (this == EXP) {return 1;}
         else if (this == MULT || this == DIV) {return 2;}
         else {return 3;}
      }
      public String toString () {return t;}
   }

   public enum SpecialOperator {
      LN ("ln"), LOG ("log"), SIN ("sin"), COS ("cos"), TAN ("tan");
      private final String t;
      private SpecialOperator (String c) {t = c;}

      public String toString () {return t;}
   }

   public static class TypeWrapper {
      private int index;
      private Type type;

      public TypeWrapper (Type type, int index) {
         this.index = index;
         this.type = type;
      }

      public Type getType() {return type;}
      public int getIndex() {return index;}

      public String toString() {
         return type.toString();
      }
   }

   public static class DoubleWrapper {
      private double m_value;
      private boolean m_isInteger;
      public DoubleWrapper (double value, boolean isInteger) {
         m_value = value;
         m_isInteger = isInteger;
      }
      public double getValue () {return m_value;}
      public boolean isInteger () {return m_isInteger;}
   }

   public static Type toType (String g) {
      for (String j : OPERATIONS) {
         if (g.equals(j)) return Type.OP;
      }

      for (String j : VARIABLES) {
         if (g.equals(j)) return Type.VAR;
      }

      for (String j : OPEN_SEPARATORS) {
         if (g.equals(j)) return Type.OPR;
      }

      for (String j : SPECIAL_OPERATION_STARTERS) {
         if (g.equals(j)) return Type.S_OP;
      }

      if (g.matches("^\\d$")) return Type.NUM;

      else {return Type.BS;}
   }

   public static Variable toVariable (String g) {
      switch (g) {
         case "x" : return Variable.X;
         case "y" : return Variable.Y;
         default : throw new InvalidParameterException();
      }
   }

   public static Operator toOperation (String g) {
      switch (g) {
         case "^" :  return Operator.EXP;
         case "*" :  return Operator.MULT;
         case "/" :  return Operator.DIV;
         case "+" :  return Operator.ADD;
         case "-" :  return Operator.SUB;
         default : throw new InvalidParameterException();
      }
   }

   public static SpecialOperator toSpecialOperation (String g) {
      switch (g) {
         case "ln" :  return SpecialOperator.LN;
         case "log" :  return SpecialOperator.LOG;
         case "sin" :  return SpecialOperator.SIN;
         case "cos" :  return SpecialOperator.COS;
         case "tan" :  return SpecialOperator.TAN;
         default : throw new InvalidParameterException();
      }
   }

   public static DoubleWrapper extractDouble (String c, int initialIndex) {
      int i = initialIndex;
      String tAns = "";
      boolean isInteger = true;
      boolean isScientific = false;
      boolean lastScientific = false;
      while (i < c.length()) {
         String s = c.substring(i, i+1);
         if (s.matches("^\\d$")) {tAns += s;}
         else {
            if (s.equals(".")) {
               if (!isInteger) {throw new InvalidParameterException();}
                tAns += ".";
                isInteger = false;
                lastScientific = false;
            }
            else if (s.equals("E")) {
               if (isScientific) {throw new InvalidParameterException();}
               tAns += "E";
               isScientific = true;
               lastScientific = true;
            }
            else if ((s.equals("-") || s.equals("+")) && lastScientific) {
               tAns += s;
               lastScientific = false;
            }
            else {break;}
         }
         i++;
      }
      return new DoubleWrapper(Double.parseDouble(tAns), isInteger);
   }

   public static SpecialOperator extractSpecialOperation (String c, int initialIndex) {

      final int maxLength = 3;
      final int minLength = 2;
      if ((c.length() - initialIndex) < minLength) throw new InvalidParameterException();
      for (int i = minLength; i <= maxLength; i++) {
         try {
            return toSpecialOperation(c.substring(initialIndex, initialIndex + i));
         }
         catch (InvalidParameterException e) {;}
      }
      throw new InvalidParameterException();  
   }

   public static double biCalculations (Operator z, double x, double y) {
      switch (z) {
         case ADD :
            return x + y;
         case SUB :
            return x - y;
         case DIV :
            return x / y;
         case MULT :
            return x * y;
         case EXP :
            return Math.pow(x,y);
         default :
            throw new InvalidParameterException();
      }
   }

   public static double specialCalculations (SpecialOperator z, double x) {
      switch (z) {
         case SIN :
            return Math.sin(x);
         case COS :
            return Math.cos(x);
         case LOG :
            return Math.log10(x);
         case LN :
            return Math.log(x);
         default :
            throw new InvalidParameterException();
      }
   }
}
