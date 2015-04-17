package com.calculator;

import java.util.Stack;  
/* Приложение написано на языке Java 1.6 без использования дополнительных библиотек
 Мы будем идти слева на право, добавляя операнды в один стек, а операции в другой. 
При каждом добавлении новой операции мы будем пытаться вытолкнуть из стека старые,
руководствуясь приоритетами операций. 
Created by Stasya on 15.12.2014.
*/

public class CalculatorImpl implements Calculator {		
	private int ind=0;	                              // индекс элемента входной строки
	public String evaluate (String s) {
	s='('+s+')';
	Stack<Double> Operands=new Stack<Double>();   // стек операндов
	Stack<Character> Functions= new Stack<Character>();   // стек операций
	Object token=new Object();    // обрабатываемый элемент
	Object predToken='K';         // предыдуший обрабатываемый элемент
	String result = null;	
	
	try{
	do {
		token=getToken (s);
		// если встетится сочетание "(+" или "(-", то заменить на "(0+" или "(0-", т.е. превращаем унарную операцию в бинарную
		if (token instanceof Character && predToken instanceof Character && (Character) predToken == '(' && 
				((Character) token == '+' || (Character) token == '-')) {
			Operands.push((double) 0);
		}
		if (token instanceof Double) {  // Если операнд 	
            Operands.push((Double) token); // то добавляем в стек
        }						
        else if (token instanceof Character) { // Если операция
        	if ((Character) token == ')')  {
                // Скобка - исключение из правил. выталкивает все операции до первой открывающейся скобки
                while (Functions.size() > 0 && Functions.peek() != '(') {
                    popFunction(Operands, Functions);
                }
                Functions.pop(); // Удаляем саму скобку "("
            }
            else  {
                while (canPop((Character)token, Functions)) { // Если можно вытолкнуть
                    popFunction(Operands, Functions); // то выталкиваем
                }
                Functions.push((Character) token); // Добавляем новую операцию в стек                
            }
        }
        predToken = token; 
	} while (token!=null);
	
	if (Operands.size() > 1 || Functions.size() > 0) {
        throw new Exception("Ошибка в разборе выражения");
	}
	
	result = Double.toString(round (Operands.pop(), 4));		
	
	}
	catch (Exception e)	{
		result="null";
		}
    return result;
	}
	// округляем результат до до 4-го знака после запятой
	private double round (double value, int scale) {
	      return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
	   }
	// берем символ с индексом ind из входной строки
	private Object getToken (String s) {
	    if (ind == s.length()) { // конец строки
	        return null;
	    }
	    if (Character.isDigit(s.charAt(ind)))  // если символ операнд
	        return Double.parseDouble(readDouble(s));
	    else
	        return readFunction(s);		 // если символ операция
	}	
	// если символ является цифрой или числом переводидм в тип Double
	private String readDouble(String s)	{
	    String res = "";
	    while (ind < s.length() && (Character.isDigit(s.charAt(ind)) || s.charAt(ind) == '.')) {
	        res += s.charAt(ind++);
	    }
	    return res;
	}
	// если символ операция
	private char readFunction(String s) {
	    return s.charAt(ind++);
	}
	private void popFunction(Stack<Double> Operands, Stack<Character> Functions) {
	    double B = Operands.pop();
	    double A = Operands.pop();
	    switch (Functions.pop()) {
	        case '+': Operands.push(A + B);
	            break;
	        case '-': Operands.push(A - B);
	            break;
	        case '*': Operands.push(A * B);
	            break;
	        case '/': 
	        	if (B==0) {           // Если деление на 0, то ошибка
	        		throw new RuntimeException ("недопустимая операция"); 
	        	}
	        	Operands.push(A / B);
	            break;
	    }
	}

	private boolean canPop(char op1, Stack<Character> Functions) {
	    if (Functions.size() == 0)
	        return false;
	    int p1 = getPriority(op1);
	    int p2 = getPriority(Functions.peek());
	    return p1 >= 0 && p2 >= 0 && p1 >= p2;
	}

	private int getPriority(char op){
	    switch (op)	    {
	        case '(':
	            return -1; // не выталкивает сам и не дает вытолкнуть себя другим
	        case '*': case '/':
	            return 1;
	        case '+': case '-':
	            return 2;
	        default: 
	            throw new RuntimeException ("недопустимая операция");
	    }
	}	
}
	

