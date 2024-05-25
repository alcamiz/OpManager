package operation;

public class RecursionTools {
    
    public static interface RecursiveHelper {
        void run (double x, double y, NewDoubleWrapper ans);
    }

    public static class OperationHelper {
        public RecursiveHelper recursive;
    }

    public static class NewDoubleWrapper {
        private double value;
        public NewDoubleWrapper (double value) {this.value = value;}

        public double getValue () {return value;}

        public void modifyValue (double value) {this.value = value;}
    }

    public static double rClassicEuler (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        NewDoubleWrapper m_ans = new NewDoubleWrapper(0);
        final double epsilon = Math.pow(10,-((Double.toString(stepSize).length())-(Integer.toString((int)stepSize).length())+1));
        OperationHelper tool = new OperationHelper();
        tool.recursive = (x,y,ans) -> {
            if (Math.abs(pX-x) < epsilon) {
                ans.modifyValue(y);
                return;
            }
            tool.recursive.run(x + stepSize, y + f.solve(x,y) * stepSize, ans);
        };
        tool.recursive.run(iX,iY,m_ans);
        return m_ans.getValue();
    }

    public static double rEnhancedEuler (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        NewDoubleWrapper m_ans = new NewDoubleWrapper(0);
        final double epsilon = Math.pow(10,-((Double.toString(stepSize).length())-(Integer.toString((int)stepSize).length())+1));
        OperationHelper tool = new OperationHelper();
        tool.recursive = (x,y,ans) -> {
            if (Math.abs(pX-x) < epsilon) {
                ans.modifyValue(y);
            }
            else {tool.recursive.run(x + stepSize, y + (f.solve(x, y) + f.solve(x + stepSize, y + f.solve(x,y) * stepSize))* stepSize / 2.0, ans);}
        };
        tool.recursive.run(iX,iY,m_ans);
        return m_ans.getValue();
    }

    public static double rClassicRungeKutta (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        final double epsilon = Math.pow(10,-((Double.toString(stepSize).length())-(Integer.toString((int)stepSize).length())+1));
        NewDoubleWrapper m_ans = new NewDoubleWrapper (0);
        OperationHelper tool = new OperationHelper();
        tool.recursive = (x,y,ans) -> {
            if (Math.abs(pX-x) < epsilon) {
                ans.modifyValue(y);
                return;
            }
            double k1 = stepSize * f.solve(x,y);
            double k2 = stepSize * f.solve(x + (stepSize/2), y + (k1/2));
            double k3 = stepSize * f.solve(x + (stepSize/2), y + (k2/2));
            double k4 = stepSize * f.solve(x + stepSize, y + k3);
            tool.recursive.run (x+stepSize,y+((k1+2*k2+2*k3+k4)/6.0),ans);
        };
        tool.recursive.run(iX,iY,m_ans);
        return m_ans.getValue();
    }

    public static double classicEuler (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        int j = 0;
        int goal = (int) Math.round(pX / stepSize);
        double y = iY;
        for (double x = iX; j < goal; x += stepSize) {
            y += f.solve(x, y) * stepSize;
            j++;
        }
        return y;
    }

    public static double enhancedEuler (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        int j = 0;
        int goal = (int) Math.round(pX / stepSize);
        double y = iY;
        for (double x = iX; j < goal; x += stepSize) {
            y += (f.solve(x, y) + f.solve(x + stepSize, y + f.solve(x,y) * stepSize))* stepSize / 2.0;
            j++;
        }
        return y;
    }

    public static double classicRungeKutta (String s, double stepSize, double iX, double iY, double pX) {
        Function f = new Function (s);
        double y = iY;
        final double epsilon = Math.pow(10,-((Double.toString(stepSize).length())-(Integer.toString((int)stepSize).length())+1));
        for (double x = iX; Math.abs(pX-x) > epsilon; x += stepSize) {
            double k1 = stepSize * f.solve(x,y);
            double k2 = stepSize * f.solve(x + (stepSize/2), y + (k1/2));
            double k3 = stepSize * f.solve(x + (stepSize/2), y + (k2/2));
            double k4 = stepSize * f.solve(x + stepSize, y + k3);
            y += ((k1+2*k2+2*k3+k4)/6.0);
        }   
        return y;
    }

}
