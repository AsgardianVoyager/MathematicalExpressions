public class Main {
    public static void main(String[] args) {
        Variable x = new Variable("x");
        Node expr = new Prod(new Prod(13, new Prod(14, new Prod(100, 100))), new Power(13, 2));

        System.out.println(expr.simplify().toString());
        System.out.println(expr.diff(x));
    }
}