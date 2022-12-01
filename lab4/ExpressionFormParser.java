import java.util.Arrays;

public class ExpressionFormParser {
    public static void main(String[] args) {
        ExressionTree expression = new ExressionTree();
        expression.setExpression("( 1 + 2 ) * 3 + ( 4 * ( 5 * ( A + B ) + 6 ) + 7 )", ExressionTree.Form.INFIX);
        System.out.println(expression.toInfix());
        System.out.println(expression.toPrefix());
        System.out.println(expression.toPostfix());
    }
}
