import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Exp extends Node {
    double base = Math.E;
    Node arg;

    Exp(Node n) {
        arg = n;
    }

    Exp(double c) {
        arg = new Constant(c);
    }

    @Override
    double evaluate() {
        double argVal = arg.evaluate();
        return Math.exp(argVal);
    }

    int getArgumentsCount() {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sign < 0)
            b.append("-");

        b.append("exp(");

        String argString = arg.toString();
        b.append(argString);

        b.append(")");

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        Prod result = new Prod(this);
        result.mul(arg.diff(var));
        return result;
    }

    @Override
    boolean isZero() {
        // exp() is different from 0 for all arguments
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
