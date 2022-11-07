import org.junit.Test;

import java.util.Locale;

public class AllTests {
    @Test
    public void testPower() {
        Variable x = new Variable("x");
        Node expr = new Power(x, 2);
        System.out.println(expr.toString());
    }

    @Test
    public void buildAndPrint() {
        Variable x = new Variable("x");
        Node expr = new Sum()
                .add(2.1, new Power(x, 3))
                .add(new Power(x, 2))
                .add(-2, x)
                .add(7);
        System.out.println(expr.toString());
    }

    @Test
    public void buildAndEvaluate() {
        Variable x = new Variable("x");
        Node expr = new Sum()
                .add(new Power(x, 3))
                .add(-2, new Power(x, 2))
                .add(-1, x)
                .add(2);

        System.out.println(expr.toString());

        for (double v = -5; v < 5; v += 0.1) {
            x.setValue(v);
            System.out.printf(Locale.US, "f(%f)=%f\n", v, expr.evaluate());
        }
    }

    @Test
    public void defineCircle() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x, 2))
                .add(new Power(y, 2))
                .add(8, x)
                .add(4, y)
                .add(16);
        System.out.println(circle.toString());

        int pointsInCircle = 0;
        while (pointsInCircle < 100) {
            double xv = 100 * (Math.random() - .5);
            double yv = 100 * (Math.random() - .5);
            x.setValue(xv);
            y.setValue(yv);
            double fv = circle.evaluate();
            if (fv < 0) {
                System.out.println(String.format("Punkt (%f,%f) leży %s koła %s", xv, yv, "wewnątrz", circle.toString()));
                ++pointsInCircle;
            }
        }
    }

    @Test
    public void diffPoly() {
        Variable x = new Variable("x");
        Node exp = new Sum()
                .add(2, new Power(x, 3))
                .add(new Power(x, 2))
                .add(-2, x)
                .add(7)
                .add(0);
        System.out.print("exp = ");
        System.out.println(exp.simplify().toString());

        Node d = exp.diff(x);
        System.out.print("d(exp)/dx = ");
        System.out.println(d.simplify().toString());
    }

    @Test
    public void diffCircle() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x,2))
                .add(new Power(y,2))
                .add(8,x)
                .add(4,y)
                .add(16);
        System.out.print("f(x,y) = ");
        System.out.println(circle.toString());

        Node dx = circle.diff(x);
        System.out.print("df(x,y)/dx = ");
        System.out.println(dx.simplify().toString());
        System.out.print("df(x,y)/dy = ");
        Node dy = circle.diff(y);
        System.out.println(dy.simplify().toString());
    }

    @Test
    public void testSinAndCos() {
        Variable x = new Variable("x");

        Node sin = new Sin(x);
        System.out.println(sin.toString());

        System.out.println("df/dx = " + sin.diff(x).simplify());

        System.out.println();

        Node cos = new Cos(x);
        System.out.println(cos.toString());

        System.out.println("df/dx = " + cos.diff(x).simplify());

        System.out.println();

        x.setValue(0);
        System.out.println("sin(0) = " + sin.evaluate());
        System.out.println("cos(0) = " + cos.evaluate());

        x.setValue(Math.PI);
        System.out.println("sin(PI) = " + sin.evaluate());
        System.out.println("cos(PI) = " + cos.evaluate());

        System.out.println();

        sin = new Sin(0);
        System.out.println(sin.toString());
        System.out.println(sin.simplify().toString());
    }

    @Test
    public void testLog() {
        Variable x = new Variable("x");

        Node ln = new Log(x);
        System.out.println(ln.toString());

        System.out.println("df/dx = " + ln.diff(x).simplify());

        x.setValue(Math.E);
        System.out.println("ln(E) = " + ln.evaluate());

        System.out.println();

        Node log2 = new Log(x, 2);
        System.out.println(log2.toString());

        System.out.println("df/dx = " + log2.diff(x).simplify());

        x.setValue(8);
        System.out.println("log_2(8) = " + log2.evaluate());
    }

    @Test
    public void testExp() {
        Variable x = new Variable("x");

        Node exp = new Exp(x);
        System.out.println(exp.toString());

        System.out.println("df/dx = " + exp.diff(x).simplify());

        x.setValue(1);
        System.out.println("exp(1) = " + exp.evaluate());
    }
}