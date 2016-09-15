import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by 28713 on 2016/9/11.
 */
public class Calculator extends JFrame{

    private Stack<Double> operandStack= new Stack<>();
    private Stack<String> operatorStack = new Stack<>();

    private Calculator(){

        setTitle("计算器");
        setSize(266,340);
        Container c=getContentPane();
        c.setLayout(null);


        JTextArea jt=new JTextArea(100,100);
        jt.setFont(new Font("Aria",Font.BOLD,32));
        jt.setLineWrap(true);
        JScrollPane sp=new JScrollPane(jt);
        jt.setCaretPosition(jt.getDocument().getLength());
        sp.setBounds(0,0,250,100);
        c.add(sp);

        JPanel p=new JPanel();
        p.setLayout(new GridLayout(5,4,0,0));

        p.setBounds(0,100,250,200);
        String[] num={"(",")","AC","/","7","8","9","*","4","5","6","-","1","2","3","+","0",".","DEL","="};
        JButton[] jb=new JButton[20];
        for(int i=0;i<20;i++){
            jb[i]=new JButton(num[i]);
            p.add(jb[i]);
        }
        c.add(p);

        for(int i=0;i<18;i++){
            if(i!=2){
                final int j=i;
                jb[i].addActionListener(e-> jt.append(num[j]));
            }
        }

        jb[2].addActionListener(e->{
            jt.setText("");
            operandStack.clear();
            operatorStack.clear();
        });
        jb[18].addActionListener(e->{
            try{
                jt.setText(jt.getText().substring(0,jt.getText().length()-1));
            }catch(Exception ignored) { }//忽略这个异常   IDEA就是好用！！！
        });
        jb[19].addActionListener(e->{
            try{
                double x= calculate(jt.getText()+"#");
                jt.setText("");
                jt.append(String.valueOf(x));
            }catch(Exception ex){
                jt.setText("ERROR!");
            }
        });
        //禁止文本域的enter换行
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        jt.getInputMap().put(enter, "none");

        this.getRootPane().setDefaultButton(jb[19]);
        //太累！不想研究了
        //下面的，不知道主体是什么，当只有焦点在那个主体上才会响应，哎，不知道怎么弄。
        /*c.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    System.out.println("adssdf");
                    double x= calculate(jt.getText()+"#");
                    jt.setText("");
                    jt.append(String.valueOf(x));
                }
            }
        });*/
        //不知道为什么响应不了键盘事件？？？
        //不知道为什么下面的的这个不行？？？

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void calculate(){
        String b = operatorStack.pop();
        double c = operandStack.pop();
        double d = operandStack.pop();
        double e;
        if (b.equals("+")) {
            e = d + c;
            operandStack.push(e);
        }
        if (b.equals("-")) {
            e = d - c;
            operandStack.push(e);
        }
        if (b.equals("*")) {
            e = d * c;
            operandStack.push(e);
        }
        if (b.equals("/")) {
            e = d / c;
            operandStack.push(e);
        }
    }

    private Double calculate(String text){
        HashMap<String,Integer> precede=new HashMap<>();
        precede.put("(",0);
        precede.put(")",0);
        precede.put("/",2);
        precede.put("*",2);
        precede.put("-",1);
        precede.put("+",1);
        precede.put("#",0);

        operatorStack.push("#");

        int flag=0;
        for(int i=0;i<text.length();i++){
            String a=String.valueOf(text.charAt(i));
            if(!a.matches("[0-9.]")){
                if(flag!=i)
                    operandStack.push(Double.parseDouble(text.substring(flag,i)));
                flag=i+1;
                while(!(a.equals("#")&&operatorStack.peek().equals("#"))){
                    if(precede.get(a)>precede.get(operatorStack.peek())||a.equals("(")){
                        operatorStack.push(a);
                        break;
                    }else {
                        if(a.equals(")")) {
                            while(!operatorStack.peek().equals("("))
                                calculate();
                            operatorStack.pop();
                            break;
                        }
                        calculate();
                    }
                }

            }
        }

        return(operandStack.pop());
    }


    public static void main(String[] args){
        new Calculator();
    }
}