public class Log extends Node {
    Node arg;
    double base = Math.E;

    Log(Node n) {
        arg = n;
    }

    Log(double c) {
        arg = new Constant(c);
    }

    Log(Node n, double b) {
        arg = n;
        base = b;
    }

    Log(double c, double b) {
        arg = new Constant(c);
        base = b;
    }

    @Override
    double evaluate() {
        double argVal = arg.evaluate();
        return Math.log(argVal) / Math.log(base);
    }

    int getArgumentsCount() {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sign < 0)
            b.append("-");

        if (base == Math.E)
            b.append("ln(");
        else
            b.append("log_" + base + "(");

        String argString = arg.toString();
        b.append(argString);

        b.append(")");

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        Prod result =  new Prod(new Power(arg, -1));
        if (base != Math.E)
            result.mul(new Power(new Log(base), -1));
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
