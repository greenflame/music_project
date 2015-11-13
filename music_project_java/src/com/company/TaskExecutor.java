package com.company;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import matlabModule.SpectrumSolver;

import java.util.Arrays;

/**
 * Created by Serge on 13.11.2015.
 */
public class TaskExecutor {
    private static float[] RealPart(float[][] arr) {
        float[] res = new float[arr.length];

        for (int i= 0; i < arr.length; i++) {
            res[i] = arr[i][0];
        }

        return res;
    }

    public static String ExecuteTask(String input) {
        String output;

        try {
            SpectrumSolver c = new SpectrumSolver();
            MWNumericArray spec_mw = (MWNumericArray)c.mySpectrum(1, "g:\\music_project\\music_project_matlab\\y.mp3")[0];

            float[] spec = RealPart((float[][])spec_mw.toFloatArray());

            output = String.format("{\"status\":\"success\",\"spectrum\":%s}", Arrays.toString(spec));

            spec_mw.dispose();
            c.dispose();
        } catch (MWException e) {
            output = "{\"status\":\"error\",\"error_msg\":\"Matlab error.\"}";
        } catch (RuntimeException e) {
            output = "{\"status\":\"error\",\"error_msg\":\"Java error.\"}";
        }

        return output;
    }
}
