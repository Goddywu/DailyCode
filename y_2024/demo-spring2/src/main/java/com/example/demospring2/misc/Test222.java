package com.example.demospring2.misc;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-23
 */
public class Test222 {

    public static void main(String[] args) {
//        System.out.println(getJavaVersionAsFloat());
        System.out.println(String.format("%.0f", 0.2d));
    }

    private static final String JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();
    private static final String JAVA_VERSION = "17";

    private static String getJavaVersionTrimmed() {
        if (JAVA_VERSION != null) {
            for(int i = 0; i < JAVA_VERSION.length(); ++i) {
                char ch = JAVA_VERSION.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    return JAVA_VERSION.substring(i);
                }
            }
        }

        return null;
    }

    private static float getJavaVersionAsFloat() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0.0F;
        } else {
            String str = JAVA_VERSION_TRIMMED.substring(0, 3);
            if (JAVA_VERSION_TRIMMED.length() >= 5) {
                str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
            }

            try {
                return Float.parseFloat(str);
            } catch (Exception var2) {
                return 0.0F;
            }
        }
    }
}
