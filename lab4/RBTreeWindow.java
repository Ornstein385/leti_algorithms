import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Window {

    static JFrame frame = new JFrame("АиСД лабораторная 4");

    static int width = 1920, height = 1080;

    static JButton insertButton = new JButton("вставить"),
            deleteButton = new JButton("удалить");

    static JTextField insertField = new JTextField(),
            deleteField = new JTextField();

    static RedBlackTree tree = new RedBlackTree();

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++){
            tree.insertNode((int) (Math.random()*2000 -1000));
        }

        frame.setBounds(0, 0, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        insertButton.setBounds(100, 0, 100, 30);
        deleteButton.setBounds(100, 30, 100, 30);

        insertField.setBounds(0, 0, 100, 30);
        deleteField.setBounds(0, 30, 100, 30);

        frame.add(insertField);
        frame.add(deleteField);

        frame.add(insertButton);
        frame.add(deleteButton);

        frame.add(new Plane());
        frame.setVisible(true);

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tree.insertNode(Integer.parseInt(insertField.getText()));
                    insertField.setText("");
                    //frame.add(new Plane());
                    frame.repaint();
                } catch (Exception ex) {
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tree.deleteNode(Integer.parseInt(deleteField.getText()));
                    deleteField.setText("");
                    //frame.add(new Plane());
                    frame.repaint();
                } catch (Exception ex) {
                }
            }
        });

        frame.add(new Plane());
    }
}

class Plane extends JComponent {
    int box = 10;

    public void paintComponent(Graphics painter) {
        int h = Window.tree.getMaxHeight();
        int w = (int) Math.pow(2, h - 1);
        box = Math.min(Window.height / h, Window.width / w);
        if (box < 50) {
            box = 50;
        }
        if (box > 200) {
            box = 200;
        }
        drawTree(Window.tree.root, painter, Window.height / h / 2, Window.height / (h + 1), 100, Window.width - 100);
    }

    private void drawTree(Node node, Graphics painter, int h, int step, int l, int r) {
        painter.setColor(Color.BLACK);
        painter.setFont(new Font("Courier", Font.BOLD, 15));
        if (node.left != null) {
            painter.drawLine((l + r) / 2, h, l + (r - l) / 4, h + step);
            drawTree(node.left, painter, h + step, step, l, (l + r) / 2);
        }
        if (node.right != null) {
            painter.drawLine((l + r) / 2, h, r - (r - l) / 4, h + step);
            drawTree(node.right, painter, h + step, step, (l + r) / 2, r);
        }
        painter.setColor(node.color);
        painter.fillOval((r + l) / 2 - box / 4, h - box / 4, box / 2, box / 2);
        painter.setColor(Color.CYAN);
        painter.drawString(String.valueOf(node.value), (l + r) / 2 - (int) Math.log10(Math.abs(node.value)) * 5, h + 5);
        painter.setColor(Color.BLACK);
    }
}