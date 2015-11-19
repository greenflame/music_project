/*
 * MATLAB Compiler: 6.0 (R2015a)
 * Date: Wed Nov 18 17:30:40 2015
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
    private static final String sComponentId = "matlabModule_BEF3D458F8B138F4198F6AF679E8D781";
    
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
