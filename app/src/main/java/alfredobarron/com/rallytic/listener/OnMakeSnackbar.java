package alfredobarron.com.rallytic.listener;

import android.support.design.widget.Snackbar;

/**
 * Created by alfredobarron on 25/07/15.
 */
public interface OnMakeSnackbar {

    Snackbar onMakeSnackbar(CharSequence text, int duration);
}
