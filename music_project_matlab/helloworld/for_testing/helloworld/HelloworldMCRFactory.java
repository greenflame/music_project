/*
 * MATLAB Compiler: 5.2 (R2014b)
 * Date: Tue Nov  3 13:29:53 2015
 * Arguments: "-B" "macro_default" "-W" "java:helloworld,HWClass" "-T" "link:lib" "-d" 
 * "/Users/Alexander/Desktop/music_project/helloworld/for_testing" 
 * "class{HWClass:/Users/Alexander/Desktop/music_project/hw.m}" 
 */

package helloworld;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class HelloworldMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "helloworld_4B242A4C331C3F666F1C49BFA3F69AD5";
    
    /** Component name */
    private static final String sComponentName = "helloworld";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(HelloworldMCRFactory.class)
        );
    
    
    private HelloworldMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            HelloworldMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{8,4,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
