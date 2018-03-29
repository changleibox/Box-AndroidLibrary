package net.izhuo.app.library.helper;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Helper class which allows access to properties of {@link Configuration} in
 * a backward compatible fashion.
 */
public final class ConfigurationHelper {
    private ConfigurationHelper() {
    }

    /**
     * Returns the current height of the available screen space, in dp units.
     *
     * <p>Uses {@code Configuration.screenHeightDp} when available, otherwise an approximation
     * is computed and returned.</p>
     *
     * @deprecated Use {@link Configuration#screenHeightDp} directly.
     */
    @Deprecated
    public static int getScreenHeightDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenHeightDp;
    }

    /**
     * Returns the current width of the available screen space, in dp units.
     *
     * <p>Uses {@code Configuration.screenWidthDp} when available, otherwise an approximation
     * is computed and returned.</p>
     *
     * @deprecated Use {@link Configuration#screenWidthDp} directly.
     */
    @Deprecated
    public static int getScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenWidthDp;
    }

    /**
     * Returns The smallest screen size an application will see in normal operation, in dp units.
     *
     * <p>Uses {@code Configuration.smallestScreenWidthDp} when available, otherwise an
     * approximation is computed and returned.</p>
     *
     * @deprecated Use {@link Configuration#smallestScreenWidthDp} directly.
     */
    @Deprecated
    public static int getSmallestScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp;
    }

    /**
     * Returns the target screen density being rendered to.
     *
     * <p>Uses {@code Configuration.densityDpi} when available, otherwise an approximation
     * is computed and returned.</p>
     */
    public static int getDensityDpi(@NonNull Resources resources) {
        if (SDK_INT >= 17) {
            return resources.getConfiguration().densityDpi;
        } else {
            return resources.getDisplayMetrics().densityDpi;
        }
    }
}