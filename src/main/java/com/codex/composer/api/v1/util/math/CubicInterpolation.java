package com.codex.composer.api.v1.util.math;

import net.minecraft.util.math.MathHelper;

public class CubicInterpolation {
    private CubicInterpolation() {}

    public static final double EASE_IN_OUT_CUBIC_X1 = 0.645;
    public static final double EASE_IN_OUT_CUBIC_Y1 = 0.045;
    public static final double EASE_IN_OUT_CUBIC_X2 = 0.355;
    public static final double EASE_IN_OUT_CUBIC_Y2 = 1.000;

    public static double easeInOutCubic(double t) {
        return cubicBezier(
                t,
                EASE_IN_OUT_CUBIC_X1, EASE_IN_OUT_CUBIC_Y1,
                EASE_IN_OUT_CUBIC_X2, EASE_IN_OUT_CUBIC_Y2
        );
    }

    public static double cubicBezier(double t, double x1, double y1, double x2, double y2) {
        t = clamp01(t);
        double u = solveForU(t, x1, x2);
        return cubic(u, y1, y2);
    }

    public static double solveForU(double x, double x1, double x2) {
        double u = x;

        for (int i = 0; i < 5; i++) {
            double xEst = cubic(u, x1, x2);
            double dx = cubicDerivative(u, x1, x2);
            if (dx == 0.0) break;
            u -= (xEst - x) / dx;
            u = clamp01(u);
        }

        return u;
    }

    public static double cubicSET(double start, double end, double t) {
        return cubic(t, start, end);
    }

    public static double cubic(double t, double a1, double a2) {
        double inv = 1.0 - t;
        return (3.0 * inv * inv * t * a1)
                + (3.0 * inv * t * t * a2)
                + (t * t * t);
    }

    public static double cubicDerivative(double t, double a1, double a2) {
        double inv = 1.0 - t;
        return (3.0 * inv * inv * a1)
                + (6.0 * inv * t * (a2 - a1))
                + (3.0 * t * t * (1.0 - a2));
    }

    public static double clamp01(double v) {
        return v < 0 ? 0 : (v > 1 ? 1 : v);
    }

    public static float fClamp01(float v) {
        return MathHelper.clamp(v, 0f, 1f);
    }
}
