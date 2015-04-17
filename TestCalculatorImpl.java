package com.calculator;

public class TestCalculatorImpl {
	public static void main(String[] args) {	
		Calculator n=new CalculatorImpl();
		System.out.println(n.evaluate("(6.5-12*3)/15+9"));	  
	}
}
