package comandre_fernando.httpsgithub.flixdb.components;

import android.view.View;

/**
 * A click listener interface
 */
public interface ClickListener {
    void onClick(@SuppressWarnings("unused") View view, int position);

    void onLongClick(@SuppressWarnings("unused") View view, int position);
}
