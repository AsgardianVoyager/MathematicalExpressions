public class Cos extends Node {
    Node arg;

    Cos(Node n) {
        arg = n;
    }

    Cos(double c) {
        arg = new Constant(c);
    }

    @Override
    double evaluate() {
        double argVal = arg.evaluate();
        return Math.cos(argVal);
    }

    int getArgumentsCount() {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sign < 0)
            b.append("-");

        b.append("cos(");

        String argString = arg.toString();
        b.append(argString);

        b.append(")");

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        Prod result =  new Prod(-1, new Sin(arg));
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
