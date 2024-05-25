package operation;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import operation.FunctionUtility.TypeWrapper;
import operation.FunctionUtility.Type;
import operation.FunctionUtility.Operator;
import operation.FunctionUtility.Variable;
import operation.FunctionUtility.SpecialOperator;
import operation.FunctionUtility.DoubleWrapper;

public class Function {

    protected final TypeWrapper [] formula;
    protected final Function [] functions;
    protected final Variable [] variables;
    protected final Operator [] operators;
    protected final SpecialOperator [] specialOperators;
    protected final Double [] numbers;
    

    /**
     * This is the heart and soul of the new and improved Function class.
     * Instead of reading operations linearly, it will use semi-recursion to store operation
     * (The class stores itself).
     */
    public Function (String op) {
        ArrayList<TypeWrapper> mFormula = new ArrayList<TypeWrapper>(op.length()+2);
        ArrayList<Function> mFunctions = new ArrayList<Function>();
        ArrayList<Variable> mVariables = new ArrayList<Variable>();
        ArrayList<Operator> mOperators = new ArrayList<Operator>();
        ArrayList<SpecialOperator> mSpecialOperators = new ArrayList<SpecialOperator>();
        ArrayList<Double> mNumbers = new ArrayList<Double>();

        Type superType = Type.TEMP;

        int superIterationModifier = 0;
        int operationCounter = 0;
        int variableCounter = 0;
        int operatorCounter = 0;
        int specialOperatorCounter = 0;
        int numCounter = 0;

        for (int i = 0; i < op.length(); i += (1 + superIterationModifier)) {

            superIterationModifier = 0;
            String sTemp = op.substring(i,i+1);

            if (sTemp.equals(" ")) continue;

            Type subType = FunctionUtility.toType(sTemp);

            switch (subType) {

                case OPR :
                    if (superType == Type.VAR || superType == Type.NUM || superType == Type.OPR) throw new InvalidParameterException();
                    int ignore = 0;
                    int outerIndex = -1;
                    for (int j = i+1; j < op.length(); j++) {
                        if (op.substring(j,j+1).equals(")")) {
                            if (ignore == 0) {
                                outerIndex = j + 1;
                                break;
                            }
                            else {ignore --;}
                        }
                        else if (op.substring(j,j+1).equals("(")) {ignore ++;}
                    }
                    if (ignore != 0) {throw new InvalidParameterException();}
                    mFunctions.add(new Function(op.substring(i+1,outerIndex-1)));
                    op = op.substring(0,i) + op.substring(outerIndex);
                    mFormula.add(new TypeWrapper (subType, operationCounter));
                    operationCounter ++;
                    superType = subType;
                    i--;
                    break;

                case VAR :
                    if (superType == Type.VAR || superType == Type.NUM) throw new InvalidParameterException();
                    mVariables.add(FunctionUtility.toVariable(sTemp));
                    mFormula.add(new TypeWrapper (subType, variableCounter));
                    variableCounter ++;
                    superType = subType;
                    break;

                case OP :
                    if (superType == Type.OP || superType == Type.S_OP || superType == Type.TEMP) throw new InvalidParameterException();
                    mOperators.add(FunctionUtility.toOperation(sTemp));
                    mFormula.add(new TypeWrapper (subType, operatorCounter));
                    operatorCounter ++;
                    superType = subType;
                    break;

                case S_OP :
                    if (superType == Type.VAR || superType == Type.NUM || superType == Type.OPR) throw new InvalidParameterException();
                    SpecialOperator ans = FunctionUtility.extractSpecialOperation(op,i);
                    superIterationModifier += ans.toString().length() - 1;
                    mSpecialOperators.add(ans);
                    mFormula.add(new TypeWrapper (subType, specialOperatorCounter));
                    specialOperatorCounter ++;
                    superType = subType;
                    break;

                case NUM :
                    if (superType == Type.VAR || superType == Type.OPR) throw new InvalidParameterException();
                    DoubleWrapper tWrapper = FunctionUtility.extractDouble(op, i);
                    double value = tWrapper.getValue();
                    int mod = (tWrapper.isInteger())? -2 : 0;
                    superIterationModifier += Double.toString(value).length() - 1 + mod;
                    mNumbers.add(value);
                    mFormula.add(new TypeWrapper (subType, numCounter));
                    numCounter ++;
                    superType = subType;
                    break;

                default : throw new InvalidParameterException();
            }
        }

        if (superType == Type.OP || superType == Type.S_OP) throw new InvalidParameterException();

        formula = new TypeWrapper [mFormula.size()];
        functions = new Function [mFunctions.size()];
        variables = new Variable [mVariables.size()];
        operators = new Operator [mOperators.size()];
        specialOperators = new SpecialOperator [mSpecialOperators.size()];
        numbers = new Double [mNumbers.size()];

        mFormula.toArray(formula);
        mFunctions.toArray(functions);
        mVariables.toArray(variables);
        mOperators.toArray(operators);
        mSpecialOperators.toArray(specialOperators);
        mNumbers.toArray(numbers);
    }

    public TypeWrapper [] getFormula () {return formula;}
    public Function [] getFunctions () {return functions;}
    public Variable [] getVariables () {return variables;}
    public Operator [] getOperations () {return operators;}
    public SpecialOperator [] getSpecialOperations () {return specialOperators;}
    public Double [] getNumbers () {return numbers;}

    public double solve (double x, double y) {
        Function tFunction = replaceVariables(x, y);
        TypeWrapper [] tFormula = tFunction.getFormula();

        String ans = "";
        for (int i = 0; i < formula.length; i++) {
            Type currentType = tFormula[i].getType();
            int currentIndex = tFormula[i].getIndex();
            switch (currentType) {
                case S_OP :
                    Type nextType = tFormula[i+1].getType();
                    int nextIndex = tFormula[i+1].getIndex();
                    double value = 0;
                    if (nextType == Type.OPR) {value = tFunction.functions[nextIndex].solve(x,y);}
                    else if (nextType == Type.NUM) {value = tFunction.numbers[nextIndex];}
                    ans += FunctionUtility.specialCalculations(tFunction.specialOperators[currentIndex], value);
                    i++;
                    break;
                case OP :
                    ans += tFunction.operators[currentIndex];
                    break;
                case NUM :
                    ans += tFunction.numbers[currentIndex];
                    break;
                case OPR :
                    ans += "(" + tFunction.functions[currentIndex] + ")";
                    break;
                default: throw new InvalidParameterException();
            }
        }

        tFunction =  new Function(ans);
        tFormula = tFunction.getFormula();

        String stringAns = "";

        int currentOrder = 1;
        while (currentOrder < Operator.numOfOrders) {
            double lastNumber = 0;
            for (int i = 0; i < tFormula.length; i++) {
                Type currentType = tFormula[i].getType();
                int currentIndex = tFormula[i].getIndex();
                outer :
                switch (currentType) {
                    case OP :
                        if (tFunction.operators[currentIndex].getOrder() != currentOrder) {
                            stringAns += "" + lastNumber + tFunction.operators[currentIndex];
                            break outer;
                        }
                        Type nextType = tFormula[i+1].getType();
                        int nextIndex = tFormula[i+1].getIndex();

                        double value = 0;
                        if (nextType == Type.OPR) {value = tFunction.functions[nextIndex].solve(x,y);}
                        else if (nextType == Type.NUM) {value = tFunction.numbers[nextIndex];}
                        
                        lastNumber = FunctionUtility.biCalculations(tFunction.operators[currentIndex], lastNumber, value);
                        i++;
                        break;
                    case NUM :
                        lastNumber = tFunction.numbers[currentIndex];
                        break;
                    case OPR :
                        lastNumber = tFunction.functions[currentIndex].solve(x,y);
                        break;
                    default: throw new InvalidParameterException();
                }
            }

            stringAns += lastNumber;
            tFunction = new Function (stringAns);
            tFormula = tFunction.getFormula();
            stringAns = "";
            currentOrder++;
        }

        double doubleAns = tFunction.numbers[tFormula[0].getIndex()];
        for (int i = 1; i < tFormula.length; i++) {
            Type currentType = tFormula[i].getType();
            int currentIndex = tFormula[i].getIndex();
            switch (currentType) {
                case OP :
                    Type nextType = tFormula[i+1].getType();
                    int nextIndex = tFormula[i+1].getIndex();
                    double value = 0;
                    if (nextType == Type.OPR) {value = tFunction.functions[nextIndex].solve(x,y);}
                    else if (nextType == Type.NUM) {value = tFunction.numbers[nextIndex];}
                    doubleAns = FunctionUtility.biCalculations(tFunction.operators[currentIndex], doubleAns, value);
                    i++;
                    break;
                case NUM :
                    break;
                default: throw new InvalidParameterException();
            }
        }

        return doubleAns;
    }

    public String toString () {
        String ans = "";
        for (TypeWrapper t : formula) {
           Type currentType = t.getType();
           int currentIndex = t.getIndex();
           switch (currentType) {
              case NUM:
                 ans += numbers[currentIndex];
                 break;
              case VAR:
                 ans += variables[currentIndex];
                 break;
              case OP:
                 ans += operators[currentIndex];
                 break;
              case S_OP:
                 ans += specialOperators[currentIndex];
                 break;
              case OPR:
                 ans += "(" + functions[currentIndex] + ")";
                 break;
              default:
           }
        }
        return ans;
     }

    public String toRangeString (int i, int f) {
        if ((i < 0 && i > formula.length) && (f < 0 && f > formula.length) && (i >= f)) throw new InvalidParameterException("Incorrect numerical value in input.");
        String ans = "";
        for (int j = i; j < f; j++) {
            Type currentType = formula[j].getType();
            int currentIndex = formula[j].getIndex();
            switch (currentType) {
                case NUM:
                    ans += numbers[currentIndex];
                    break;
                case VAR:
                    ans += variables[currentIndex];
                    break;
                case OP:
                    ans += operators[currentIndex];
                    break;
                case S_OP:
                    ans += specialOperators[currentIndex];
                    break;
                case OPR:
                    ans += "(" + functions[currentIndex] + ")";
                default:
            }
        }
        return ans;
    }

    public Function replaceVariables (double x, double y){
        int startIndex = 0;
        String tAns = "";
        for (int i = 0; i < formula.length; i++) {
            TypeWrapper t = formula [i];
            if (t.getType() == Type.VAR) {
                tAns += this.toRangeString(startIndex, i);
                i++;
                startIndex = i;
                switch (variables[t.getIndex()]) {
                    case X : 
                        tAns += x;
                        break;
                    case Y :
                        tAns += y;
                        break;
                }
            }
        }
        tAns += this.toRangeString(startIndex, formula.length);
        return new Function (tAns);
    }

}
