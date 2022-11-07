import java.util.ArrayList;
import java.util.List;

public class Sum extends Node {

    List<Node> args = new ArrayList<>();

    Sum() {}

    Sum(Node n1, Node n2) {
        args.add(n1);
        args.add(n2);
    }

    Sum(double c, Node n) {
        args.add(new Constant(c));
        args.add(n);
    }

    Sum(Node n, double c) {
        args.add(n);
        args.add(new Constant(c));
    }

    Sum add(Node n) {
        args.add(n);
        return this;
    }

    Sum add(double c) {
        args.add(new Constant(c));
        return this;
    }

    Sum add(double c, Node n) {
        Node mul = new Prod(c, n);
        args.add(mul);
        return this;
    }

    @Override
    double evaluate() {
        double result = 0;
        // oblicz sumę wartości zwróconych przez wywołanie evaluate składników sumy
        for (Node arg : args) {
            result += arg.evaluate();
        }
        return sign*result;
    }

    int getArgumentsCount() {return args.size();}

    public String toString() {
        StringBuilder b =  new StringBuilder();

        if (sign < 0)
            b.append("-(");

        for (int i = 0; i < getArgumentsCount(); ++i) {
            if (i != 0) {
                b.append(" + ");
            }
            Node arg = args.get(i);
            int argSign = arg.getSign();
            boolean useBracket = false;
            if (argSign < 0)
                useBracket = true;
            if (useBracket)
                b.append("(");
            String argString = arg.toString();
            b.append(argString);
            if (useBracket)
                b.append(")");
        }

        if (sign < 0)
            b.append(")");

        return b.toString();
    }

    @Override
    Node diff(Variable var) {
        List<Node> sum = new ArrayList<>();
        for (Node arg : args) {
            Node diff = arg.diff(var);
            if (!diff.isZero())
                sum.add(diff);
        }

        if (sum.size() == 0) {
            return new Constant(0);
        } else if (sum.size() == 1) {
            Node soleElement = sum.get(0);
            return soleElement;
        } else {
            Sum result = new Sum();
            for (Node element : sum) {
                result.add(element);
            }
            return result;
        }
    }

    @Override
    boolean isZero() {
        if (getArgumentsCount() == 0)
            return true;
        else
            return false;
    }

    @Override
    Node simplify() {
        List<Node> sum = new ArrayList<>();

        double constants = 0;
        for (Node arg : args) {
            if (arg instanceof Constant) {
                constants += arg.evaluate();
            } else if (arg.isZero()) {
                continue;
            } else {
                Node simplifiedArg = arg.simplify();
                sum.add(simplifiedArg);
            }
        }
        if (constants != 0)
            sum.add(new Constant(constants));

        if (sum.size() == 0) {
            return new Constant(0);
        } else if (sum.size() == 1) {
            Node soleElement = sum.get(0);
            return soleElement;
        } else {
            Sum result = new Sum();
            for (Node element : sum) {
                result.add(element);
            }
            return result;
        }
    }
}