package com.cotufa.util;


import java.lang.reflect.Field;
import java.util.HashSet;

public class Util {
	
	//Reemplaza todo los campos no null del objetoInicial al objetoFinal
	public static void merge(Object objetoInicial, Object objetoFinal) {
		
		Field[] campos = objetoInicial.getClass().getDeclaredFields();
		
    	for (Field field : campos) {
			try {
				field.setAccessible(campos, true);				
				
				if( field.get(objetoInicial) != null ) {						
					
					Object valor = field.get(objetoInicial);
					
					if(valor instanceof HashSet<?>) {						
						HashSet set = (HashSet) valor;
						System.out.println(valor);
						if(set != null) {
							field.set(objetoFinal, field.get(objetoInicial));
						}
						/*
						if(!set.isEmpty()) {
							field.set(objetoFinal, field.get(objetoInicial));
						}
						*/
					}else {
						field.set(objetoFinal, field.get(objetoInicial));
					}				

					
					
					
				}
				field.setAccessible(campos, false);
			} catch (IllegalArgumentException | IllegalAccessException e) {

			}
		}
    	
	}

}

