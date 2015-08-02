package alfredobarron.com.rallytic.listener;

import android.view.View;

public interface OnRecyclerItemClickListener {

    /**
     * To detect click-event on recycler-view.
     */
    void onClickRecyclerItem(View v, int position);
}
