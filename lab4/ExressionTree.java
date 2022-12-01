import java.util.ArrayList;

public class ExressionTree {

    private ExpressionTreeNode root;

    private int size;

    int getSize() {
        return size;
    }

    private String[] tokens;

    private ArrayList<String> traversalArray = new ArrayList<>();

    enum TokenType {
        L_BRACKET,
        R_BRACKET,
        NUMBER,
        OPERATOR;
    }

    enum Form {
        POSTFIX,
        PREFIX,
        INFIX;
    }

    public TokenType getTokenType(String s) {
        switch (s) {
            case "(":
                return TokenType.L_BRACKET;
            case ")":
                return TokenType.R_BRACKET;
            case "+":
            case "/":
            case "-":
            case "*":
                return TokenType.OPERATOR;
            default:
                return TokenType.NUMBER;
        }
    }

    class ExpressionTreeNode {
        TokenType type;
        ExpressionTreeNode left;
        ExpressionTreeNode right;
        String value;

        public ExpressionTreeNode(TokenType type, ExpressionTreeNode left, ExpressionTreeNode right, String value) {
            this.type = type;
            this.left = left;
            this.right = right;
            this.value = value;
        }

    }

    private ExpressionTreeNode makeNodeInfix(int l, int r) {
        if (l == r) {
            if (getTokenType(tokens[l]) != TokenType.NUMBER) {
                throw new IllegalStateException("ожидалось значение");
            } else {
                return new ExpressionTreeNode(TokenType.NUMBER, null, null, tokens[l]);
            }
        } else {
            int br = 0, fp = -1, fm = -1;
            for (int i = l; i <= r; i++) {
                if (br < 0) {
                    throw new IllegalStateException("неправильная скобочная последовательность");
                } else {
                    TokenType type = getTokenType(tokens[i]);
                    if (type == TokenType.L_BRACKET) {
                        br++;
                    } else if (type == TokenType.R_BRACKET) {
                        br--;
                    }
                    if (br != 0) {
                        continue;
                    }
                    if (type == TokenType.OPERATOR) {
                        switch (tokens[i]) {
                            case "+":
                            case "-":
                                fp = fp == -1 ? i : fp;
                                break;
                            case "*":
                            case "/":
                                fm = fm == -1 ? i : fm;
                                break;
                        }
                    }
                    if (fp != -1) {
                        break;
                    }
                }
            }
            if (fp != -1) {
                ExpressionTreeNode node = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[fp]);
                node.left = makeNodeInfix(l, fp - 1);
                node.right = makeNodeInfix(fp + 1, r);
                return node;
            } else if (fm != -1) {
                ExpressionTreeNode node = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[fm]);
                node.left = makeNodeInfix(l, fm - 1);
                node.right = makeNodeInfix(fm + 1, r);
                return node;
            } else if (getTokenType(tokens[l]) == TokenType.L_BRACKET && getTokenType(tokens[r]) == TokenType.R_BRACKET) {
                return makeNodeInfix(l + 1, r - 1);
            }
            throw new IllegalStateException("неправильно расставлены знаки операций");
        }
    }

    private int ind = 0;

    private void makeNodePrefix(ExpressionTreeNode node) {
        if (ind == size) {
            return;
        }
        if (getTokenType(tokens[ind]) == TokenType.OPERATOR) {
            node.left = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[ind]);
            ind++;
            makeNodePrefix(node.left);
        } else {
            node.left = new ExpressionTreeNode(TokenType.NUMBER, null, null, tokens[ind]);
            ind++;
        }

        if (getTokenType(tokens[ind]) == TokenType.OPERATOR) {
            node.right = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[ind]);
            ind++;
            makeNodePrefix(node.right);
        } else {
            node.right = new ExpressionTreeNode(TokenType.NUMBER, null, null, tokens[ind]);
            ind++;
        }
    }

    private void makeNodePostfix(ExpressionTreeNode node) {
        if (ind < 0) {
            return;
        }

        if (getTokenType(tokens[ind]) == TokenType.OPERATOR) {
            node.right = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[ind]);
            ind--;
            makeNodePostfix(node.right);
        } else {
            node.right = new ExpressionTreeNode(TokenType.NUMBER, null, null, tokens[ind]);
            ind--;
        }

        if (getTokenType(tokens[ind]) == TokenType.OPERATOR) {
            node.left = new ExpressionTreeNode(TokenType.OPERATOR, null, null, tokens[ind]);
            ind--;
            makeNodePostfix(node.left);
        } else {
            node.left = new ExpressionTreeNode(TokenType.NUMBER, null, null, tokens[ind]);
            ind--;
        }
    }

    public void setExpression(String exp, Form form) {
        tokens = exp.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("(") || tokens[i].equals(")")) {
            } else {
                size++;
            }
        }
        if (tokens.length == 0) {
            return;
        }
        switch (form) {
            case INFIX:
                root = makeNodeInfix(0, tokens.length - 1);
                break;
            case PREFIX:
                ind = 1;
                root = new ExpressionTreeNode(getTokenType(tokens[0]), null, null, tokens[0]);
                if (tokens.length > 1) {
                    makeNodePrefix(root);
                }
                break;
            case POSTFIX:
                ind = tokens.length - 2;
                root = new ExpressionTreeNode(getTokenType(tokens[tokens.length - 1]), null, null, tokens[tokens.length - 1]);
                if (tokens.length > 1) {
                    makeNodePostfix(root);
                }
                break;
        }
    }

    public String[] getPreorderTraversalArray() {
        traversalArray.clear();
        if (root == null) {
            return new String[0];
        }
        preorderTraversal(root);
        return traversalArray.toArray(new String[0]);
    }

    private void preorderTraversal(ExpressionTreeNode node) {
        traversalArray.add(node.value);
        if (node.left != null) {
            preorderTraversal(node.left);
        }
        if (node.right != null) {
            preorderTraversal(node.right);
        }
    }

    public String[] getInorderTraversalArray() {
        traversalArray.clear();
        if (root == null) {
            return new String[0];
        }
        inorderTraversal(root);
        return traversalArray.toArray(new String[0]);
    }

    private void inorderTraversal(ExpressionTreeNode node) {
        if (node.left != null) {
            inorderTraversal(node.left);
        }
        traversalArray.add(node.value);
        if (node.right != null) {
            inorderTraversal(node.right);
        }
    }

    public String[] getInorderTraversalArrayWithBrackets() {
        traversalArray.clear();
        if (root == null) {
            return new String[0];
        }
        inorderTraversalWithBrackets(root);
        return traversalArray.toArray(new String[0]);
    }

    private void inorderTraversalWithBrackets(ExpressionTreeNode node) {

        if (node.left != null) {
            if ((node.value.equals("*") || node.value.equals("/")) && (node.left.value.equals("+") || node.left.value.equals("-"))) {
                traversalArray.add("(");
            }
            inorderTraversalWithBrackets(node.left);
            if ((node.value.equals("*") || node.value.equals("/")) && (node.left.value.equals("+") || node.left.value.equals("-"))) {
                traversalArray.add(")");
            }
        }
        traversalArray.add(node.value);
        if (node.right != null) {
            if ((node.value.equals("*") || node.value.equals("/")) && (node.right.value.equals("+") || node.right.value.equals("-"))) {
                traversalArray.add("(");
            }
            inorderTraversalWithBrackets(node.right);
            if ((node.value.equals("*") || node.value.equals("/")) && (node.right.value.equals("+") || node.right.value.equals("-"))) {
                traversalArray.add(")");
            }
        }
    }

    public String[] getPostorderTraversalArray() {
        traversalArray.clear();
        if (root == null) {
            return new String[0];
        }
        postorderTraversal(root);
        return traversalArray.toArray(new String[0]);
    }

    private void postorderTraversal(ExpressionTreeNode node) {
        if (node.left != null) {
            postorderTraversal(node.left);
        }
        if (node.right != null) {
            postorderTraversal(node.right);
        }
        traversalArray.add(node.value);
    }

    private String arrayToString(String[] s){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++){
            sb.append(s[i]).append(' ');
        }
        return sb.toString();
    }

    public String toInfix(){
        return arrayToString(getInorderTraversalArrayWithBrackets());
    }

    public String toPrefix(){
        return arrayToString(getPreorderTraversalArray());
    }

    public String toPostfix(){
        return arrayToString(getPostorderTraversalArray());
    }
}
