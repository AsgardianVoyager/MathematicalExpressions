import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Power extends Node {
    Node arg;
    double p;

    Power(Node n, double p) {
        arg = n;
        this.p = p;
    }

    Power(double c, double p) {
        arg = new Constant(c);
        this.p = p;
    }

    @Override
    double evaluate() {
        double argVal = arg.evaluate();
        return Math.pow(argVal, p);
    }

    int getArgumentsCount() {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sign < 0)
            b.append("-");

        int argSign = arg.getSign();
        int cnt = arg.getArgumentsCount();
        boolean useBracket = false;
        if (argSign < 0 || cnt > 1)
            useBracket = true;
        if (useBracket)
            b.append("(");

        String argString = arg.toString();
        b.append(argString);

        if (useBracket)
            b.append(")");

        b.append("^");

        DecimalFormat format = new DecimalFormat("0.#####", new DecimalFormatSymbols(Locale.US));
        String pString = format.format(p);
        b.append(pString);

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        Prod result =  new Prod(sign*p, new Power(arg,p-1));
        result.mul(arg.diff(var));
        return result;
    }

    @Override
    boolean isZero() {
        return arg.isZero();
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