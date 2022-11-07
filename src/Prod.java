import java.util.ArrayList;
import java.util.List;

public class Prod extends Node {
    List<Node> args = new ArrayList<>();

    Prod() {}

    Prod(Node n1) {
        args.add(n1);
    }

    Prod(double c) {
        // wywołaj konstruktor jednoargumentowy przekazując new Constant(c)
        this(new Constant(c));
    }

    Prod(Node n1, Node n2) {
        args.add(n1);
        args.add(n2);
    }

    Prod(double c, Node n) {
        // wywołaj konstruktor dwuargumentowy
        this(new Constant(c), n);
    }

    Prod(double c1, double c2) {
        this(new Constant(c1), new Constant(c2));
    }

    Prod mul(Node n){
        args.add(n);
        return this;
    }

    Prod mul(double c) {
        args.add(new Constant(c));
        return this;
    }

    @Override
    double evaluate() {
        double result = 1;
        // oblicz iloczyn czynników wołając ich metodę evaluate
        for (Node arg : args) {
            result *= arg.evaluate();
        }
        return sign*result;
    }

    int getArgumentsCount() {return args.size();}

    public String toString() {
        StringBuilder b =  new StringBuilder();

        if (sign < 0)
            b.append("-");

        for (int i = 0; i < getArgumentsCount(); ++i) {
            if (i != 0)
                b.append("*");
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

        return b.toString();
    }

    Node diff(Variable var) {
        List<Node> sum = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            List<Node> product = new ArrayList<>();
            for (int j = 0; j < args.size(); j++){
                Node f = args.get(j);
                if (f.isZero()) {
                    product.clear();
                    break;
                }
                if (j == i) {
                    Node diff = f.diff(var);
                    if (diff.isZero()) {
                        product.clear();
                        break;
                    }
                    product.add(diff);
                }
                else
                    product.add(f);
            }

            if (product.size() == 0) {
                continue;
            }
            else if (product.size() == 1) {
                Node soleElement = product.get(0);
                sum.add(soleElement);
            }
            else {
                Prod m = new Prod();
                for (Node element : product) {
                    m.mul(element);
                }
                sum.add(m);
            }
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

    boolean isZero() {
        for (Node arg : args) {
            if (arg.isZero())
                return true;
        }
        if (getArgumentsCount() == 0)
            return true;
        else
            return false;
    }

//    @Override
//    Node simplify() {
//        List<Node> product = new ArrayList<>();
//        double constants = 1;
//        for (Node arg : args) {
//            if (arg instanceof Constant) {
//                constants *= arg.evaluate();
//            } else if (arg.isZero()) {
//                product.clear();
//                break;
//            } else if (arg instanceof Prod) {
//                Node simplifiedArg = arg.simplify();
//                if (simplifiedArg instanceof Prod) {
//                    if (((Prod) simplifiedArg).args.get(0) instanceof Constant) {
//                        constants *= ((Prod) simplifiedArg).args.get(0).evaluate();
//                        ((Prod) simplifiedArg).args.remove(0);
//                    }
//                    product.add(simplifiedArg);
//                } else {
//                    product.add(simplifiedArg);
//                }
//            } else {
//                Node simplifiedArg = arg.simplify();
//                product.add(simplifiedArg);
//            }
//        }
//        if (constants != 1) {
//            product.add(0, new Constant(constants));
//        }
//
//        if (product.size() == 0) {
//            return new Constant(0);
//        } else if (product.size() == 1) {
//            Node soleElement = product.get(0);
//            return soleElement;
//        } else {
//            Prod result = new Prod();
//            for (Node element : product) {
//                result.mul(element);
//            }
//            return result;
//        }
//    }
//}

    @Override
    Node simplify() {
        List<Node> product = new ArrayList<>();
        double constants = 1;
        for (Node arg : args) {
            if (arg instanceof Constant) {
                constants *= arg.evaluate();
            } else if (arg.isZero()) {
                product.clear();
                break;
            } else {
                Node simplifiedArg = arg.simplify();
                if (simplifiedArg instanceof Constant) {
                    constants *= simplifiedArg.evaluate();
                } else if (simplifiedArg instanceof Prod) {
                    for (Node innerArg : ((Prod) simplifiedArg).args) {
                        if (innerArg instanceof Constant) {
                            constants *= innerArg.evaluate();
                        } else {
                            product.add(innerArg);
                        }
                    }
                } else {
                    product.add(simplifiedArg);
                }
            }
        }
        if (constants != 1) {
            product.add(0, new Constant(constants));
        }

        if (product.size() == 0) {
            return new Constant(0);
        } else if (product.size() == 1) {
            Node soleElement = product.get(0);
            return soleElement;
        } else {
            Prod result = new Prod();
            for (Node element : product) {
                result.mul(element);
            }
            return result;
        }
    }
}