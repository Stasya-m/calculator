package com.calculator;

import java.util.Stack;  
/* 
Created by Stasya on 15.12.2014.
*/

public class CalculatorImpl implements Calculator {		
	private int ind=0;	                             
	public String evaluate (String s) {
	s='('+s+')';
	Stack<Double> Operands=new Stack<Double>();   
	Stack<Character> Functions= new Stack<Character>();   
	Object token=new Object();   
	Object predToken='K';         
	String result = null;	
	
	try{
	do {
		token=getToken (s);
		
		if (token instanceof Character && predToken instanceof Character && (Character) predToken == '(' && 
				((Character) token == '+' || (Character) token == '-')) {
			Operands.push((double) 0);
		}
		if (token instanceof Double) {  
            Operands.push((Double) token); 
        }						
        else if (token instanceof Character) { 
        	if ((Character) token == ')')  {
           
                while (Functions.size() > 0 && Functions.peek() != '(') {
                    popFunction(Operands, Functions);
                }
                Functions.pop(); 
            }
            else  {
                while (canPop((Character)token, Functions)) { 
                    popFunction(Operands, Functions); 
                }
                Functions.push((Character) token); 
            }
        }
        predToken = token; 
	} while (token!=null);
	
	if (Operands.size() > 1 || Functions.size() > 0) {
        throw new Exception("Oshibka v razbore virazheniya");
	}
	
	result = Double.toString(round (Operands.pop(), 4));		
	
	}
	catch (Exception e)	{
		result="null";
		}
    return result;
	}
	
	private double round (double value, int scale) {
	      return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
	   }
	
	private Object getToken (String s) {
	    if (ind == s.length()) { 
	        return null;
	    }
	    if (Character.isDigit(s.charAt(ind)))  
	        return Double.parseDouble(readDouble(s));
	    else
	        return readFunction(s);		 
	}	
	
	private String readDouble(String s)	{
	    String res = "";
	    while (ind < s.length() && (Character.isDigit(s.charAt(ind)) || s.charAt(ind) == '.')) {
	        res += s.charAt(ind++);
	    }
	    return res;
	}
	
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
	        	if (B==0) {           
	        		throw new RuntimeException ("unallowable operation"); 
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
	            return -1; 
	        case '*': case '/':
	            return 1;
	        case '+': case '-':
	            return 2;
	        default: 
	            throw new RuntimeException ("unallowable operation");
	    }
	}	
}
	

