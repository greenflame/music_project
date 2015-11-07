package com.company;

import com.mathworks.toolbox.javabuilder.MWException;
import helloworld.HWClass;

public class Main {

    public static void main(String[] args) {
	// write your code here

        try {
            HWClass h = new HWClass();
            h.dispose();
        } catch (MWException e) {
            e.printStackTrace();
        }

    }
}
