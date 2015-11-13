/*
 * MATLAB Compiler: 6.0 (R2015a)
 * Date: Fri Nov 13 10:46:49 2015
 * Arguments: "-B" "macro_default" "-W" "java:hw,Class1" "-T" "link:lib" "-d" 
 * "g:\\music_project\\music_project_matlab\\hw\\for_testing" 
 * "class{Class1:g:\\music_project\\music_project_matlab\\hw.m}" 
 */

package hw;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class HwMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "hw_3F593385B1E8EDFFD1597D8350C5F76F";
    
    /** Component name */
    private static final String sComponentName = "hw";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(HwMCRFactory.class)
        );
    
    
    private HwMCRFactory()
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
            HwMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{8,5,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
