package com.annotation.drinking_zone.Utility;

public interface RecyclerViewClickHandler {

    void onDeleteClicked(final int position);

    void onQuantityUpdated(final int position, String quantity);
}
