
package in.thyferny.images.core.jai.cmm;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;

public class CMMColorSpace extends ICC_ColorSpace {

    private static final long   serialVersionUID  = -6155748576908535877L;

    // Cache of singletons for the predefined color spaces.
    private static ColorSpace   CMYspace;
    private static ColorSpace   CMYKspace;

    private static final String CMY_PROFILE_NAME  = "CMY.pf";
    private static final String CMYK_PROFILE_NAME = "RSWOP.ICM";

    // private static ColorSpace YCC601space;
    // private static ColorSpace YCC601Lspace;
    // private static ColorSpace YCC709space;
    // private static ColorSpace YCC709Lspace;
    // private static ColorSpace YLINARspace;

    private CMMColorSpace(ICC_Profile profile){
        super(profile);
    }

    public static ColorSpace getInstance(int colorspace) {
        ColorSpace theColorSpace = null;
        switch (colorspace) {
            case ColorSpace.TYPE_CMY:
                synchronized (ColorSpace.class) {
                    if (CMYspace == null) {
                        ICC_Profile theProfile = getICC_Profile(CMY_PROFILE_NAME);
                        CMYspace = new ICC_ColorSpace(theProfile);
                    }

                    theColorSpace = CMYspace;
                }
                break;

            case ColorSpace.TYPE_CMYK:
                synchronized (ColorSpace.class) {
                    if (CMYKspace == null) {
                        ICC_Profile theProfile = getICC_Profile(CMYK_PROFILE_NAME);
                        CMYKspace = new ICC_ColorSpace(theProfile);
                    }

                    theColorSpace = CMYKspace;
                }
                break;
            case ColorSpace.TYPE_RGB:
                theColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                break;
            case ColorSpace.TYPE_GRAY:
                theColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                break;
            default:
                throw new IllegalArgumentException("Unknown ColorSpace " + colorspace);
        }
        return theColorSpace;
    }

    public static ICC_Profile getICC_Profile(String name) {
        InputStream is = CMMColorSpace.class.getResourceAsStream("/META-INF/simpleimage/cmm/" + name);
        try {
            ICC_Profile iccp = ICC_Profile.getInstance(is);
            return iccp;
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid icc profile");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
