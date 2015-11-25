/*
 * MATLAB Compiler: 6.0 (R2015a)
 * Date: Tue Nov 24 12:05:54 2015
 * Arguments: "-B" "macro_default" "-W" "java:matlabModule,Indexer" "-T" "link:lib" "-d" 
 * "g:\\music_project\\music_project_matlab\\matlabModule\\for_testing" 
 * "class{Indexer:g:\\music_project\\music_project_matlab\\analyse.m}" 
 */

package matlabModule;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class MatlabModuleMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "matlabModule_BF1AF9F479432AE743040D8EC841FFF9";
    
    /** Component name */
    private static final String sComponentName = "matlabModule";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(MatlabModuleMCRFactory.class)
        );
    
    
    private MatlabModuleMCRFactory()
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
            MatlabModuleMCRFactory.class, 
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
