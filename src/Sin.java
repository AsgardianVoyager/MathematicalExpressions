import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Sin extends Node {
    Node arg;

    Sin(Node n) {
        arg = n;
    }

    Sin(double c) {
        arg = new Constant(c);
    }

    @Override
    double evaluate() {
        double argVal = arg.evaluate();
        return Math.sin(argVal);
    }

    int getArgumentsCount() {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sign < 0)
            b.append("-");

        b.append("sin(");

        String argString = arg.toString();
        b.append(argString);

        b.append(")");

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        Prod result =  new Prod(new Cos(arg));
        result.mul(arg.diff(var));
        return result;
    }

    @Override
    boolean isZero() {
        return false;
    }

    @Override
    Node simplify() {
        if (isZero()) {
            return new Constant(0);
        } else if (arg instanceof Constant) {
            return new Constant(evaluate());
        } else {
            return this;
        }
    }
}
