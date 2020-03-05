package utils;

import AllChess.Chess;
import java.util.Stack;

public class AiStack {

    //保存已下的棋的公共栈
    public static Stack<Chess> stack;


    //将棋子出栈
    public static Chess OutPutStack() {
        if(!stack.empty()) {
            return stack.pop();
        } else {
            return null;
        }
    }
    //清空栈
    public static void ClearStack() {
        stack = new Stack<Chess>();
    }
    //逆置栈
    public static void ReverseStack() {
        Stack<Chess> Worker = new Stack<>();
        while(!stack.empty()) {
            Worker.push(stack.pop());
        }
        stack = Worker;
    }
    //悔棋步骤将栈重新设置
    public static void SetStack(Stack<Chess> nowstack) {
        if(stack.empty()) {
            while(!nowstack.empty()) {
                Chess s = nowstack.pop();
                stack.push(s);
            }

        } else {
            System.out.println("工作栈不为空！");
        }
    }
}
