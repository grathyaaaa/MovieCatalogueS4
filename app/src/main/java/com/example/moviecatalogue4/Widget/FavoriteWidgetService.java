package com.example.moviecatalogue4.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavoriteWidgetService extends RemoteViewsService {

    public FavoriteWidgetService() {

    }
    @Override

    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRemoteViewsFactory(this.getApplicationContext());
    }
}
